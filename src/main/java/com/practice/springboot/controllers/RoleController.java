package com.practice.springboot.controllers;

import com.practice.springboot.payloads.RoleDto;
import com.practice.springboot.payloads.UserDto;
import com.practice.springboot.services.interfaces.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;
    private final ModelMapper modelMapper;

    @Autowired
    public RoleController(RoleService roleService, ModelMapper modelMapper) {
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("")
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto roleDto) {
        RoleDto createdRole = roleService.createRole(modelMapper.map(roleDto, RoleDto.class));
        return new ResponseEntity<>(modelMapper.map(createdRole, RoleDto.class), HttpStatus.CREATED);
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable int roleId) {
        RoleDto role = roleService.getRoleById(roleId);
        return new ResponseEntity<>(modelMapper.map(role, RoleDto.class), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<RoleDto> roles = roleService.getAllRoles();
        return new ResponseEntity<>(roles.stream()
                .map(role -> modelMapper.map(role, RoleDto.class))
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @PatchMapping("/{roleId}")
    public ResponseEntity<RoleDto> updateRole(@RequestBody RoleDto roleDto, @PathVariable int roleId) {
        RoleDto updatedRole = roleService.updateRole(roleDto, roleId);
        return new ResponseEntity<>(modelMapper.map(updatedRole, RoleDto.class), HttpStatus.OK);
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable int roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{roleId}/users")
    public ResponseEntity<List<UserDto>> getUsersByRoleId(@PathVariable int roleId) {
        List<UserDto> users = roleService.getUsersForRole(roleId);
        return new ResponseEntity<>(users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList()), HttpStatus.OK);
    }
}
