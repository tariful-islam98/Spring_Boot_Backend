package com.practice.springboot.services.interfaces;

import com.practice.springboot.payloads.PrivilegeDto;
import com.practice.springboot.payloads.RoleDto;
import com.practice.springboot.payloads.UserDto;

import java.util.List;
import java.util.Set;

public interface RoleService {
    RoleDto createRole(RoleDto roleDto);

    RoleDto updateRole(RoleDto roleDto, int roleId);

    void deleteRole(int roleId);

    RoleDto getRoleById(int roleId);

    List<RoleDto> getAllRoles();

    List<UserDto> getUsersForRole(int roleId);

    Set<PrivilegeDto> getPrivilegesByRole(String roleName);
}
