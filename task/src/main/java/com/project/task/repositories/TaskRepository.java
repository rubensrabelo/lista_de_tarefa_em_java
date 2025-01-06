package com.project.task.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.task.models.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
