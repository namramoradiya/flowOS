package com.flowos.dto.response;

public record DashboardTodayResponse(
        long totalVisitors,
        long waiting,
        long completed,
        long cancelledOrSkipped,
        double averageWaitMinutes
) {}