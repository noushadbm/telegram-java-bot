package com.rayshan.taskscheduler.repositories;

import com.rayshan.taskscheduler.entities.ScheduledTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduledTasksRepository extends JpaRepository<ScheduledTaskEntity, String> {
    @Query( value = "select * from scheduled_tasks where status = 'PENDING' and rownum = 1", nativeQuery = true)
    ScheduledTaskEntity getPendingTaskForExecution();

    List<ScheduledTaskEntity> findByStatus(String status);

    @Query( value = "select * from scheduled_tasks where status = 'IN-PROGRESS' and submitted_time < :beginTime and rownum = 1", nativeQuery = true)
    ScheduledTaskEntity getStuckTask(LocalDateTime beginTime);
}
