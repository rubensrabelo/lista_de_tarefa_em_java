package com.project.task.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.project.task.dto.task.TaskDTO;
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
	
	public List<TaskDTO> findAllTasksByUserId(String authorizationHeader) {
		var user = getUserByToken(authorizationHeader);
		List<Task> entities = taskRepository.findByUserId(user.getId());
		List<TaskDTO> dtos = entities.stream()
								.map(e -> new TaskDTO(e))
								.collect(Collectors.toList());
		return dtos;
	}
	
	public TaskDTO findTaskByIdAndUserId(Long id, String authorizationHeader) {
		var user = getUserByToken(authorizationHeader);
		Task entity = taskRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException(Task.class.getName(), id));
		TaskDTO dto = new TaskDTO(entity);
		return dto;
	}

	public TaskDTO create(TaskDTO dto, String authorizationHeader) {
		
		var user = getUserByToken(authorizationHeader);
		var entity = new Task(dto);
		entity.setUser(user);
		entity = taskRepository.save(entity);
		dto = new TaskDTO(entity);
		
		return dto;
	}
	
	public TaskDTO update(Long id, TaskDTO dto, String authorizationHeader) {
		var user = getUserByToken(authorizationHeader);
		Task entity = taskRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException(Task.class.getName(), id));
		Task update = new Task(dto);
		updateData(entity, update);
		taskRepository.save(entity);
		dto = new TaskDTO(entity);
		return dto;
	}
	
	public void delete(Long id, String authorizationHeader) {
		var user = getUserByToken(authorizationHeader);
		Task task = taskRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException(Task.class.getName(), id));
		task.setActive(false);
		taskRepository.save(task);	
	}

	private User getUserByToken(String authorizationHeader) {
		String token = authorizationHeader.replace("Bearer ", "");
        var login = this.tokenService.validateToken(token);   
		User user = userRepository.findByEmail(login)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		return user;
	}
	
	private void updateData(Task entity, Task update) {
		entity.setTitle(update.getTitle());
		entity.setCompleted(update.isCompleted());
	}
}
