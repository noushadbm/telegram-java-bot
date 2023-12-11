package com.rayshan.taskscheduler.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "task_lock")
public class TaskLockEntity {

    @Id
    @Column(name = "task_id")
    private String taskId;

    @Column(name = "last_execution")
    private Long lastExecution;
}
