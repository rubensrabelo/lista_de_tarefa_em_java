package com.project.task.services.exceptions;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ResourceNotFoundException(String email) {
		super(email + " not found.");
	}
	
	public ResourceNotFoundException(String type, Long id) {
		super(type + " not with id=" + id + " found.");
	}

}
