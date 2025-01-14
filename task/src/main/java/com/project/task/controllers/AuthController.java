package com.project.task.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.task.infra.security.TokenService;
import com.project.task.repositories.UserRepository;

public class AuthController {
	
	private final UserRepository repository;
	private final PasswordEncoder passwordEnconder;
	private final TokenService tokenService;
	
	public AuthController(UserRepository repository, PasswordEncoder passwordEnconder, TokenService tokenService) {
		this.repository = repository;
		this.passwordEnconder = passwordEnconder;
		this.tokenService = tokenService;
	}
	
	public ResponseEntity<T> login() {
		return null;
	}
	
	public ResponseEntity<T> register() {
		return null;
	}
}
