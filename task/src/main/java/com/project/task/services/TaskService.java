package com.project.task.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.project.task.models.Task;
import com.project.task.repositories.TaskRepository;
import com.project.task.services.exceptions.DatabaseException;
import com.project.task.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TaskService {
	
	@Autowired
	private TaskRepository repository;
	
	public List<Task> findAll() {
		List<Task> tasks = repository.findAll();
		return tasks;
	}
	
	public Task findById(Long id) {
		Task task = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Task", id));
		return task;
	}
	
	public Task create(Task task) {
		task = repository.save(task);
		return task;
	}
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Task", id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	public Task update(Long id, Task task) {
		try {
			Task taskUpdate = repository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Task", id));
			updateData(task, taskUpdate);
			return taskUpdate;
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Task", id);
		}
	}

	private void updateData(Task task, Task taskUpdate) {
		taskUpdate.setTitle(task.getTitle());
		taskUpdate.setCompleted(task.getCompleted());
	}
}
