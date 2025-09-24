package com.project.task.controllers.exceptions.handler;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.project.task.controllers.exceptions.StandardError;
import com.project.task.services.exceptions.DatabaseException;
import com.project.task.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
		String error = "Resource not found";
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError(
				Instant.now(),
				status.value(),
				error,
				e.getMessage(),
				request.getRequestURI()
		);
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> database(DatabaseException e, HttpServletRequest request) {
		String error = "Database error";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(
				Instant.now(),
				status.value(),
				error,
				e.getMessage(),
				request.getRequestURI()
		);
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<StandardError> usernameNotFound(UsernameNotFoundException e, HttpServletRequest request) {
		String error = "Authentication error";
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		StandardError err = new StandardError(
				Instant.now(),
				status.value(),
				error,
				e.getMessage(),
				request.getRequestURI()
		);
		return ResponseEntity.status(status).body(err);
	}
}
