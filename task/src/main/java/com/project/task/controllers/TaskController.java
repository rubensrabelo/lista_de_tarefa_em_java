package com.project.task.controllers;


import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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

import com.project.task.dto.TaskDTO;
import com.project.task.models.Task;
import com.project.task.services.TaskService;

@RestController
@RequestMapping(value = "/tasks")
public class TaskController {
	
	@Autowired
	private TaskService service;
	
	@GetMapping
	public ResponseEntity<List<TaskDTO>> findAll() {
		List<Task> tasks = service.findAll();
		List<TaskDTO> tasksDTO = tasks.stream()
			.map(t -> new TaskDTO(t))
			.collect(Collectors.toList());
		return ResponseEntity.ok().body(tasksDTO);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<TaskDTO> findById(@PathVariable Long id) {
		Task task = service.findById(id);
		TaskDTO taskDTO = new TaskDTO(task);
		return ResponseEntity.ok().body(taskDTO);
	}
	
	@PostMapping
	public ResponseEntity<TaskDTO> create(@RequestBody Task task) {
		task = service.create(task);
		TaskDTO taskDTO = new TaskDTO(task);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(task.getId()).toUri();
		return ResponseEntity.created(uri).body(taskDTO);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	// Não está funcionando
	@PutMapping(value = "/{id}")
	public ResponseEntity<TaskDTO> update(@PathVariable Long id, @RequestBody Task task) {
		task = service.update(id, task);
		TaskDTO taskDTO = new TaskDTO(task);
		return ResponseEntity.ok().body(taskDTO);
	}
}
