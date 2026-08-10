package com.flowos.config;

import com.flowos.common.enums.OrgType;
import com.flowos.common.enums.Role;
import com.flowos.entity.AppUser;
import com.flowos.entity.Branch;
import com.flowos.entity.Organization;
import com.flowos.repository.AppUserRepository;
import com.flowos.repository.BranchRepository;
import com.flowos.repository.OrganizationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class DataSeeder implements CommandLineRunner {
    private final OrganizationRepository organizationRepository;
    private final BranchRepository branchRepository;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(OrganizationRepository organizationRepository, BranchRepository branchRepository, AppUserRepository appUserRepository
    , PasswordEncoder passwordEncoder){
        this.organizationRepository=organizationRepository;
        this.appUserRepository=appUserRepository;
        this.branchRepository=branchRepository;
        this.passwordEncoder=passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if(organizationRepository.count()>0){
            return;
        }
        else{
            Organization org = organizationRepository.save(
                    Organization.builder()
                            .name("Sunrise Diagnostics")
                            .type(OrgType.LAB)
                            .city("Pune")
                            .build()
            );

            Branch branch = branchRepository.save(
                    Branch.builder()
                            .organization(org)
                            .name("Sunrise Diagnostics - Baner")
                            .address("Baner Road, Pune")
                            .build()
            );
            appUserRepository.save(
                    AppUser.builder()
                            .organization(org)
                            .branch(branch)
                            .name("Demo Owner")
                            .phone("9033385985")
                            .passwordHash(passwordEncoder.encode("password123"))
                            .role(Role.OWNER)
                            .build()
            );

            System.out.println("Seeded demo login -> phone: 9999999999 | password: password123");
            System.out.println("Branch ID: " + branch.getId());
        }
    }
}
