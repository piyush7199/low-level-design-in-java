package org.lld.practice.design_task_scheduler.improved_solution.scheduler;

import org.lld.practice.design_task_scheduler.improved_solution.models.*;
import org.lld.practice.design_task_scheduler.improved_solution.observer.TaskListener;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread pool based task scheduler implementation.
 * Uses a priority queue and scheduled executor service.
 */
public class ThreadPoolTaskScheduler implements TaskScheduler {
    
    private final int workerPoolSize;
    private final PriorityBlockingQueue<Task> taskQueue;
    private final Map<String, Task> tasksById;
    private final List<TaskListener> listeners;
    private final AtomicBoolean running;
    private final AtomicInteger activeWorkers;
    
    private ExecutorService workerPool;
    private ScheduledExecutorService schedulerThread;
    
    // Statistics
    private final AtomicInteger completedCount = new AtomicInteger(0);
    private final AtomicInteger failedCount = new AtomicInteger(0);

    public ThreadPoolTaskScheduler(int workerPoolSize) {
        this.workerPoolSize = workerPoolSize;
        this.taskQueue = new PriorityBlockingQueue<>();
        this.tasksById = new ConcurrentHashMap<>();
        this.listeners = new CopyOnWriteArrayList<>();
        this.running = new AtomicBoolean(false);
        this.activeWorkers = new AtomicInteger(0);
    }

    public ThreadPoolTaskScheduler() {
        this(Runtime.getRuntime().availableProcessors());
    }

    // ========== TaskScheduler Interface ==========

    @Override
    public String schedule(Task task) {
        tasksById.put(task.getTaskId(), task);
        taskQueue.offer(task);
        notifyScheduled(task);
        return task.getTaskId();
    }

    @Override
    public boolean cancel(String taskId) {
        Task task = tasksById.get(taskId);
        if (task != null && task.getStatus() == TaskStatus.PENDING) {
            task.cancel();
            taskQueue.remove(task);
            notifyCancelled(task);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Task> getTask(String taskId) {
        return Optional.ofNullable(tasksById.get(taskId));
    }

    @Override
    public Optional<TaskStatus> getStatus(String taskId) {
        return getTask(taskId).map(Task::getStatus);
    }

    @Override
    public void addListener(TaskListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(TaskListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void start() {
        if (running.compareAndSet(false, true)) {
            workerPool = Executors.newFixedThreadPool(workerPoolSize);
            schedulerThread = Executors.newSingleThreadScheduledExecutor();
            
            // Start polling for tasks
            schedulerThread.scheduleAtFixedRate(
                    this::pollAndExecuteTasks,
                    0, 100, TimeUnit.MILLISECONDS);
            
            System.out.printf("🚀 Scheduler started with %d workers%n", workerPoolSize);
        }
    }

    @Override
    public void shutdown() {
        if (running.compareAndSet(true, false)) {
            schedulerThread.shutdown();
            workerPool.shutdown();
            
            try {
                if (!workerPool.awaitTermination(5, TimeUnit.SECONDS)) {
                    workerPool.shutdownNow();
                }
            } catch (InterruptedException e) {
                workerPool.shutdownNow();
                Thread.currentThread().interrupt();
            }
            
            System.out.println("🛑 Scheduler shutdown");
        }
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public SchedulerStats getStats() {
        int pending = 0, inProgress = 0;
        
        for (Task task : tasksById.values()) {
            switch (task.getStatus()) {
                case PENDING, RETRYING -> pending++;
                case RUNNING -> inProgress++;
            }
        }
        
        return new SchedulerStats(
                tasksById.size(),
                pending,
                inProgress,
                completedCount.get(),
                failedCount.get(),
                activeWorkers.get()
        );
    }

    // ========== Internal Methods ==========

    private void pollAndExecuteTasks() {
        while (running.get()) {
            Task task = taskQueue.peek();
            
            if (task == null) break;
            
            if (!task.isReadyToRun()) {
                // Task is scheduled for future, stop polling
                break;
            }
            
            // Remove from queue and execute
            task = taskQueue.poll();
            if (task != null && task.getStatus() == TaskStatus.PENDING) {
                executeTask(task);
            }
        }
    }

    private void executeTask(Task task) {
        activeWorkers.incrementAndGet();
        
        workerPool.submit(() -> {
            try {
                notifyStarted(task);
                TaskResult result = task.execute();
                
                if (result.isSuccess()) {
                    completedCount.incrementAndGet();
                    notifyCompleted(task, result);
                    
                    // Handle recurring tasks
                    if (task.isRecurring()) {
                        task.rescheduleForNextRun();
                        taskQueue.offer(task);
                        notifyScheduled(task);
                    }
                } else {
                    // Handle failure and retry
                    if (task.canRetry()) {
                        task.scheduleRetry();
                        taskQueue.offer(task);
                        notifyRetry(task, task.getAttemptCount());
                    } else {
                        failedCount.incrementAndGet();
                        notifyFailed(task, result);
                    }
                }
            } finally {
                activeWorkers.decrementAndGet();
            }
        });
    }

    // ========== Listener Notifications ==========

    private void notifyScheduled(Task task) {
        for (TaskListener listener : listeners) {
            listener.onTaskScheduled(task);
        }
    }

    private void notifyStarted(Task task) {
        for (TaskListener listener : listeners) {
            listener.onTaskStarted(task);
        }
    }

    private void notifyCompleted(Task task, TaskResult result) {
        for (TaskListener listener : listeners) {
            listener.onTaskCompleted(task, result);
        }
    }

    private void notifyFailed(Task task, TaskResult result) {
        for (TaskListener listener : listeners) {
            listener.onTaskFailed(task, result);
        }
    }

    private void notifyCancelled(Task task) {
        for (TaskListener listener : listeners) {
            listener.onTaskCancelled(task);
        }
    }

    private void notifyRetry(Task task, int attemptNumber) {
        for (TaskListener listener : listeners) {
            listener.onTaskRetry(task, attemptNumber);
        }
    }

    // ========== Status ==========

    public void printStatus() {
        SchedulerStats stats = getStats();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║        ⏰ SCHEDULER STATUS              ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf("║ Running: %-30s ║%n", running.get() ? "✅ YES" : "❌ NO");
        System.out.printf("║ Worker Pool Size: %-21d ║%n", workerPoolSize);
        System.out.printf("║ Active Workers: %-23d ║%n", stats.activeWorkers());
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf("║ Total Tasks: %-26d ║%n", stats.totalTasks());
        System.out.printf("║ Pending: %-30d ║%n", stats.pendingTasks());
        System.out.printf("║ Running: %-30d ║%n", stats.runningTasks());
        System.out.printf("║ Completed: %-28d ║%n", stats.completedTasks());
        System.out.printf("║ Failed: %-31d ║%n", stats.failedTasks());
        System.out.println("╚════════════════════════════════════════╝\n");
    }
}

