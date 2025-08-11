package com.jsalva.gymsystem.dto;

import jakarta.persistence.Column;

public record UserDTO(
        Long id,
        String firstName,
        String lastName,
        String username,
        Boolean isActive
) {}
