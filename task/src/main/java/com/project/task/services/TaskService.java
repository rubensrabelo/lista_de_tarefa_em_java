package com.project.task.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.project.task.infra.security.TokenService;
import com.project.task.models.Task;
import com.project.task.models.User;
import com.project.task.repositories.TaskRepository;
import com.project.task.repositories.UserRepository;
import com.project.task.services.exceptions.ResourceNotFoundException;

@Service
public class TaskService {
	
	private final TokenService tokenService;
	private final TaskRepository taskRepository;
	private final UserRepository userRepository;

	public TaskService(TaskRepository taskRepository, UserRepository userRepository, TokenService tokenService) {
		this.taskRepository = taskRepository;
		this.userRepository = userRepository;
		this.tokenService = tokenService;
	}
	
	public List<Task> findAllTasksByUserId(String authorizationHeader) {
		var user = getUserByToken(authorizationHeader);
		List<Task> tasks = taskRepository.findByUserId(user.getId());
		return tasks;
	}
	
	public Task findTaskByIdAndUserId(Long id, String authorizationHeader) {
		var user = getUserByToken(authorizationHeader);
		Task task = taskRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException(Task.class.getName(), id));
		return task;
	}

	public Task create(Task task, String authorizationHeader) {
		
		var user = getUserByToken(authorizationHeader);
		task.setUser(user);
		task = taskRepository.save(task);
		
		return task;
	}
	
	public Task update(Long id, Task taskUpdate, String authorizationHeader) {
		var user = getUserByToken(authorizationHeader);
		Task task = taskRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException(Task.class.getName(), taskUpdate.getId()));
		updateData(task, taskUpdate);
		taskRepository.save(task);
		return task;
	}
	
	public void delete(Long id, String authorizationHeader) {
		var user = getUserByToken(authorizationHeader);
		Task task = taskRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException(Task.class.getName(), id));
		taskRepository.delete(task);	
	}

	private User getUserByToken(String authorizationHeader) {
		String token = authorizationHeader.replace("Bearer ", "");
        var login = this.tokenService.validateToken(token);   
		User user = userRepository.findByEmail(login)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		return user;
	}
	
	private void updateData(Task task, Task taskUpdate) {
		task.setTitle(taskUpdate.getTitle());
		task.setCompleted(taskUpdate.isCompleted());
	}
	
}
