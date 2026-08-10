package com.flowos.entity;

import com.flowos.common.enums.OrgType;
import com.flowos.common.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "organization")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization {
    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrgType type;

    @Column(nullable = false, length = 80)
    private String city;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SubscriptionStatus subscriptionStatus = SubscriptionStatus.TRIAL;

    private LocalDateTime trialEndsAt;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
