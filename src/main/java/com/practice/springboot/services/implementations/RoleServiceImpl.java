package com.practice.springboot.services.implementations;

import com.practice.springboot.entities.Role;
import com.practice.springboot.payloads.PrivilegeDto;
import com.practice.springboot.payloads.RoleDto;
import com.practice.springboot.payloads.UserDto;
import com.practice.springboot.repositories.RoleRepo;
import com.practice.springboot.services.interfaces.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepo roleRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public RoleServiceImpl(RoleRepo roleRepo, ModelMapper modelMapper) {
        this.roleRepo = roleRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public RoleDto createRole(RoleDto roleDto) {
        // Check if the role name already exists
        if (roleRepo.existsByName(roleDto.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role with name " + roleDto.getName() + " already exists");
        }

        Role role = modelMapper.map(roleDto, Role.class);
        Role savedRole = roleRepo.save(role);
        return modelMapper.map(savedRole, RoleDto.class);
    }

    @Override
    public RoleDto updateRole( RoleDto roleDto, int roleId) {
        Role existingRole = roleRepo.findById(roleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found with id: " + roleId));

        // Check if the updated role name already exists
        if (!existingRole.getName().equals(roleDto.getName()) && roleRepo.existsByName(roleDto.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role with name " + roleDto.getName() + " already exists");
        }

        existingRole.setName(roleDto.getName());
        Role updatedRole = roleRepo.save(existingRole);
        return modelMapper.map(updatedRole, RoleDto.class);
    }

    @Override
    public void deleteRole(int roleId) {
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found with id: " + roleId));

        roleRepo.delete(role);
    }

    @Override
    public RoleDto getRoleById(int roleId) {
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found with id: " + roleId));

        return modelMapper.map(role, RoleDto.class);
    }

    @Override
    public List<RoleDto> getAllRoles() {
        List<Role> roles = roleRepo.findAll();
        return roles.stream()
                .map(role -> modelMapper.map(role, RoleDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getUsersForRole(int roleId) {
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found with id: " + roleId));

        return role.getUsers().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Set<PrivilegeDto> getPrivilegesByRole(String roleName) {
        Role role = roleRepo.findByName(roleName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found with name: " + roleName));

        return role.getPrivileges().stream()
                .map(privilege -> modelMapper.map(privilege, PrivilegeDto.class))
                .collect(Collectors.toSet());
    }
}
