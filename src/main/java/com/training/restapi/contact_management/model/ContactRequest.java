package com.training.restapi.contact_management.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactRequest {

    @NotBlank
    private String firstName;

    private String lastName;

    @Email
    private String email;

    private String phone;
}
