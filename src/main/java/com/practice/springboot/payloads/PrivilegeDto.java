package com.practice.springboot.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class PrivilegeDto {
    private Long id;
    private String name;
    private Set<RoleDto> roles;
}
