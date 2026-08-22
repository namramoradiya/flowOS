package com.flowos.service;

import com.flowos.common.enums.TokenStatus;
import com.flowos.dto.response.DashboardTodayResponse;
import com.flowos.entity.QueueToken;
import com.flowos.repository.QueueTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class AdminDashboardService {
    private final QueueTokenRepository queueTokenRepository;

    public AdminDashboardService(QueueTokenRepository queueTokenRepository){
        this.queueTokenRepository=queueTokenRepository;
    }

    @Transactional(readOnly = true)
    public DashboardTodayResponse today(UUID branchId){
        LocalDate today=LocalDate.now();
        List<QueueToken> tokens=queueTokenRepository.findByBranchIdAndTokenDate(branchId,today);
        long total=tokens.size();

        long waiting= tokens.stream()
                .filter(token-> token.getStatus()==(TokenStatus.WAITING))
                .count();

        long completed=tokens.stream()
                .filter(token->token.getStatus() == (TokenStatus.COMPLETED))
                .count();

        long cancelledOrSkipped=tokens.stream()
                .filter(token-> token.getStatus() == TokenStatus.CANCELLED || token.getStatus() == TokenStatus.SKIPPED)
                .count();

        double averageWaitMinutes= tokens.stream()
                .filter(token -> token.getStatus()==TokenStatus.COMPLETED && token.getJoinedAt() != null)
                .mapToLong(token-> Duration.between(token.getJoinedAt(),token.getCalledAt()).toMinutes())
                .average()
                .orElse(0.0);

        return new DashboardTodayResponse(total,waiting,completed,cancelledOrSkipped,averageWaitMinutes);
    }
}
