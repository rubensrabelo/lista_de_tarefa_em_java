package com.project.task.dto.auth;

import com.project.task.dto.user.UserResponseDTO;

public record LoginResponseDTO(UserResponseDTO userResponse, String token) {
}
