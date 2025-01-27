package com.training.restapi.contact_management.controller;

import com.training.restapi.contact_management.entity.User;
import com.training.restapi.contact_management.model.AddressRequest;
import com.training.restapi.contact_management.model.AddressResponse;
import com.training.restapi.contact_management.model.UpdateAddressRequest;
import com.training.restapi.contact_management.model.WebResponse;
import com.training.restapi.contact_management.service.AddressServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AddressController {

    @Autowired
    private AddressServiceImpl addressService;

    @PostMapping(
            path = "/api/contacts/{contactId}/addresses",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> createAddress(User user, @RequestBody AddressRequest addressRequest, @PathVariable("contactId") String contactId) {
        addressRequest.setContactId(contactId);
        AddressResponse addressResponse = addressService.createAddress(user, addressRequest);
        return WebResponse.<AddressResponse>builder().data(addressResponse).build();
    }

    @PutMapping(
            path = "/api/contacts/{contacId}/addresses/{addressId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> updateAddress(
            User user,
            @RequestBody UpdateAddressRequest updateAddressRequest,
            @PathVariable("contactId") String contactId,
            @PathVariable("addressId") String addressId
    ) {
        updateAddressRequest.setContactId(contactId);
        updateAddressRequest.setAddressId(addressId);
        AddressResponse addressResponse = addressService.updateAddress(user, updateAddressRequest);
        return WebResponse.<AddressResponse>builder().data(addressResponse).build();
    }

    @GetMapping(
            path = "/api/contacts/{contactId}/addresses/{addressId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> getAddress(User user, @PathVariable("contactId") String contactId, @PathVariable("addressId") String addressId) {
        AddressResponse addressResponse = addressService.getAddress(user, contactId, addressId);
        return WebResponse.<AddressResponse>builder().data(addressResponse).build();
    }

    @DeleteMapping(
            path = "/api/contacts/{contactId}/addresses/{addressId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> deleteAddress(User user, @PathVariable("contactId") String contactId, @PathVariable("addressId") String addressId) {
        addressService.deleteAddress(user, contactId, addressId);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
            path = "/api/contacts/{contactId}/addresses",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<AddressResponse>> getAllUser(User user, @PathVariable("contactId") String contactId) {
        List<AddressResponse> addressResponses = addressService.getAllUser(user, contactId);
        return WebResponse.<List<AddressResponse>>builder().data(addressResponses).build();
    }
}
