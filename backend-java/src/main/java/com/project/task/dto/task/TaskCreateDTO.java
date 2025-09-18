package com.project.task.dto.task;

import com.project.task.models.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class TaskCreateDTO {

	// Adiciona bean validation

	private String title;

	public TaskCreateDTO(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
