package org.lld.practice.design_task_scheduler.improved_solution.observer;

import org.lld.practice.design_task_scheduler.improved_solution.models.Task;
import org.lld.practice.design_task_scheduler.improved_solution.models.TaskResult;

/**
 * Task listener that logs all events to console.
 */
public class LoggingTaskListener implements TaskListener {
    
    @Override
    public void onTaskScheduled(Task task) {
        System.out.printf("📋 Scheduled: %s [%s] at %s%n", 
                task.getName(), task.getTaskId(), task.getScheduledTime());
    }
    
    @Override
    public void onTaskStarted(Task task) {
        System.out.printf("▶️ Started: %s [%s]%n", task.getName(), task.getTaskId());
    }
    
    @Override
    public void onTaskCompleted(Task task, TaskResult result) {
        System.out.printf("✅ Completed: %s [%s] in %dms (attempt %d)%n", 
                task.getName(), task.getTaskId(), 
                result.getExecutionTime().toMillis(),
                result.getAttemptNumber());
    }
    
    @Override
    public void onTaskFailed(Task task, TaskResult result) {
        System.out.printf("❌ Failed: %s [%s] - %s (attempt %d)%n", 
                task.getName(), task.getTaskId(), 
                result.getErrorMessage(),
                result.getAttemptNumber());
    }
    
    @Override
    public void onTaskCancelled(Task task) {
        System.out.printf("🚫 Cancelled: %s [%s]%n", task.getName(), task.getTaskId());
    }
    
    @Override
    public void onTaskRetry(Task task, int attemptNumber) {
        System.out.printf("🔄 Retrying: %s [%s] - attempt %d scheduled at %s%n", 
                task.getName(), task.getTaskId(), 
                attemptNumber + 1, task.getScheduledTime());
    }
}

