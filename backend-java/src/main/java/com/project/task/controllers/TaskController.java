package com.project.task.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.project.task.dto.task.TaskResponseDTO;
import com.project.task.services.TaskService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

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
		Page<TaskResponseDTO> dtos = service.findAll(pageable, authorizationHeader);
		return ResponseEntity.ok().body(dtos);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<TaskResponseDTO> findTaskByIdAndUserId(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
		TaskResponseDTO dto = service.findTaskByIdAndUserId(id, authorizationHeader);
		return ResponseEntity.ok().body(dto);
	}

	@PostMapping
	public ResponseEntity<TaskResponseDTO> create(@RequestBody TaskResponseDTO dto, @RequestHeader("Authorization") String authorizationHeader) {
		dto = service.create(dto, authorizationHeader);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.id()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<TaskResponseDTO> update(@PathVariable Long id, @RequestBody TaskResponseDTO dto, @RequestHeader("Authorization") String authorizationHeader) {
		dto = service.update(id, dto, authorizationHeader);
		return ResponseEntity.ok().body(dto);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
		service.delete(id, authorizationHeader);
		return ResponseEntity.noContent().build();	
	}
}
