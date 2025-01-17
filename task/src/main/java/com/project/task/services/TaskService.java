package com.project.task.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.project.task.dto.task.TaskUpdateData;
import com.project.task.models.Task;
import com.project.task.models.User;
import com.project.task.repositories.TaskRepository;
import com.project.task.repositories.UserRepository;
import com.project.task.services.exceptions.DatabaseException;
import com.project.task.services.exceptions.ResourceNotFoundException;

@Service
public class TaskService {

	@Autowired
	private TaskRepository repository;
	
	@Autowired
	private UserRepository userRepository;

	public List<Task> findAllTasksByUser(UserDetails authenticatedUser) {
		User user = getAuthenticatedUser(authenticatedUser);
		List<Task> tasks = repository.findByUserId(user.getId());
		return tasks;
	}

	public Task findTaskByUser(Long id, UserDetails authenticatedUser) {
		User user = getAuthenticatedUser(authenticatedUser);
		Task task = repository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Task", id));
		return task;
	}

	public Task create(Task task, UserDetails authenticatedUser) {
		User user = getAuthenticatedUser(authenticatedUser);
		task.setUser(user);
		task = repository.save(task);
		return task;
	}

	public void delete(Long id, UserDetails authenticatedUser) {
		try {
			User user = getAuthenticatedUser(authenticatedUser);
			Task task = repository.findByIdAndUserId(id, user.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Task", id));
			repository.delete(task);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Task", id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	public Task update(Long id, TaskUpdateData taskUpdate, UserDetails authenticatedUser) {
		User user = getAuthenticatedUser(authenticatedUser);
		Task task = repository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Task", id));
		updateData(task, taskUpdate);
		task = repository.save(task);
		return task;
	}

	private void updateData(Task task, TaskUpdateData taskUpdate) {
		task.setTitle(taskUpdate.title());
		task.setCompleted(taskUpdate.isCompleted());
	}
	
	private User getAuthenticatedUser(UserDetails authenticatedUser) {
		return userRepository.findByEmail(authenticatedUser.getUsername())
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
	}
}
