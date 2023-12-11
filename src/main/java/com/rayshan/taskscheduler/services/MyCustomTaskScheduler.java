package com.rayshan.taskscheduler.services;

import com.rayshan.taskscheduler.entities.ScheduledTaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class MyCustomTaskScheduler {
    private static final String SCHEDULER_ID = UUID.randomUUID().toString();

    @Autowired
    private DbService dbService;

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    public void createTasks() {
        ScheduledTaskEntity newTask = dbService.createNewTask(SCHEDULER_ID);
        if(newTask == null) {
            System.out.println(SCHEDULER_ID + " >> Task not created");
        } else {
            System.out.println( SCHEDULER_ID + " >> Created new task: " + newTask.getScheduledTaskId());
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

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
    public void reRunTask() {
        // Run tasks stuck for more than 10 minutes.
        dbService.reRunStuckTasks();
    }
}
