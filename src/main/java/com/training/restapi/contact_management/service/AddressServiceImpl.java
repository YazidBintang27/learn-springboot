package com.training.restapi.contact_management.service;

import com.training.restapi.contact_management.entity.Address;
import com.training.restapi.contact_management.entity.Contact;
import com.training.restapi.contact_management.entity.User;
import com.training.restapi.contact_management.model.AddressRequest;
import com.training.restapi.contact_management.model.AddressResponse;
import com.training.restapi.contact_management.model.UpdateAddressRequest;
import com.training.restapi.contact_management.repository.AddressRepository;
import com.training.restapi.contact_management.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Transactional
    @Override
    public AddressResponse createAddress(User user, AddressRequest addressRequest) {
        validationService.validate(addressRequest);

        Contact contact = contactRepository.findFirstByUserAndId(user, addressRequest.getContactId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Address address = new Address();

        address.setId(UUID.randomUUID().toString());
        address.setContact(contact);
        address.setStreet(addressRequest.getStreet());
        address.setCity(addressRequest.getCity());
        address.setProvince(addressRequest.getProvince());
        address.setCountry(addressRequest.getCountry());
        address.setPostalCode(addressRequest.getPostalCode());

        addressRepository.save(address);

        return toAddressResponse(address);
    }

    @Transactional
    @Override
    public AddressResponse updateAddress(User user, UpdateAddressRequest updateAddressRequest) {
        Contact contact = contactRepository.findFirstByUserAndId(user, updateAddressRequest.getContactId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Address address = addressRepository.findFirstByContactAndId(contact, updateAddressRequest.getAddressId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));

        address.setStreet(updateAddressRequest.getStreet());
        address.setCity(updateAddressRequest.getCity());
        address.setProvince(updateAddressRequest.getProvince());
        address.setCountry(updateAddressRequest.getCountry());
        address.setPostalCode(updateAddressRequest.getPostalCode());

        addressRepository.save(address);

        return toAddressResponse(address);
    }

    @Transactional(readOnly = true)
    @Override
    public AddressResponse getAddress(User user, String contactId, String addressId) {
        Contact contact = contactRepository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Address address = addressRepository.findFirstByContactAndId(contact, addressId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));

        return toAddressResponse(address);
    }

    @Transactional
    @Override
    public void deleteAddress(User user, String contactId, String addressId) {
        Contact contact = contactRepository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Address address = addressRepository.findFirstByContactAndId(contact, addressId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));

        addressRepository.delete(address);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AddressResponse> getAllUser(User user, String contactId) {
        Contact contact = contactRepository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        List<Address> addresses = addressRepository.findAllByContact(contact);
        return addresses.stream().map(address -> toAddressResponse(address)).toList();
    }

    private AddressResponse toAddressResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .province(address.getProvince())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .build();
    }
}
