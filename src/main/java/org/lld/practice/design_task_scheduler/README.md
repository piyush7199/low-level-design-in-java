# Design Task Scheduler

## 1. Problem Statement and Requirements

Design a Task Scheduler system that can schedule, manage, and execute tasks. The scheduler should support one-time tasks, recurring tasks, delayed execution, and priority-based scheduling.

### Functional Requirements:

- **Task Scheduling**: Schedule tasks for immediate, delayed, or recurring execution
- **Priority Support**: Execute higher priority tasks first
- **Recurring Tasks**: Support cron-like recurring schedules (fixed-rate, fixed-delay)
- **Task Management**: Cancel, pause, resume tasks
- **Retry Mechanism**: Automatic retry on failure with configurable policies
- **Task Dependencies**: Execute tasks only after dependencies complete
- **Concurrent Execution**: Execute multiple tasks in parallel with thread pool

### Non-Functional Requirements:

- **Reliability**: Tasks should not be lost on system restart
- **Scalability**: Handle thousands of scheduled tasks
- **Accuracy**: Execute tasks at the scheduled time (within tolerance)
- **Fault Tolerance**: Handle worker failures gracefully

---

## 2. Naive Solution: The "Starting Point"

### The Thought Process:

A beginner might use a simple list with Thread.sleep():

```java
class SimpleScheduler {
    private List<Task> tasks = new ArrayList<>();
    
    public void schedule(Runnable task, long delayMs) {
        new Thread(() -> {
            try {
                Thread.sleep(delayMs);
                task.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
```

### Limitations and Design Flaws:

1. **Thread Per Task**:
   - Creates a new thread for each task
   - Doesn't scale - thousands of threads = OOM
   - No thread reuse

2. **No Priority Handling**:
   - FIFO only
   - Cannot prioritize important tasks

3. **No Retry Mechanism**:
   - Task failure = permanent failure
   - No exponential backoff

4. **No Recurring Tasks**:
   - Only one-time execution
   - No cron-like scheduling

5. **No Task Tracking**:
   - Cannot check task status
   - Cannot cancel scheduled tasks

6. **Not Fault Tolerant**:
   - Tasks lost on restart
   - No persistence

---

## 3. Improved Solution: The "Mentor's Guidance"

### Design Patterns Used:

| Pattern | Usage | Why |
|---------|-------|-----|
| **Strategy** | Scheduling algorithms | Different scheduling strategies |
| **Command** | Task encapsulation | Encapsulate tasks as objects |
| **Observer** | Task events | Notify on task completion/failure |
| **Factory** | Task creation | Create different task types |
| **Singleton** | Scheduler instance | Single scheduler per application |

### Architecture:

```
┌─────────────────────────────────────────────────────────────┐
│                    TASK SCHEDULER ARCHITECTURE              │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌──────────┐    ┌─────────────────────────────────────┐   │
│  │  Client  │───>│          TaskScheduler              │   │
│  └──────────┘    │  ┌─────────────────────────────┐    │   │
│                  │  │     Priority Queue          │    │   │
│                  │  │  ┌─────┬─────┬─────┬─────┐ │    │   │
│                  │  │  │ P=1 │ P=2 │ P=3 │ ... │ │    │   │
│                  │  │  │Task │Task │Task │     │ │    │   │
│                  │  │  └─────┴─────┴─────┴─────┘ │    │   │
│                  │  └─────────────────────────────┘    │   │
│                  └──────────────────┬──────────────────┘   │
│                                     │                       │
│                        ┌────────────┴────────────┐         │
│                        ▼                         ▼         │
│                  ┌──────────┐              ┌──────────┐    │
│                  │ Worker 1 │              │ Worker N │    │
│                  │ (Thread) │    ...       │ (Thread) │    │
│                  └──────────┘              └──────────┘    │
│                        └────────── Thread Pool ───────┘    │
└─────────────────────────────────────────────────────────────┘
```

### Task States:

```
┌──────────┐     ┌───────────┐     ┌───────────┐
│ PENDING  │────>│  RUNNING  │────>│ COMPLETED │
└──────────┘     └───────────┘     └───────────┘
     │                │                   
     │                │ (failure)         
     │                ▼                   
     │           ┌───────────┐     ┌───────────┐
     │           │  FAILED   │────>│ RETRYING  │──┐
     │           └───────────┘     └───────────┘  │
     │                                    │       │
     ▼                                    ▼       │
┌──────────┐                        (back to     │
│CANCELLED │                         PENDING)────┘
└──────────┘
```

### Scheduling Types:

```
┌─────────────────────────────────────────────────────────────┐
│                    SCHEDULING TYPES                         │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  1. ONE-TIME (Delayed)                                      │
│     ────────────────────────────────────────>               │
│     now                          delay        execute       │
│                                                             │
│  2. FIXED-RATE (Recurring)                                  │
│     ────┬────┬────┬────┬────────────────────>               │
│         │    │    │    │                                    │
│         T1   T2   T3   T4   (execute every N seconds)       │
│                                                             │
│  3. FIXED-DELAY (Recurring)                                 │
│     ────T1────delay────T2────delay────T3────>               │
│              │              │                               │
│         (delay AFTER task completes)                        │
│                                                             │
│  4. CRON (Recurring)                                        │
│     "0 0 * * *" = every day at midnight                    │
│     "*/5 * * * *" = every 5 minutes                        │
└─────────────────────────────────────────────────────────────┘
```

### Priority Queue:

```
┌─────────────────────────────────────────────────────────────┐
│                    PRIORITY QUEUE                           │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Tasks ordered by:                                          │
│  1. Scheduled time (earliest first)                        │
│  2. Priority (if same time, higher priority first)         │
│                                                             │
│  ┌───────────────────────────────────────────────────┐     │
│  │ Time=10:00 P=1 │ Time=10:00 P=5 │ Time=10:05 P=1 │     │
│  │    (first)     │    (second)    │    (third)     │     │
│  └───────────────────────────────────────────────────┘     │
│                                                             │
│  Java: PriorityBlockingQueue with custom Comparator        │
└─────────────────────────────────────────────────────────────┘
```

### Core Classes:

#### 1. Models (`models/`)
- `Task` - Task with command, schedule, priority
- `TaskStatus` - Enum: PENDING, RUNNING, COMPLETED, FAILED, CANCELLED
- `ScheduleType` - Enum: ONE_TIME, FIXED_RATE, FIXED_DELAY
- `TaskResult` - Result of task execution
- `RetryPolicy` - Retry configuration (max attempts, backoff)

#### 2. Scheduler (`scheduler/`)
- `TaskScheduler` - Main scheduler interface
- `ThreadPoolTaskScheduler` - Thread pool based implementation
- `ScheduledTask` - Wrapper with scheduling metadata

#### 3. Executor (`executor/`)
- `TaskExecutor` - Executes tasks
- `RetryExecutor` - Handles retries with backoff

### Class Diagram:

```
┌─────────────────────────────────────────────────────────────┐
│                        Task                                 │
├─────────────────────────────────────────────────────────────┤
│ - taskId: String                                            │
│ - name: String                                              │
│ - command: Runnable                                         │
│ - priority: int                                             │
│ - scheduleType: ScheduleType                                │
│ - scheduledTime: Instant                                    │
│ - interval: Duration                                        │
│ - status: TaskStatus                                        │
│ - retryPolicy: RetryPolicy                                  │
│ - attempts: int                                             │
├─────────────────────────────────────────────────────────────┤
│ + execute(): TaskResult                                     │
│ + cancel(): void                                            │
│ + reschedule(time): void                                    │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ scheduled by
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    TaskScheduler                            │
├─────────────────────────────────────────────────────────────┤
│ - taskQueue: PriorityBlockingQueue<Task>                   │
│ - executorService: ScheduledExecutorService                │
│ - tasks: Map<String, Task>                                 │
├─────────────────────────────────────────────────────────────┤
│ + schedule(task): String                                   │
│ + scheduleWithDelay(task, delay): String                   │
│ + scheduleAtFixedRate(task, period): String                │
│ + cancel(taskId): boolean                                  │
│ + getStatus(taskId): TaskStatus                            │
│ + shutdown(): void                                         │
└─────────────────────────────────────────────────────────────┘
```

---

## 4. Final Design Overview

### Task Execution Flow:

```
┌──────────┐    ┌───────────────┐    ┌──────────────────┐
│  Client  │───>│   schedule()  │───>│  PriorityQueue   │
│          │    │   (Task)      │    │  (sorted by time │
└──────────┘    └───────────────┘    │   and priority)  │
                                      └────────┬─────────┘
                                               │
                                               ▼
                                      ┌──────────────────┐
                                      │  Worker Thread   │
                                      │  (poll & wait)   │
                                      └────────┬─────────┘
                                               │
                    ┌──────────────────────────┴──────────────────────────┐
                    ▼                          ▼                          ▼
              ┌───────────┐              ┌───────────┐              ┌───────────┐
              │ Execute   │              │ On Fail   │              │ If Recurring│
              │   Task    │              │  Retry?   │              │ Reschedule │
              └─────┬─────┘              └─────┬─────┘              └───────────┘
                    │                          │
                    ▼                          ▼
              ┌───────────┐              ┌───────────┐
              │ COMPLETED │              │  FAILED   │
              └───────────┘              └───────────┘
```

### Retry with Exponential Backoff:

```
┌─────────────────────────────────────────────────────────────┐
│               EXPONENTIAL BACKOFF                           │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Attempt 1: Fail → wait 1s                                 │
│  Attempt 2: Fail → wait 2s                                 │
│  Attempt 3: Fail → wait 4s                                 │
│  Attempt 4: Fail → wait 8s                                 │
│  Attempt 5: Fail → GIVE UP (max retries reached)           │
│                                                             │
│  Formula: delay = baseDelay * (2 ^ attemptNumber)          │
│  With jitter: delay = delay * (0.5 + random(0.5))          │
└─────────────────────────────────────────────────────────────┘
```

### Interview Discussion Points:

1. **How to handle scheduler crashes?**
   - Persist tasks to database
   - On restart, reload pending tasks
   - Use distributed lock for single execution

2. **How to scale to multiple nodes?**
   - Distributed task queue (Redis, RabbitMQ)
   - Database-backed scheduling
   - Leader election for coordination

3. **How to ensure exactly-once execution?**
   - Idempotent task design
   - Distributed locks (Redis SETNX)
   - Database row-level locking

4. **How to handle long-running tasks?**
   - Heartbeat mechanism
   - Task timeout with forced termination
   - Progress tracking and checkpointing

5. **How to monitor task health?**
   - Metrics: success/failure rate, latency
   - Dead letter queue for failed tasks
   - Alerting on repeated failures

