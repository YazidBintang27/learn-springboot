package com.training.restapi.contact_management.controller;

import com.training.restapi.contact_management.entity.User;
import com.training.restapi.contact_management.model.RegisterUserRequest;
import com.training.restapi.contact_management.model.UpdateUserRequest;
import com.training.restapi.contact_management.model.UserResponse;
import com.training.restapi.contact_management.model.WebResponse;
import com.training.restapi.contact_management.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping(
            path = "/api/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> register(@RequestBody RegisterUserRequest registerUserRequest) {
        userService.register(registerUserRequest);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
            path = "/api/users/current",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> getUser(User user) {
        UserResponse userResponse = userService.getUser(user);
        return WebResponse.<UserResponse>builder().data(userResponse).build();
    }

    @PatchMapping(
            path = "/api/users/current",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> updateUser(User user, @RequestBody UpdateUserRequest updateUserRequest) {
        UserResponse userResponse = userService.updateUser(user, updateUserRequest);
        return WebResponse.<UserResponse>builder().data(userResponse).build();
    }
}
