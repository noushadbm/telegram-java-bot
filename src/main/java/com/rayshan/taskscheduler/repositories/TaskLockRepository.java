package com.rayshan.taskscheduler.repositories;

import com.rayshan.taskscheduler.entities.TaskLockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;

@Repository
public interface TaskLockRepository extends JpaRepository<TaskLockEntity, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({
            @QueryHint(name = "jakarta.persistence.lock.timeout", value = "1000")
    })
    Optional<TaskLockEntity> findByTaskIdAndLastExecutionLessThan(String taskId, long timestamp);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({
            @QueryHint(name = "jakarta.persistence.lock.timeout", value = "1000")
    })
    Optional<TaskLockEntity> findByTaskId(String lockId);
}
