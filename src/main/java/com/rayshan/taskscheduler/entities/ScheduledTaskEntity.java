package com.rayshan.taskscheduler.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Table(name="scheduled_tasks")
@Data
@Entity
@Builder
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

    @Column(name = "submitted_time")
    private LocalDateTime submittedTime;

    @Column(name = "retry_count")
    private Long retryCount;

}
