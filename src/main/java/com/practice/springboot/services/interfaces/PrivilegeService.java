package com.practice.springboot.services.interfaces;

import com.practice.springboot.payloads.PrivilegeDto;

import java.util.List;

public interface PrivilegeService {
    PrivilegeDto createPrivilege(PrivilegeDto privilegeDto);
    PrivilegeDto updatePrivilege(PrivilegeDto privilegeDto, Long privilegeId);
    PrivilegeDto getPrivilegeById(Long privilegeId);
    List<PrivilegeDto> getAllPrivileges();
    void deletePrivilege(Long privilegeId);
}
