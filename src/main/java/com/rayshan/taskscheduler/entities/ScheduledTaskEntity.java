package com.rayshan.taskscheduler.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="scheduled_tasks")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledTaskEntity {
    @Id
    @Column(name = "scheduled_task_id")
    private String scheduledTaskId;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "status")
    private String status;
}
