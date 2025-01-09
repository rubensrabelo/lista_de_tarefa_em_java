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
	public ResponseEntity<List<Task>> findAll() {
		List<Task> tasks = service.findAll();
		return ResponseEntity.ok().body(tasks);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Task> findById(@PathVariable Long id) {
		Task task = service.findById(id);
		return ResponseEntity.ok().body(task);
	}
	
	@PostMapping
	public ResponseEntity<Task> create(@RequestBody Task task) {
		task = service.create(task);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(task.getId()).toUri();
		return ResponseEntity.created(uri).body(task);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Task> update(@PathVariable Long id, @RequestBody Task task) {
		task = service.update(id, task);
		return ResponseEntity.ok().body(task);
	}
}
