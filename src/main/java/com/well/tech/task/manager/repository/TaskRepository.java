package com.well.tech.task.manager.repository;

import com.well.tech.task.manager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {}
