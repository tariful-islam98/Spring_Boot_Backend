package com.practice.springboot.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private int userId;
    @NotEmpty(message = "User Name cannot be null or empty!")
    private String name;
    @NotEmpty(message = "Email cannot be null or empty!")
    @Email(message = "Email address is not valid")
    private String email;
    @NotEmpty(message = "Password cannot be null or empty!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$!%*?&])[A-Za-z\\d@#$!%*?&]{8,}$", message = "Password should be at least 8 characters with at 1 lower case, 1 upper case, 1 digit, and 1 special character")
    private String password;
}
