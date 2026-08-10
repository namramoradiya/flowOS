package com.flowos.dto.response;

import com.flowos.common.enums.Role;

import java.util.UUID;

public record LoginResponse(
        String token,
        UUID userId,
        String name,
        Role role,
        UUID branchId
) {}
