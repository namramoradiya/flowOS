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

    // was: findByBranchIdAndStatusOrderByJoinedAtAsc(branchId, status) - no date filter
    List<QueueToken> findByBranchIdAndTokenDateAndStatusOrderByJoinedAtAsc(
            UUID branchId, LocalDate tokenDate, TokenStatus status);

    @Query("""
        SELECT COALESCE(MAX(q.tokenNumber), 0) FROM QueueToken q
        WHERE q.branch.id = :branchId AND q.tokenDate = :date
    """)
    Integer findMaxTokenNumberForBranchAndDate(@Param("branchId") UUID branchId, @Param("date") LocalDate date);

    // now scoped to a specific day, not "any day, ever"
    @Query("""
        SELECT COUNT(q) FROM QueueToken q
        WHERE q.branch.id = :branchId AND q.tokenDate = :date
        AND q.status = 'WAITING' AND q.joinedAt < :joinedAt
    """)
    long countWaitingAhead(
            @Param("branchId") UUID branchId,
            @Param("date") LocalDate date,
            @Param("joinedAt") LocalDateTime joinedAt);

    // was: findFirstByBranchIdAndStatusOrderByJoinedAtAsc(branchId, status) - no date filter
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<QueueToken> findFirstByBranchIdAndTokenDateAndStatusOrderByJoinedAtAsc(
            UUID branchId, LocalDate tokenDate, TokenStatus status);
}