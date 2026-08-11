package com.flowos.controller;

import com.flowos.dto.request.LoginRequest;
import com.flowos.dto.response.LoginResponse;
import com.flowos.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/auth")
@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService=authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request){
        return authService.login(request);
    }

}
