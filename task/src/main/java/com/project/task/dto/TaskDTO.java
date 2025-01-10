package com.project.task.dto;

import com.project.task.models.Task;

public record TaskDTO(Long id, String title,  boolean isCompleted) {

	public TaskDTO(Task task) {
		this(task.getId(), task.getTitle(), task.getCompleted());
	}
}
