package com.rayshan.taskscheduler.services;

import com.rayshan.taskscheduler.entities.ScheduledTaskEntity;
import com.rayshan.taskscheduler.entities.TaskLockEntity;
import com.rayshan.taskscheduler.repositories.ScheduledTasksRepository;
import com.rayshan.taskscheduler.repositories.TaskLockRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DbService {
    private final TaskLockRepository taskLockRepository;
    private final ScheduledTasksRepository scheduledTasksRepository;

    public DbService(TaskLockRepository taskLockRepository, ScheduledTasksRepository scheduledTasksRepository) {
        this.taskLockRepository = taskLockRepository;
        this.scheduledTasksRepository = scheduledTasksRepository;
    }
    @Transactional
    public ScheduledTaskEntity createNewTask(String createdBy) {
        long currentTime = System.currentTimeMillis();
        long scheduledRate = Duration.of(5, ChronoUnit.SECONDS).toMillis(); // 1h
        Optional<TaskLockEntity> taskLockOpt = taskLockRepository.findByTaskIdAndLastExecutionLessThan("produce", currentTime - scheduledRate);
        //taskLockRepository.findByTaskIdAndLastExecutionLessThan("produce", currentTime - scheduledRate)
        if(taskLockOpt.isPresent()) {
            List<ScheduledTaskEntity> pending = scheduledTasksRepository.findByStatus("PENDING");
            // Keep maximum 10 in pending.
            if(pending.size() < 10) {
                TaskLockEntity createTask = taskLockOpt.get();
                ScheduledTaskEntity newTask = new ScheduledTaskEntity(UUID.randomUUID().toString(), createdBy, "PENDING");
                ScheduledTaskEntity newScheduledTask = scheduledTasksRepository.save(newTask);
                createTask.setLastExecution(System.currentTimeMillis());
                return newScheduledTask;
            }
        }
        return null;
    }

    @Transactional
    public ScheduledTaskEntity getOneTaskForExecution() {
        long currentTime = System.currentTimeMillis();
        long scheduledRate = Duration.of(25, ChronoUnit.SECONDS).toMillis();
        Optional<TaskLockEntity> taskLock = taskLockRepository.findByTaskIdAndLastExecutionLessThan("consume", currentTime - scheduledRate);

        if(taskLock == null) {
            System.out.println("Didn't get lock.");
        } else {
            // Get one task for execution.
            ScheduledTaskEntity pendingTaskForExecution = scheduledTasksRepository.getPendingTaskForExecution();
            if(pendingTaskForExecution != null) {
                pendingTaskForExecution.setStatus("IN-PROGRESS");
                return pendingTaskForExecution;
            }
        }
        return null;
    }

    @Transactional
    public void updateTaskStatus(String taskId, String status) {
        Optional<ScheduledTaskEntity> taskToUpdate = scheduledTasksRepository.findById(taskId);
        taskToUpdate.ifPresent(task -> {
            task.setStatus(status);
            System.out.println("Task: " + taskId + " status updated to " + status);
        });
    }
}
