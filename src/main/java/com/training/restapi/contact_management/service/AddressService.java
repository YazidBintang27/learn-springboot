package com.training.restapi.contact_management.service;

import com.training.restapi.contact_management.entity.User;
import com.training.restapi.contact_management.model.AddressRequest;
import com.training.restapi.contact_management.model.AddressResponse;
import com.training.restapi.contact_management.model.UpdateAddressRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AddressService {
    public AddressResponse createAddress(User user, AddressRequest addressRequest);
    public AddressResponse updateAddress(User user, UpdateAddressRequest updateAddressRequest);
    public AddressResponse getAddress(User user, String contactId, String addressId);
    public void deleteAddress(User user, String contactId, String addressId);
    public List<AddressResponse> getAllUser(User user, String contactId);
}
