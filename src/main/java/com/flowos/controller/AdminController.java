package com.flowos.controller;

import com.flowos.dto.response.DashboardTodayResponse;
import com.flowos.service.AdminDashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminDashboardService adminDashboardService;

    public AdminController(AdminDashboardService adminDashboardService){
        this.adminDashboardService=adminDashboardService;
    }

    @GetMapping("/dashboard/{branchId}/today")
    public DashboardTodayResponse today(@PathVariable UUID branchId){
        return adminDashboardService.today(branchId);
    }
}
