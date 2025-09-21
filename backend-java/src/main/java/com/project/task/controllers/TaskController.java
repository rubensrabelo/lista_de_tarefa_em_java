package com.project.task.controllers;

import com.project.task.dto.task.TaskCreateDTO;
import com.project.task.dto.task.TaskResponseDTO;
import com.project.task.dto.task.TaskUpdateDTO;
import com.project.task.services.TaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/tasks")
@SecurityRequirement(name = "bearer-key")
public class TaskController {
	
	private final TaskService service;

	public TaskController(TaskService service) {
		this.service = service;
	}

	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Page<TaskResponseDTO>> findAllTasksByUserId(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestHeader("Authorization") String authorizationHeader
	) {
		var sortDirection = direction.equalsIgnoreCase("asc") ? Direction.ASC : Direction.DESC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "createdAt"));
		Page<TaskResponseDTO> dtosRespose = service.findAll(pageable, authorizationHeader);
		return ResponseEntity.ok().body(dtosRespose);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<TaskResponseDTO> findTaskByIdAndUserId(
			@PathVariable Long id,
			@RequestHeader("Authorization") String authorizationHeader
	) {
		TaskResponseDTO dtoResponse = service.findById(id, authorizationHeader);
		return ResponseEntity.ok().body(dtoResponse);
	}

	@PostMapping
	public ResponseEntity<TaskResponseDTO> create(
			@RequestBody TaskCreateDTO dtoCreate,
			@RequestHeader("Authorization") String authorizationHeader
	) {
		TaskResponseDTO dtoResponse = service.create(dtoCreate, authorizationHeader);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dtoResponse.getId()).toUri();
		return ResponseEntity.created(uri).body(dtoResponse);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<TaskResponseDTO> update(
			@PathVariable Long id,
			@RequestBody TaskUpdateDTO dtoUpdate,
			@RequestHeader("Authorization") String authorizationHeader
	) {
		TaskResponseDTO dtoResponse = service.update(id, dtoUpdate, authorizationHeader);
		return ResponseEntity.ok().body(dtoResponse);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(
			@PathVariable Long id,
			@RequestHeader("Authorization") String authorizationHeader
	) {
		service.delete(id, authorizationHeader);
		return ResponseEntity.noContent().build();	
	}
}
