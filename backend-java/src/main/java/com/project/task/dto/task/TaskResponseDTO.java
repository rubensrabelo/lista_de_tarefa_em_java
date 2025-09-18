package com.project.task.dto.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.task.models.Task;
import com.project.task.models.User;
import jakarta.persistence.*;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Objects;

public class TaskResponseDTO {

	private Long id;
	private String title;
	private boolean isCompleted;
	private boolean isActive;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private User user;

	public TaskResponseDTO(Long id, String title, boolean isCompleted, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.title = title;
		this.isCompleted = isCompleted;
		this.isActive = isActive;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean completed) {
		isCompleted = completed;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		TaskResponseDTO that = (TaskResponseDTO) o;
		return isCompleted == that.isCompleted && isActive == that.isActive && Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, isCompleted, isActive, createdAt, updatedAt);
	}
}
