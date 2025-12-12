package org.lld.practice.design_task_scheduler.improved_solution;

import org.lld.practice.design_task_scheduler.improved_solution.models.*;
import org.lld.practice.design_task_scheduler.improved_solution.observer.LoggingTaskListener;
import org.lld.practice.design_task_scheduler.improved_solution.scheduler.TaskScheduler;
import org.lld.practice.design_task_scheduler.improved_solution.scheduler.ThreadPoolTaskScheduler;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demo application for the Task Scheduler.
 * 
 * Demonstrates:
 * - One-time delayed tasks
 * - Priority-based execution
 * - Recurring tasks (fixed-rate)
 * - Retry with exponential backoff
 * - Task cancellation
 */
public class Main {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║     ⏰ TASK SCHEDULER - DEMO                                   ║");
        System.out.println("║     Features: Priority, Retry, Recurring, Thread Pool         ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");
        
        // Create scheduler with 4 workers
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler(4);
        scheduler.addListener(new LoggingTaskListener());
        scheduler.start();
        
        // Demo 1: One-time delayed tasks
        demoDelayedTasks(scheduler);
        
        // Demo 2: Priority-based execution
        demoPriorityTasks(scheduler);
        
        // Demo 3: Retry mechanism
        demoRetryMechanism(scheduler);
        
        // Demo 4: Recurring tasks
        demoRecurringTasks(scheduler);
        
        // Demo 5: Task cancellation
        demoTaskCancellation(scheduler);
        
        // Final status
        scheduler.printStatus();
        
        // Cleanup
        scheduler.shutdown();
        
        printSummary();
    }
    
    private static void demoDelayedTasks(TaskScheduler scheduler) throws InterruptedException {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 1: One-Time Delayed Tasks");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        // Schedule tasks with different delays
        Task task1 = Task.builder("SendEmail", () -> {
            System.out.println("   📧 Sending welcome email...");
            sleep(50);
        }).delay(Duration.ofMillis(100)).build();
        
        Task task2 = Task.builder("GenerateReport", () -> {
            System.out.println("   📊 Generating daily report...");
            sleep(100);
        }).delay(Duration.ofMillis(200)).build();
        
        scheduler.schedule(task1);
        scheduler.schedule(task2);
        
        // Wait for tasks to complete
        Thread.sleep(500);
        System.out.println();
    }
    
    private static void demoPriorityTasks(TaskScheduler scheduler) throws InterruptedException {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 2: Priority-Based Execution");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        System.out.println("Scheduling 3 tasks with same time but different priorities:\n");
        
        // All tasks scheduled for the same time
        Task lowPriority = Task.builder("LowPriorityTask", () -> {
            System.out.println("   🔵 LOW priority task running");
        }).priority(TaskPriority.LOW).build();
        
        Task normalPriority = Task.builder("NormalPriorityTask", () -> {
            System.out.println("   🟡 NORMAL priority task running");
        }).priority(TaskPriority.NORMAL).build();
        
        Task criticalPriority = Task.builder("CriticalPriorityTask", () -> {
            System.out.println("   🔴 CRITICAL priority task running");
        }).priority(TaskPriority.CRITICAL).build();
        
        // Schedule in reverse priority order
        scheduler.schedule(lowPriority);
        scheduler.schedule(normalPriority);
        scheduler.schedule(criticalPriority);
        
        Thread.sleep(300);
        System.out.println("\n✅ Higher priority tasks execute first!\n");
    }
    
    private static void demoRetryMechanism(TaskScheduler scheduler) throws InterruptedException {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 3: Retry with Exponential Backoff");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        AtomicInteger attemptCounter = new AtomicInteger(0);
        
        // Task that fails first 2 times, succeeds on 3rd attempt
        Task retryTask = Task.builder("FlakyServiceCall", () -> {
            int attempt = attemptCounter.incrementAndGet();
            System.out.printf("   🔌 Calling external service (attempt %d)...%n", attempt);
            
            if (attempt < 3) {
                throw new RuntimeException("Service unavailable");
            }
            System.out.println("   ✅ Service call successful!");
        })
        .retryPolicy(new RetryPolicy(3, Duration.ofMillis(100), 2.0))
        .build();
        
        scheduler.schedule(retryTask);
        
        // Wait for retries
        Thread.sleep(1000);
        System.out.println();
    }
    
    private static void demoRecurringTasks(TaskScheduler scheduler) throws InterruptedException {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 4: Recurring Tasks (Fixed Rate)");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        AtomicInteger counter = new AtomicInteger(0);
        
        Task recurringTask = Task.builder("HealthCheck", () -> {
            int count = counter.incrementAndGet();
            System.out.printf("   💓 Health check #%d%n", count);
        })
        .fixedRate(Duration.ofMillis(200))
        .noRetry()
        .build();
        
        String taskId = scheduler.schedule(recurringTask);
        
        // Let it run 3 times
        Thread.sleep(650);
        
        // Cancel the recurring task
        scheduler.cancel(taskId);
        System.out.println();
    }
    
    private static void demoTaskCancellation(TaskScheduler scheduler) throws InterruptedException {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 5: Task Cancellation");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        // Schedule a task with delay
        Task cancelableTask = Task.builder("LongRunningReport", () -> {
            System.out.println("   📄 This should not print - task was cancelled!");
        })
        .delay(Duration.ofSeconds(1))
        .build();
        
        String taskId = scheduler.schedule(cancelableTask);
        
        // Cancel before it runs
        Thread.sleep(100);
        boolean cancelled = scheduler.cancel(taskId);
        
        System.out.printf("Task cancellation: %s%n%n", cancelled ? "✅ Successful" : "❌ Failed");
        
        // Verify it doesn't run
        Thread.sleep(200);
    }
    
    private static void printSummary() {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO COMPLETE - KEY CONCEPTS DEMONSTRATED:");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("✅ Thread Pool: Efficient worker thread management");
        System.out.println("✅ Priority Queue: Higher priority tasks execute first");
        System.out.println("✅ Delayed Execution: Schedule for future time");
        System.out.println("✅ Recurring Tasks: Fixed-rate and fixed-delay");
        System.out.println("✅ Retry Policy: Exponential backoff on failure");
        System.out.println("✅ Task Lifecycle: PENDING → RUNNING → COMPLETED/FAILED");
        System.out.println("✅ Observer Pattern: Task event notifications");
        System.out.println();
        System.out.println("🎯 Interview Discussion Points:");
        System.out.println("   - Distributed scheduling with Redis/database");
        System.out.println("   - Exactly-once execution with distributed locks");
        System.out.println("   - Dead letter queues for failed tasks");
        System.out.println("   - Task dependencies and DAG execution");
        System.out.println("   - Cron expression parsing");
    }
    
    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

