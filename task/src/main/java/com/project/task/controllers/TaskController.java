package com.project.task.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.project.task.infra.security.TokenService;
import com.project.task.models.Task;
import com.project.task.models.User;
import com.project.task.repositories.TaskRepository;
import com.project.task.repositories.UserRepository;

@RestController
@RequestMapping(value = "/tasks")
public class TaskController {
	
	private final TokenService tokenService;
	private final TaskRepository taskRepository;
	private final UserRepository userRepository;

	public TaskController(TaskRepository taskRepository, UserRepository userRepository, TokenService tokenService) {
		this.taskRepository = taskRepository;
		this.userRepository = userRepository;
		this.tokenService = tokenService;
	}

	@PostMapping
	public ResponseEntity<Task> createTask(@RequestBody Task task, @RequestHeader("Authorization") String authorizationHeader) {
		
		String token = authorizationHeader.replace("Bearer ", "");

        var login = this.tokenService.validateToken(token);

		User user = userRepository.findByEmail(login)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

		task.setUser(user);
		Task savedTask = taskRepository.save(task);

		return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
	}
}
