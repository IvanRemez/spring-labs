package com.cydeo.service;

import com.cydeo.dto.AddressDTO;

import java.util.List;

public interface AddressService {

    List<AddressDTO> getAddressList();

    AddressDTO createAddress(AddressDTO addressDTO);

    AddressDTO updateAddress(AddressDTO addressDTO);

    List<AddressDTO> findAllByStreetStartingWith(String prefix);

    List<AddressDTO> getAddressListByCustomerId(Long id);

    List<AddressDTO> findAllByCustomerAndName(Long id, String name);
}
