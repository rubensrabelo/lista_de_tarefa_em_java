package com.project.task.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.task.dto.user.LoginRequestDTO;
import com.project.task.dto.user.RegisterRequestDTO;
import com.project.task.dto.user.ResponseDTO;
import com.project.task.infra.security.TokenService;
import com.project.task.models.User;
import com.project.task.repositories.UserRepository;
import com.project.task.services.exceptions.ResourceNotFoundException;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
	
	private final UserRepository repository;
	private final PasswordEncoder passwordEnconder;
	private final TokenService tokenService;
	
	public AuthController(UserRepository repository, PasswordEncoder passwordEnconder, TokenService tokenService) {
		this.repository = repository;
		this.passwordEnconder = passwordEnconder;
		this.tokenService = tokenService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<ResponseDTO> login(@RequestBody LoginRequestDTO userLogin) {
		User user = repository.findByEmail(userLogin.email())
				.orElseThrow(() -> new ResourceNotFoundException(userLogin.email()));
		
		if(passwordEnconder.matches(userLogin.password(), user.getPassword())) {
			String token = this.tokenService.generateToken(user);
			ResponseDTO response = new ResponseDTO(user.getName(), token);
			return ResponseEntity.ok().body(response);
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@PostMapping("/register")
	public ResponseEntity<ResponseDTO> register(@RequestBody RegisterRequestDTO userRegister) {
		Optional<User> user = this.repository.findByEmail(userRegister.email());
		
		if(user.isEmpty()) {
			User newUser = new User();
			newUser.setPassword(passwordEnconder.encode(userRegister.password()));
			newUser.setEmail(userRegister.email());
			newUser.setName(userRegister.name());
			this.repository.save(newUser);
			String token = this.tokenService.generateToken(newUser);
			ResponseDTO response = new ResponseDTO(newUser.getName(), token);
			return ResponseEntity.ok().body(response);
		}
		
		return ResponseEntity.badRequest().build();
	}
}
