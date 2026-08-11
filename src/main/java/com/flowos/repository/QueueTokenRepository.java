package com.flowos.repository;

import com.flowos.common.enums.TokenStatus;
import com.flowos.entity.QueueToken;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QueueTokenRepository extends JpaRepository<QueueToken, UUID> {

    // the live queue for a branch - oldest first, this IS what staff/customers see
    List<QueueToken> findByBranchIdAndStatusOrderByJoinedAtAsc(UUID branchId, TokenStatus status);

    // next token number to allocate - see explanation below
    @Query("""
        SELECT COALESCE(MAX(q.tokenNumber), 0) FROM QueueToken q
        WHERE q.branch.id = :branchId AND q.tokenDate = :date
    """)
    Integer findMaxTokenNumberForBranchAndDate(@Param("branchId") UUID branchId, @Param("date") LocalDate date);

    // how many WAITING people are ahead of a given joinedAt timestamp
    @Query("""
        SELECT COUNT(q) FROM QueueToken q
        WHERE q.branch.id = :branchId AND q.status = 'WAITING' AND q.joinedAt < :joinedAt
    """)
    long countWaitingAhead(@Param("branchId") UUID branchId, @Param("joinedAt") LocalDateTime joinedAt);

    // "Call Next" - locks the row so two staff can't call the same token at once
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<QueueToken> findFirstByBranchIdAndStatusOrderByJoinedAtAsc(UUID branchId, TokenStatus status);
}