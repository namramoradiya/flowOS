package com.flowos.dto.response;

import com.flowos.common.enums.TokenStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record QueueTokenSummary(
        UUID tokenId,
        int tokenNumber,
        String customerName,
        String customerPhone,
        TokenStatus status,
        LocalDateTime joinedAt
) {}