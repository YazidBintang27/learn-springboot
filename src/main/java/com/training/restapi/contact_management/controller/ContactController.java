package com.training.restapi.contact_management.controller;

import com.training.restapi.contact_management.entity.User;
import com.training.restapi.contact_management.model.*;
import com.training.restapi.contact_management.service.ContactServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ContactController {

    @Autowired
    private ContactServiceImpl contactService;

    @PostMapping(
            path = "/api/contacts",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> createContact(User user, @RequestBody ContactRequest contactRequest) {
        ContactResponse contactResponse = contactService.createContact(user, contactRequest);
        return WebResponse.<ContactResponse>builder().data(contactResponse).build();
    }

    @GetMapping(
            path = "/api/contacts/{contactId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> getContact(User user, @PathVariable("contactId") String contactId) {
        ContactResponse contactResponse = contactService.getContact(user, contactId);
        return WebResponse.<ContactResponse>builder().data(contactResponse).build();
    }

    @PutMapping(
            path = "api/contacts/{contactId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> updateContact(
            User user,
            @RequestBody UpdateContactRequest contactRequest,
            @PathVariable("contactId") String contactId
    ) {
        contactRequest.setId(contactId);
        ContactResponse contactResponse = contactService.updateContact(user, contactRequest);
        return WebResponse.<ContactResponse>builder().data(contactResponse).build();
    }

    @DeleteMapping(
            path = "api/contacts/{contactId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> deleteContact(User user, @PathVariable("contactID") String contactId) {
        contactService.deleteContact(user, contactId);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
            path = "api/contacts",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<ContactResponse>> searchContact(
            User user,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "phone", required = false) String phone,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ) {
        SearchContactRequest contactRequest = SearchContactRequest.builder()
                .email(email)
                .name(name)
                .phone(phone)
                .page(page)
                .size(size).build();
        Page<ContactResponse> contactResponses = contactService.searchContact(user, contactRequest);

        return WebResponse.<List<ContactResponse>>builder()
                .data(contactResponses.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(contactResponses.getNumber())
                        .totalPage(contactResponses.getTotalPages())
                        .size(contactResponses.getSize()).build())
                .build();
    }
}
