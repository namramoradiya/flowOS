package com.flowos.controller;

import com.flowos.dto.request.JoinQueueRequest;
import com.flowos.dto.response.QueueStatusResponse;
import com.flowos.service.QueueService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/queue")
public class QueueController {

    private final QueueService queueService;

    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    @PostMapping("/join")
    public ResponseEntity<QueueStatusResponse> join(@Valid @RequestBody JoinQueueRequest request) {
        return ResponseEntity.ok(queueService.joinQueue(request));
    }

    @GetMapping("/{tokenId}/status")
    public ResponseEntity<QueueStatusResponse> status(@PathVariable UUID tokenId) {
        return ResponseEntity.ok(queueService.getStatus(tokenId));
    }

    @PostMapping("/{tokenId}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable UUID tokenId) {
        queueService.cancel(tokenId);
        return ResponseEntity.noContent().build();
    }
}