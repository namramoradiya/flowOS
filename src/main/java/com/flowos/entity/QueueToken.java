package com.flowos.entity;

import com.flowos.common.enums.TokenSource;
import com.flowos.common.enums.TokenStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "queue_token",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_branch_token_per_day",
                columnNames = {"branch_id", "token_number", "token_date"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueueToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "counter_id")
    private Counter counter;

    @Column(nullable = false)
    private int tokenNumber;

    // this is what makes the uniqueness constraint actually work -
    // see explanation below for why we need a real column here
    @Column(name = "token_date", nullable = false)
    @Builder.Default
    private LocalDate tokenDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TokenSource source;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TokenStatus status = TokenStatus.WAITING;

    @Builder.Default
    private LocalDateTime joinedAt = LocalDateTime.now();

    private LocalDateTime calledAt;

    private LocalDateTime completedAt;
}