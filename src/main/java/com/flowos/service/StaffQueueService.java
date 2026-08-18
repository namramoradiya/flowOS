package com.flowos.service;

import com.flowos.common.enums.TokenStatus;
import com.flowos.dto.response.QueueTokenSummary;
import com.flowos.entity.Counter;
import com.flowos.entity.QueueToken;
import com.flowos.exception.BadRequestException;
import com.flowos.exception.NotFoundException;
import com.flowos.repository.CounterRepository;
import com.flowos.repository.QueueTokenRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class StaffQueueService {
    private final QueueTokenRepository queueTokenRepository;
    private final CounterRepository counterRepository;

    public StaffQueueService(QueueTokenRepository queueTokenRepository,CounterRepository counterRepository){
        this.queueTokenRepository=queueTokenRepository;
        this.counterRepository=counterRepository;
    }

    @Transactional
    public QueueTokenSummary callNext(UUID branchId){
        Counter counter=counterRepository.findByBranchIdAndIsActiveTrue(branchId).stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No Active Customer Configured for this branch"));

        QueueToken token = queueTokenRepository
                .findFirstByBranchIdAndTokenDateAndStatusOrderByJoinedAtAsc(branchId,LocalDate.now(),TokenStatus.WAITING)
                .orElseThrow(() -> new NotFoundException("No customers waiting in this queue"));

        token.setStatus(TokenStatus.CALLED);
        token.setCounter(counter);
        token.setCalledAt(LocalDateTime.now());

        return toSummary(token);
    }

    @Transactional
    public QueueTokenSummary skip(UUID tokenId){
        QueueToken token=getTokenOrThrow(tokenId);

        if(token.getStatus() != TokenStatus.CALLED){
            throw new BadRequestException("Only Called Token Can be Skipped");
        }

        token.setStatus(TokenStatus.SKIPPED);
        queueTokenRepository.save(token);
        return  toSummary(token);
    }

    @Transactional
    public QueueTokenSummary recall(UUID tokenId){
        QueueToken token= getTokenOrThrow(tokenId);

        if(token.getStatus() != TokenStatus.SKIPPED){
            throw new BadRequestException("Only Skipped Token can be Recalled");
        }

        token.setStatus(TokenStatus.WAITING);
        queueTokenRepository.save(token);
        return toSummary(token);
    }

    @Transactional
    public QueueTokenSummary complete(UUID tokenId){
        QueueToken token = getTokenOrThrow(tokenId);

        if(token.getStatus() != TokenStatus.CALLED){
            throw new BadRequestException("Only Called Token can be Completed");
        }

        token.setStatus(TokenStatus.COMPLETED);
        queueTokenRepository.save(token);

        return toSummary(token);
    }

    @Transactional(readOnly = true)
    public List<QueueTokenSummary> currentQueue(UUID brancId){
        return queueTokenRepository.findByBranchIdAndTokenDateAndStatusOrderByJoinedAtAsc(brancId, LocalDate.now(),TokenStatus.WAITING).stream()
                .map(this::toSummary)
                .toList();
    }

    private QueueToken getTokenOrThrow(UUID tokenId){
        return queueTokenRepository.findById(tokenId).orElseThrow(() -> new NotFoundException("Token Not Found"));
    }

    private QueueTokenSummary toSummary(QueueToken token){
        return new QueueTokenSummary(
                token.getId(),
                token.getTokenNumber(),
                token.getCustomer().getName(),
                token.getCustomer().getPhone(),
                token.getStatus(),
                token.getJoinedAt()
        );
    }

}
