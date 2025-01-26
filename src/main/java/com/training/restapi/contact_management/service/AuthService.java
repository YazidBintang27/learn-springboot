package com.training.restapi.contact_management.service;

import com.training.restapi.contact_management.entity.User;
import com.training.restapi.contact_management.model.LoginUserRequest;
import com.training.restapi.contact_management.model.TokenResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    public TokenResponse login(LoginUserRequest loginUserRequest);
    public void logout(User user);
}
