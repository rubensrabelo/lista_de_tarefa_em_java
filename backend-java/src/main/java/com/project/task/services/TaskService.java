package com.project.task.services;

import com.project.task.dto.task.TaskCreateDTO;
import com.project.task.dto.task.TaskResponseDTO;
import com.project.task.dto.task.TaskUpdateDTO;
import com.project.task.infra.security.TokenService;
import com.project.task.models.Task;
import com.project.task.models.User;
import com.project.task.repositories.TaskRepository;
import com.project.task.repositories.UserRepository;
import com.project.task.services.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TaskService {
	
	private final TokenService tokenService;
	private final TaskRepository taskRepository;
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	public TaskService(
			TaskRepository taskRepository,
			UserRepository userRepository,
			TokenService tokenService,
			ModelMapper modelMapper
	) {
		this.taskRepository = taskRepository;
		this.userRepository = userRepository;
		this.tokenService = tokenService;
		this.modelMapper = modelMapper;
	}
	
	public Page<TaskResponseDTO> findAll(Pageable pageable, String authorizationHeader) {
		User user = getUserByToken(authorizationHeader);
		Page<Task> entities = taskRepository.findByUserId(user.getId(), pageable);

		return entities.map(
				entity -> modelMapper.map(entity, TaskResponseDTO.class)
		);
	}

	public TaskResponseDTO findById(Long id, String authorizationHeader) {
		User user = getUserByToken(authorizationHeader);
		Task entity = taskRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException(Task.class.getName(), id));
		return modelMapper.map(entity, TaskResponseDTO.class);
	}

	public TaskResponseDTO create(TaskCreateDTO dto, String authorizationHeader) {
		User user = getUserByToken(authorizationHeader);
		Task entity = modelMapper.map(dto, Task.class);
		entity.setUser(user);

		entity = taskRepository.save(entity);

		return modelMapper.map(entity, TaskResponseDTO.class);
	}

	public TaskResponseDTO update(Long id, TaskUpdateDTO dto, String authorizationHeader) {
		User user = getUserByToken(authorizationHeader);
		Task entity = taskRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException(Task.class.getName(), id));

		Task update = modelMapper.map(dto, Task.class);
		updateData(entity, update);

		taskRepository.save(entity);
		return modelMapper.map(entity, TaskResponseDTO.class);
	}

	public void delete(Long id, String authorizationHeader) {
		User user = getUserByToken(authorizationHeader);
		Task task = taskRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException(Task.class.getName(), id));
		task.setActive(false);
		taskRepository.save(task);
	}

	private User getUserByToken(String authorizationHeader) {
		String token = authorizationHeader.replace("Bearer ", "");
        var login = this.tokenService.validateToken(token);
        return userRepository.findByEmail(login)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
	}
	
	private void updateData(Task entity, Task update) {
		entity.setTitle(update.getTitle());
		entity.setCompleted(update.isCompleted());
	}
}
