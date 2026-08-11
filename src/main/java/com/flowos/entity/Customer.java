package com.flowos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 120)
    private String name;

    @Column(nullable = false, unique = true, length = 15)
    private String phone;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}