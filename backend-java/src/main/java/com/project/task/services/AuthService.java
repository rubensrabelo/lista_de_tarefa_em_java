package com.project.task.services;

import com.project.task.dto.auth.LoginRequestDTO;
import com.project.task.dto.auth.LoginResponseDTO;
import com.project.task.dto.user.UserResponseDTO;
import com.project.task.infra.security.TokenService;
import com.project.task.models.User;
import com.project.task.repositories.UserRepository;
import com.project.task.services.exceptions.InvalidPasswordException;
import com.project.task.services.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final ModelMapper modelMapper;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            TokenService tokenService,
            ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.modelMapper = modelMapper;
    }

    public LoginResponseDTO login(LoginRequestDTO dtoRequest) {
        User user = userRepository.findByEmail(dtoRequest.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(passwordEncoder.matches(dtoRequest.password(), user.getPassword())) {
            String token = tokenService.generateToken(user);
            UserResponseDTO userDTO = modelMapper.map(user, UserResponseDTO.class);
            return new LoginResponseDTO(userDTO, token);
        }

        throw new InvalidPasswordException("Invalid password.");
    }
}
