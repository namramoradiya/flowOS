package com.flowos.controller;

import com.flowos.dto.response.QueueTokenSummary;
import com.flowos.service.StaffQueueService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffQueueService staffQueueService;

    public StaffController(StaffQueueService staffQueueService) {
        this.staffQueueService = staffQueueService;
    }

    @GetMapping("/queue/{branchId}")
    public List<QueueTokenSummary> currentQueue(@PathVariable UUID branchId) {
        return staffQueueService.currentQueue(branchId);
    }

    @PostMapping("/queue/{branchId}/next")
    public QueueTokenSummary callNext(@PathVariable UUID branchId) {
        return staffQueueService.callNext(branchId);
    }

    @PostMapping("/queue/{tokenId}/skip")
    public QueueTokenSummary skip(@PathVariable UUID tokenId) {
        return staffQueueService.skip(tokenId);
    }

    @PostMapping("/queue/{tokenId}/recall")
    public QueueTokenSummary recall(@PathVariable UUID tokenId) {
        return staffQueueService.recall(tokenId);
    }

    @PostMapping("/queue/{tokenId}/complete")
    public QueueTokenSummary complete(@PathVariable UUID tokenId) {
        return staffQueueService.complete(tokenId);
    }
}