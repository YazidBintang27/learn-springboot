package com.training.restapi.contact_management.service;

import com.training.restapi.contact_management.entity.User;
import com.training.restapi.contact_management.model.ContactRequest;
import com.training.restapi.contact_management.model.ContactResponse;
import com.training.restapi.contact_management.model.SearchContactRequest;
import com.training.restapi.contact_management.model.UpdateContactRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface ContactService {
    public ContactResponse createContact(User user, ContactRequest contactRequest);
    public ContactResponse getContact(User user, String id);
    public ContactResponse updateContact(User user, UpdateContactRequest updateContactRequest);
    public void deleteContact(User user, String id);
    public Page<ContactResponse> searchContact(User user, SearchContactRequest contactRequest);
}
