package com.rayshan.taskscheduler.services;

import com.rayshan.taskscheduler.entities.ScheduledTaskEntity;
import com.rayshan.taskscheduler.entities.TaskLockEntity;
import com.rayshan.taskscheduler.repositories.ScheduledTasksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import com.rayshan.taskscheduler.repositories.TaskLockRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class MyCustomTaskScheduler {
    private static final String SHCEDULER_ID = UUID.randomUUID().toString();

    @Autowired
    private DbService dbService;

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    public void createTasks() {
        ScheduledTaskEntity newTask = dbService.createNewTask(SHCEDULER_ID);
        if(newTask == null) {
            System.out.println(SHCEDULER_ID + " >> Task not created");
        } else {
            System.out.println( SHCEDULER_ID + " >> Created new task: " + newTask.getScheduledTaskId());
        }

    }

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    public void runTask() throws InterruptedException {
        ScheduledTaskEntity scheduledTask = dbService.getOneTaskForExecution();
        if(null == scheduledTask) {
            System.out.println("No task to execute.");
        } else {
            System.out.println("Start executing task :" + scheduledTask.getScheduledTaskId());
            Thread.sleep(2 * 60 * 1000);
            dbService.updateTaskStatus(scheduledTask.getScheduledTaskId(), "SUCCESS");
            System.out.println("End executing task :" + scheduledTask.getScheduledTaskId());
        }

    }
}
