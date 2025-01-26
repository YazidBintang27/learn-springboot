package com.training.restapi.contact_management.service;

import com.training.restapi.contact_management.entity.User;
import com.training.restapi.contact_management.model.RegisterUserRequest;
import com.training.restapi.contact_management.model.UpdateUserRequest;
import com.training.restapi.contact_management.model.UserResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public void register(RegisterUserRequest registerUserRequest);
    public UserResponse getUser(User user);
    public UserResponse updateUser(User user, UpdateUserRequest updateUserRequest);
}
