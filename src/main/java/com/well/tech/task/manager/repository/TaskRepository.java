package com.well.tech.task.manager.repository;

import com.well.tech.task.manager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID>, JpaSpecificationExecutor<Task> {
       Optional<Task> findByIdAndUserId(UUID id, UUID userId);
}
