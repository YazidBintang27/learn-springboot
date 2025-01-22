package com.training.restapi.contact_management.service;

import com.training.restapi.contact_management.model.RegisterUserRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public void register(RegisterUserRequest registerUserRequest);
}
