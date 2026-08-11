package com.flowos.repository;

import com.flowos.entity.Counter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CounterRepository extends JpaRepository<Counter, UUID> {
    List<Counter> findByBranchIdAndIsActiveTrue(UUID branchId);
}