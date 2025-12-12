package org.lld.practice.design_task_scheduler.naive_solution;

import java.util.ArrayList;
import java.util.List;

/**
 * Naive implementation of a Task Scheduler.
 * 
 * This demonstrates common anti-patterns:
 * - Thread per task (doesn't scale)
 * - No priority handling
 * - No retry mechanism
 * - No recurring tasks
 * - No task tracking/cancellation
 * 
 * DO NOT use this pattern in production!
 */
public class SimpleScheduler {
    
    private final List<Thread> threads = new ArrayList<>();
    
    /**
     * Schedule a task with delay.
     * 
     * Problems:
     * - Creates a new thread for each task (resource waste)
     * - Cannot cancel the task
     * - No retry on failure
     * - No priority handling
     */
    public void schedule(Runnable task, long delayMs) {
        Thread thread = new Thread(() -> {
            try {
                System.out.printf("⏳ Task scheduled, waiting %dms...%n", delayMs);
                Thread.sleep(delayMs);
                System.out.println("▶️ Executing task...");
                task.run();
                System.out.println("✅ Task completed");
            } catch (InterruptedException e) {
                System.out.println("❌ Task interrupted");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.out.println("❌ Task failed: " + e.getMessage());
                // No retry! Task is lost forever
            }
        });
        
        threads.add(thread);
        thread.start();
    }
    
    /**
     * Schedule a recurring task.
     * 
     * Problems:
     * - Busy loop with Thread.sleep
     * - Cannot stop the recurring task
     * - Drift issues (doesn't account for execution time)
     */
    public void scheduleRecurring(Runnable task, long intervalMs) {
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    System.out.println("▶️ Executing recurring task...");
                    task.run();
                    Thread.sleep(intervalMs);  // Fixed delay, not fixed rate!
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    System.out.println("❌ Recurring task failed: " + e.getMessage());
                    // Continues running despite failure - no backoff
                }
            }
        });
        
        threads.add(thread);
        thread.start();
    }
    
    /**
     * Get count of active threads.
     */
    public int getActiveThreadCount() {
        return (int) threads.stream()
                .filter(Thread::isAlive)
                .count();
    }
    
    /**
     * Shutdown all tasks (interrupt threads).
     */
    public void shutdown() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
        threads.clear();
        System.out.println("🛑 Scheduler shutdown");
    }
    
    // ========== Demo ==========
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Naive Task Scheduler Demo ===\n");
        System.out.println("⚠️ This demonstrates ANTI-PATTERNS. See improved_solution for proper design.\n");
        
        SimpleScheduler scheduler = new SimpleScheduler();
        
        // Schedule multiple tasks
        scheduler.schedule(() -> System.out.println("   Task 1: Hello!"), 100);
        scheduler.schedule(() -> System.out.println("   Task 2: World!"), 200);
        scheduler.schedule(() -> {
            throw new RuntimeException("Simulated failure");
        }, 150);
        
        // Wait for tasks
        Thread.sleep(500);
        
        System.out.printf("%n⚠️ Active threads: %d (not reused!)%n", 
                scheduler.getActiveThreadCount());
        
        System.out.println("\n⚠️ Problems demonstrated:");
        System.out.println("1. Created 3 threads for 3 tasks (doesn't scale)");
        System.out.println("2. Failed task is lost - no retry");
        System.out.println("3. Cannot cancel individual tasks");
        System.out.println("4. No priority - all tasks equal");
        System.out.println("5. Recurring task has time drift");
        System.out.println("6. No task status tracking");
        
        scheduler.shutdown();
    }
}

