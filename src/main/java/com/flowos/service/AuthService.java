package com.flowos.service;

import com.flowos.dto.request.LoginRequest;
import com.flowos.dto.response.LoginResponse;
import com.flowos.entity.AppUser;
import com.flowos.exception.UnauthorizedException;
import com.flowos.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(AppUserRepository appUserRepository,PasswordEncoder passwordEncoder,JwtService jwtService){
        this.appUserRepository=appUserRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtService=jwtService;
    }

    public LoginResponse login(LoginRequest request){
        AppUser user=appUserRepository.findByPhone(request.phone()).orElseThrow(()->new UnauthorizedException("Invalid Phone or Password"));
        if(!passwordEncoder.matches(request.password(), user.getPasswordHash())){
            throw new UnauthorizedException("Invalid Password");
        }
        String token= jwtService.generateToken(user.getId(),user.getRole().name(),user.getOrganization().getId());
        return new LoginResponse(
                token,
                user.getId(),
                user.getName(),
                user.getRole(),
                user.getBranch() !=null ? user.getBranch().getId() : null
        );
    }
}
