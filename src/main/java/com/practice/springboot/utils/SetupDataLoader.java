package com.practice.springboot.utils;

import com.practice.springboot.payloads.PrivilegeDto;
import com.practice.springboot.payloads.RoleDto;
import com.practice.springboot.services.interfaces.PrivilegeService;
import com.practice.springboot.services.interfaces.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleService roleService; // Inject your RoleService here
    private final PrivilegeService privilegeService; // Inject your PrivilegeService here
    private boolean alreadySetup = false;

    @Autowired
    public SetupDataLoader(RoleService roleService, PrivilegeService privilegeService) {
        this.roleService = roleService;
        this.privilegeService = privilegeService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!alreadySetup) {
            // Create and save privileges
            PrivilegeDto readPrivilege = new PrivilegeDto();
            readPrivilege.setName("READ");
            privilegeService.createPrivilege(readPrivilege);

            PrivilegeDto writePrivilege = new PrivilegeDto();
            writePrivilege.setName("WRITE");
            privilegeService.createPrivilege(writePrivilege);

            // Create a set to hold the privileges
            Set<PrivilegeDto> privilegeSet = new HashSet<>();
            // Create a PrivilegeDto object using the readPrivilege and add it to the set
            PrivilegeDto readPrivilegeDto = new PrivilegeDto();
            readPrivilegeDto.setName(readPrivilege.getName());
            privilegeSet.add(readPrivilegeDto);

            // Create and save roles
            RoleDto userRole = new RoleDto();
            userRole.setName("USER");
            userRole.setPrivileges(privilegeSet);
            roleService.createRole(userRole);

            RoleDto adminRole = new RoleDto();
            adminRole.setName("ADMIN");

            readPrivilegeDto.setName(readPrivilege.getName());

            PrivilegeDto writePrivilegeDto = new PrivilegeDto();
            writePrivilegeDto.setName(writePrivilege.getName());

            // Set the privileges for the admin role
            adminRole.setPrivileges(Set.of(readPrivilegeDto, writePrivilegeDto));

            // Save the admin role
            roleService.createRole(adminRole);

            alreadySetup = true;
        }
    }
}
