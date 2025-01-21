package com.project.task.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.project.task.models.Task;
import com.project.task.services.TaskService;

@RestController
@RequestMapping(value = "/tasks")
public class TaskController {
	
	@Autowired
	private TaskService service;
	
	@GetMapping
	public ResponseEntity<List<Task>> findAllTasksByUserId(@RequestHeader("Authorization") String authorizationHeader) {
		List<Task> tasks = service.findAllTasksByUserId(authorizationHeader);
		return ResponseEntity.ok().body(tasks);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Task> findTaskByIdAndUserId(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
		Task task = service.findTaskByIdAndUserId(id, authorizationHeader);
		return ResponseEntity.ok().body(task);
	}

	@PostMapping
	public ResponseEntity<Task> create(@RequestBody Task task, @RequestHeader("Authorization") String authorizationHeader) {
		task = service.create(task, authorizationHeader);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(task.getId()).toUri();
		return ResponseEntity.created(uri).body(task);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Task> update(@PathVariable Long id, @RequestBody Task task, @RequestHeader("Authorization") String authorizationHeader) {
		task = service.update(id, task, authorizationHeader);
		return ResponseEntity.ok().body(task);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
		service.delete(id, authorizationHeader);
		return ResponseEntity.noContent().build();	
	}
}
