package com.app.team3demobanking.dto;

import com.app.team3demobanking.model.Role;

public record UserResponse(
        Long id,
        String username,
        Role role
) {
}
