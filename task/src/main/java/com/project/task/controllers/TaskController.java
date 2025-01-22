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

import com.project.task.dto.task.TaskDTO;
import com.project.task.services.TaskService;

@RestController
@RequestMapping(value = "/tasks")
public class TaskController {
	
	@Autowired
	private TaskService service;
	
	@GetMapping
	public ResponseEntity<List<TaskDTO>> findAllTasksByUserId(@RequestHeader("Authorization") String authorizationHeader) {
		List<TaskDTO> dtos = service.findAllTasksByUserId(authorizationHeader);
		return ResponseEntity.ok().body(dtos);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<TaskDTO> findTaskByIdAndUserId(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
		TaskDTO dto = service.findTaskByIdAndUserId(id, authorizationHeader);
		return ResponseEntity.ok().body(dto);
	}

	@PostMapping
	public ResponseEntity<TaskDTO> create(@RequestBody TaskDTO dto, @RequestHeader("Authorization") String authorizationHeader) {
		dto = service.create(dto, authorizationHeader);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.id()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<TaskDTO> update(@PathVariable Long id, @RequestBody TaskDTO dto, @RequestHeader("Authorization") String authorizationHeader) {
		dto = service.update(id, dto, authorizationHeader);
		return ResponseEntity.ok().body(dto);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
		service.delete(id, authorizationHeader);
		return ResponseEntity.noContent().build();	
	}
}
