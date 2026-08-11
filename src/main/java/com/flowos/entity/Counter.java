package com.flowos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "counter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Counter {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(nullable = false, length = 60)
    private String name;

    @Builder.Default
    private boolean isActive = true;
}