package org.lld.practice.design_task_scheduler.improved_solution.scheduler;

import org.lld.practice.design_task_scheduler.improved_solution.models.*;
import org.lld.practice.design_task_scheduler.improved_solution.observer.TaskListener;

import java.time.Duration;
import java.util.Optional;

/**
 * Interface for task scheduler implementations.
 */
public interface TaskScheduler {
    
    /**
     * Schedule a task for execution.
     */
    String schedule(Task task);
    
    /**
     * Cancel a scheduled task.
     */
    boolean cancel(String taskId);
    
    /**
     * Get task by ID.
     */
    Optional<Task> getTask(String taskId);
    
    /**
     * Get task status.
     */
    Optional<TaskStatus> getStatus(String taskId);
    
    /**
     * Add a task listener.
     */
    void addListener(TaskListener listener);
    
    /**
     * Remove a task listener.
     */
    void removeListener(TaskListener listener);
    
    /**
     * Start the scheduler.
     */
    void start();
    
    /**
     * Shutdown the scheduler.
     */
    void shutdown();
    
    /**
     * Check if scheduler is running.
     */
    boolean isRunning();
    
    /**
     * Get scheduler statistics.
     */
    SchedulerStats getStats();
    
    /**
     * Statistics about the scheduler.
     */
    record SchedulerStats(
            int totalTasks,
            int pendingTasks,
            int runningTasks,
            int completedTasks,
            int failedTasks,
            int activeWorkers
    ) {}
}

