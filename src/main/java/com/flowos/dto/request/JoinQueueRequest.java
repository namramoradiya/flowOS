package com.flowos.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record JoinQueueRequest(
        @NotNull UUID branchId,
        @NotNull String customerName,
        @Pattern(regexp = "^[0-9]{10}$", message = "phone must be a 10 digit number")
        String customerPhone
) {}