package com.training.restapi.contact_management.service;

import com.training.restapi.contact_management.entity.Contact;
import com.training.restapi.contact_management.entity.User;
import com.training.restapi.contact_management.model.ContactRequest;
import com.training.restapi.contact_management.model.ContactResponse;
import com.training.restapi.contact_management.model.SearchContactRequest;
import com.training.restapi.contact_management.model.UpdateContactRequest;
import com.training.restapi.contact_management.repository.ContactRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    @Override
    public ContactResponse createContact(User user, ContactRequest contactRequest) {
        validationService.validate(contactRequest);
        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName(contactRequest.getFirstName());
        contact.setLastName(contactRequest.getLastName());
        contact.setEmail(contactRequest.getEmail());
        contact.setPhone(contactRequest.getPhone());
        contact.setUser(user);

        return toContactResponse(contact);
    }

    private ContactResponse toContactResponse(Contact contact) {
        return ContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public ContactResponse getContact(User user, String id) {
        Contact contact = contactRepository.findFirstByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        return toContactResponse(contact);
    }

    @Transactional
    @Override
    public ContactResponse updateContact(User user, UpdateContactRequest updateContactRequest) {
        validationService.validate(updateContactRequest);
        Contact contact = contactRepository.findFirstByUserAndId(user, updateContactRequest.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        contact.setFirstName(updateContactRequest.getFirstName());
        contact.setLastName(updateContactRequest.getLastName());
        contact.setEmail(updateContactRequest.getEmail());
        contact.setPhone(updateContactRequest.getPhone());

        contactRepository.save(contact);
        return toContactResponse(contact);
    }

    @Transactional
    @Override
    public void deleteContact(User user, String id) {
        Contact contact = contactRepository.findFirstByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        contactRepository.delete(contact);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ContactResponse> searchContact(User user, SearchContactRequest contactRequest) {
        Specification<Contact> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("user"), user));
            if (Objects.nonNull(contactRequest.getName())) {
                predicates.add(builder.or(
                        builder.like(root.get("firstName"), "%" + contactRequest.getName() + "%"),
                        builder.like(root.get("lastName"), "%" + contactRequest.getName() + "%")
                ));
            }
            if (Objects.nonNull(contactRequest.getEmail())) {
                predicates.add(builder.like(root.get("email"), "%" + contactRequest.getEmail() + "%"));
            }
            if (Objects.nonNull(contactRequest.getPhone())) {
                predicates.add(builder.like(root.get("phone"), "%" + contactRequest.getPhone() + "%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(contactRequest.getPage(), contactRequest.getSize());
        Page<Contact> contacts = contactRepository.findAll(specification, pageable);
        List<ContactResponse> contactResponses = contacts.getContent().stream()
                .map(this::toContactResponse)
                .toList();

        return new PageImpl<>(contactResponses, pageable, contacts.getTotalElements());
    }
}
