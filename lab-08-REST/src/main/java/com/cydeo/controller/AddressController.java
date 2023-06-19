package com.cydeo.controller;

import com.cydeo.dto.AddressDTO;
import com.cydeo.model.ResponseWrapper;
import com.cydeo.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getAddressList() {

        return ResponseEntity.ok(new ResponseWrapper("Address are successfully retrieved",
                addressService.getAddressList(), HttpStatus.OK));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createAddress(@RequestBody AddressDTO addressDTO) {

        return ResponseEntity.ok(new ResponseWrapper("Address created",
                addressService.createAddress(addressDTO), HttpStatus.OK));
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper> updateAddress(@RequestBody AddressDTO addressDTO) {

        return ResponseEntity.ok(new ResponseWrapper("Address updated",
                addressService.updateAddress(addressDTO), HttpStatus.OK));
    }

    @GetMapping("/startsWith/{address}")
    public ResponseEntity<ResponseWrapper> getAddressListByStartsWithAddress(
            @PathVariable("address") String prefix) {

        return ResponseEntity.ok(new ResponseWrapper("Address are successfully retrieved",
                addressService.findAllByStreetStartingWith(prefix), HttpStatus.OK));
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<ResponseWrapper> getAddressListByCustomerId(
            @PathVariable("id") Long id) {

        return ResponseEntity.ok(new ResponseWrapper("Address are successfully retrieved",
                addressService.getAddressListByCustomerId(id), HttpStatus.OK));
    }
    @GetMapping("/customer/{customerId}/name/{name}")
    public ResponseEntity<ResponseWrapper> getAddressListByCustomerAndName(
            @PathVariable("customerId") Long id, @PathVariable("name") String name) {

        return ResponseEntity.ok(new ResponseWrapper("Address are successfully retrieved",
                addressService.findAllByCustomerAndName(id, name), HttpStatus.OK));
    }
}
