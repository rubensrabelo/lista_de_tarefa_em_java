package com.project.task.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.task.models.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

	Page<Task> findByUserId(Long userId, Pageable pageable);

	Optional<Task> findByIdAndUserId(Long taskId, Long userId);
}
