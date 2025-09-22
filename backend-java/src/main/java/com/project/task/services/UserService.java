package com.project.task.services;

import com.project.task.infra.security.TokenService;
import com.project.task.models.User;
import com.project.task.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public UserService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public User getUserByToken(String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        var login = this.tokenService.validateToken(token);
        return userRepository.findByEmail(login)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}
