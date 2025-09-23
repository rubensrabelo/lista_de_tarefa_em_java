package com.project.task.services;

import com.project.task.dto.user.UserCreateDTO;
import com.project.task.dto.user.UserResponseDTO;
import com.project.task.infra.security.TokenService;
import com.project.task.models.User;
import com.project.task.repositories.UserRepository;
import com.project.task.services.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserService(
            UserRepository userRepository,
            TokenService tokenService,
            PasswordEncoder passwordEncoder,
            ModelMapper modelMapper
    ) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public User getUserByToken(String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        var login = this.tokenService.validateToken(token);
        return userRepository.findByEmail(login)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public UserResponseDTO register(@RequestBody UserCreateDTO dtoRegister) {
        User user = userRepository.findByEmail(dtoRegister.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(dtoRegister.getPassword()));
        newUser.setEmail(dtoRegister.getEmail());
        newUser.setFirstname(dtoRegister.getFirstname());
        newUser.setLastname(dtoRegister.getLastname());
        userRepository.save(newUser);

        return modelMapper.map(newUser, UserResponseDTO.class);
    }
}
