package com.training.restapi.contact_management.service;

import com.training.restapi.contact_management.entity.User;
import com.training.restapi.contact_management.model.RegisterUserRequest;
import com.training.restapi.contact_management.model.UpdateUserRequest;
import com.training.restapi.contact_management.model.UserResponse;
import com.training.restapi.contact_management.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    @Override
    public void register(RegisterUserRequest registerUserRequest) {
        validationService.validate(registerUserRequest);

        if (userRepository.existsById(registerUserRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered");
        }

        User user = new User();
        user.setUsername(registerUserRequest.getUsername());
        user.setPassword(BCrypt.hashpw(registerUserRequest.getPassword(), BCrypt.gensalt()));
        user.setName(registerUserRequest.getName());

        userRepository.save(user);
    }

    @Override
    public UserResponse getUser(User user) {
        return UserResponse.builder().username(user.getUsername()).name(user.getName()).build();
    }

    @Transactional
    @Override
    public UserResponse updateUser(User user, UpdateUserRequest updateUserRequest) {
        validationService.validate(updateUserRequest);
        if (Objects.nonNull(updateUserRequest.getName())) {
            user.setName(updateUserRequest.getName());
        }

        if (Objects.nonNull(updateUserRequest.getPassword())) {
            user.setPassword(updateUserRequest.getPassword());
        }
        userRepository.save(user);
        return UserResponse.builder().name(user.getName()).username(user.getUsername()).build();
    }

}
