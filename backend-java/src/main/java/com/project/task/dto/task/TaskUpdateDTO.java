package com.project.task.dto.task;

public class TaskUpdateDTO {

	private String title;

	public TaskUpdateDTO(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
