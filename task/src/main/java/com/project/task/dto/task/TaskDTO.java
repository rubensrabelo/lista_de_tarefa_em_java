package com.project.task.dto.task;

import com.project.task.models.Task;

public record TaskDTO(Long id, String title,  boolean isCompleted, boolean isActive) {

	public TaskDTO(Task task) {
		this(task.getId(), task.getTitle(), task.isCompleted(), task.isActive());
	}
}
