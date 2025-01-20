package com.project.task.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.project.task.infra.security.TokenService;
import com.project.task.models.Task;
import com.project.task.models.User;
import com.project.task.repositories.TaskRepository;
import com.project.task.repositories.UserRepository;
import com.project.task.services.exceptions.ResourceNotFoundException;

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
	
	@GetMapping
	public ResponseEntity<List<Task>> findAllTasksByUserId(@RequestHeader("Authorization") String authorizationHeader) {
		var user = getUserByToken(authorizationHeader);
		List<Task> tasks = taskRepository.findByUserId(user.getId());
		return ResponseEntity.ok().body(tasks);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Task> findTaskByIdAndUserId(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
		var user = getUserByToken(authorizationHeader);
		Task task = taskRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException(Task.class.getName(), id));
		return ResponseEntity.ok().body(task);
	}

	@PostMapping
	public ResponseEntity<Task> create(@RequestBody Task task, @RequestHeader("Authorization") String authorizationHeader) {
		
		var user = getUserByToken(authorizationHeader);
		task.setUser(user);
		Task savedTask = taskRepository.save(task);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(task.getId()).toUri();

		return ResponseEntity.created(uri).body(savedTask);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Task> update(@PathVariable Long id, @RequestBody Task taskUpdate, @RequestHeader("Authorization") String authorizationHeader) {
		var user = getUserByToken(authorizationHeader);
		Task task = taskRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException(Task.class.getName(), taskUpdate.getId()));
		updateData(task, taskUpdate);
		taskRepository.save(task);
		return ResponseEntity.ok().body(task);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
		var user = getUserByToken(authorizationHeader);
		Task task = taskRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException(Task.class.getName(), id));
		taskRepository.delete(task);
		return ResponseEntity.noContent().build();	
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
