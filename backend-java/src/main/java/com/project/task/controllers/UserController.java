package com.project.task.controllers;

import com.project.task.dto.user.UserCreateDTO;
import com.project.task.dto.user.UserResponseDTO;
import com.project.task.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserCreateDTO dtoRegister) {
        UserResponseDTO dtoResponse = userService.register(dtoRegister);

        return ResponseEntity.ok().body(dtoResponse);
    }
}
