package com.practice.springboot.services.interfaces;

import com.practice.springboot.payloads.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto user);

    UserDto updateUser(UserDto user, Integer userId);

    UserDto getUserById(Integer userId);

    List<UserDto> getAllUser();

    void deleteUser(Integer userId);

    UserDto getUserByEmail(String email);
}
