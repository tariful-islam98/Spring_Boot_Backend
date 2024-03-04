package com.practice.springboot.payloads;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class RoleDto {
    private int roleId;
    @NotEmpty(message = "Name cannot be null or empty!")
    private String name;
    private Set<UserDto> users;
    private Set<PrivilegeDto> privileges;
}
