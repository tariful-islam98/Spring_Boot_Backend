package com.practice.springboot.services.implementations;

import com.practice.springboot.entities.User;
import com.practice.springboot.exceptions.NotNullViolationException;
import com.practice.springboot.payloads.UserDto;
import com.practice.springboot.repositories.UserRepo;
import com.practice.springboot.services.interfaces.UserServiceInterface;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServiceInterface {

    private UserRepo userRepo;
    private ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepo userRepo, ModelMapper modelMapper) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public UserDto createUser(@Valid UserDto userDto) {
        User userEntity = modelMapper.map(userDto, User.class);

        try {
            validateNotNullFields(userDto);

            User savedUser = userRepo.save(userEntity);

            return modelMapper.map(savedUser, UserDto.class);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email address already exists", e);
        } catch (NotNullViolationException e) {
            throw new ResponseStatusException(e.getHttpStatus(), e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating user", e);
        }

    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));

        if (userDto.getName() != null) {
            if (!userDto.getName().isBlank()) user.setName(userDto.getName());
        }

        user.setEmail(user.getEmail()); //keeping email field as non-updatable

        if (userDto.getPassword() != null) {
            if (!userDto.getPassword().isBlank()) user.setPassword(userDto.getPassword());
        }
        try {
            User updateUser = userRepo.save(user);
            return modelMapper.map(updateUser, UserDto.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Error updating user");
        }
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> getAllUser() {
        List<User> userList = userRepo.findAll();

        return userList.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));

        userRepo.delete(user);
    }

    private void validateNotNullFields(UserDto user) {
        if (user.getName() == null || user.getName().isBlank()) {
            throw new NotNullViolationException("Error creating user: name field is required and cannot be blank", HttpStatus.BAD_REQUEST);
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new NotNullViolationException("Error creating user: email field is required and cannot be blank", HttpStatus.BAD_REQUEST);
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new NotNullViolationException("Error creating user: password field is required and cannot be blank", HttpStatus.BAD_REQUEST);
        }
    }
}
