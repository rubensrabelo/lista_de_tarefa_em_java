package com.project.task.controllers;

import com.project.task.dto.auth.LoginResponseDTO;
import com.project.task.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.task.dto.auth.LoginRequestDTO;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
	
	private final AuthService authService;

	public AuthController(
			AuthService authService
	) {
		this.authService = authService;
	}
	
	@PostMapping
	public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO userLogin) {
		LoginResponseDTO response = authService.login(userLogin);
		return ResponseEntity.ok().body(response);
	}
}
