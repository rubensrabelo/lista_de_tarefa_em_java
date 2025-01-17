package com.project.task.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.task.models.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

	List<Task> findByUserId(Long userId);

	Optional<Task> findByIdAndUserId(Long taskId, Long userId);
}
