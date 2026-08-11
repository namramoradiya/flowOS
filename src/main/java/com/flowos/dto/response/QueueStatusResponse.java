package com.flowos.dto.response;

import com.flowos.common.enums.TokenStatus;

import java.util.UUID;

public record QueueStatusResponse(
        UUID tokenId,
        int tokenNumber,
        TokenStatus status,
        long peopleAhead,
        int estimatedWaitMinutes
) {}