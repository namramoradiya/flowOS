package com.flowos.service;

import com.flowos.common.enums.TokenSource;
import com.flowos.common.enums.TokenStatus;
import com.flowos.dto.request.JoinQueueRequest;
import com.flowos.dto.response.QueueStatusResponse;
import com.flowos.entity.Branch;
import com.flowos.entity.Customer;
import com.flowos.entity.QueueToken;
import com.flowos.exception.BadRequestException;
import com.flowos.exception.NotFoundException;
import com.flowos.repository.BranchRepository;
import com.flowos.repository.CounterRepository;
import com.flowos.repository.CustomerRepository;
import com.flowos.repository.QueueTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class QueueService {
    private static final int MINUTES_PER_PERSON_ESTIMATE=5;

    private final QueueTokenRepository queueTokenRepository;
    private final BranchRepository branchRepository;
    private final CounterRepository counterRepository;
    private final CustomerRepository customerRepository;

    public QueueService(QueueTokenRepository queueTokenRepository, BranchRepository branchRepository,CounterRepository counterRepository, CustomerRepository customerRepository){
        this.queueTokenRepository=queueTokenRepository;
        this.branchRepository=branchRepository;
        this.counterRepository=counterRepository;
        this.customerRepository=customerRepository;
    }

    @Transactional
    public QueueStatusResponse joinQueue(JoinQueueRequest request){
        Branch branch=branchRepository.findById(request.branchId()).orElseThrow(()-> new NotFoundException("Branch Not Found"));

        Customer customer=customerRepository.findByPhone(request.customerPhone()).orElseGet(
                () -> customerRepository.save(
                        Customer.builder()
                                .name(request.customerName())
                                .phone(request.customerPhone())
                                .build()
                ));
        LocalDate today= LocalDate.now();

        int nextTokenNumber = queueTokenRepository.findMaxTokenNumberForBranchAndDate(branch.getId(),today)+1;

        QueueToken token=queueTokenRepository.save(
                QueueToken.builder()
                        .branch(branch)
                        .customer(customer)
                        .tokenNumber(nextTokenNumber)
                        .tokenDate(today)
                        .source(TokenSource.WALK_IN)
                        .status(TokenStatus.WAITING)
                        .build()
        );
        return toStatusresponse(token);
    }

    @Transactional(readOnly=true)
    public QueueStatusResponse getStatus(UUID tokenId){
        QueueToken token=queueTokenRepository.findById(tokenId).orElseThrow(()-> new NotFoundException("Queue Not Found"));
        return toStatusresponse(token);
    }

    @Transactional
    public void cancel(UUID tokenId){
        QueueToken token = queueTokenRepository.findById(tokenId).orElseThrow(() -> new NotFoundException("Queue Token Not Found"));

        if(token.getStatus() != TokenStatus.WAITING){
            throw new BadRequestException("Only a Waiting token can be a cancelled");
        }

        token.setStatus(TokenStatus.CANCELLED);
        queueTokenRepository.save(token);
    }

    private QueueStatusResponse toStatusresponse(QueueToken token){
        long peopleAhead=token.getStatus() == TokenStatus.WAITING ?
                queueTokenRepository.countWaitingAhead(token.getBranch().getId(),token.getJoinedAt()) :0;

        int activeCounters=Math.max(counterRepository.findByBranchIdAndIsActiveTrue(token.getBranch().getId()).size(),1
        );

        int estimatedWait = (int) Math.ceil(
                (double) peopleAhead / activeCounters * MINUTES_PER_PERSON_ESTIMATE
        );

        return new QueueStatusResponse(
                token.getId(),
                token.getTokenNumber(),
                token.getStatus(),
                peopleAhead,
                estimatedWait
        );
    }
}
