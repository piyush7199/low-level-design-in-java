package org.lld.practice.design_task_scheduler.improved_solution.observer;

import org.lld.practice.design_task_scheduler.improved_solution.models.Task;
import org.lld.practice.design_task_scheduler.improved_solution.models.TaskResult;

/**
 * Observer interface for task lifecycle events.
 */
public interface TaskListener {
    
    /**
     * Called when a task is scheduled.
     */
    default void onTaskScheduled(Task task) {}
    
    /**
     * Called when task execution starts.
     */
    default void onTaskStarted(Task task) {}
    
    /**
     * Called when task completes successfully.
     */
    default void onTaskCompleted(Task task, TaskResult result) {}
    
    /**
     * Called when task fails.
     */
    default void onTaskFailed(Task task, TaskResult result) {}
    
    /**
     * Called when task is cancelled.
     */
    default void onTaskCancelled(Task task) {}
    
    /**
     * Called when task is being retried.
     */
    default void onTaskRetry(Task task, int attemptNumber) {}
}

