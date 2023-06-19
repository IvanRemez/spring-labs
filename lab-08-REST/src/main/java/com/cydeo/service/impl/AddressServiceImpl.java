package com.cydeo.service.impl;

import com.cydeo.dto.AddressDTO;
import com.cydeo.dto.CustomerDTO;
import com.cydeo.entity.Address;
import com.cydeo.entity.Customer;
import com.cydeo.exception.NotFoundException;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.AddressRepository;
import com.cydeo.repository.CustomerRepository;
import com.cydeo.service.AddressService;
import com.cydeo.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final MapperUtil mapperUtil;
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;

    public AddressServiceImpl(AddressRepository addressRepository, MapperUtil mapperUtil, CustomerService customerService, CustomerRepository customerRepository) {
        this.addressRepository = addressRepository;
        this.mapperUtil = mapperUtil;
        this.customerService = customerService;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<AddressDTO> getAddressList() {

        List<Address> addressList = addressRepository.findAll();

        return addressList.stream().map(address -> mapperUtil.convert(
                address, new AddressDTO())).collect(Collectors.toList());
    }

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {

        Address address = addressRepository.save(mapperUtil.convert(addressDTO, new Address()));

        return mapperUtil.convert(address, new AddressDTO());
    }

    @Override
    public AddressDTO updateAddress(AddressDTO addressDTO) {

        Address addressToUpdate = addressRepository.findById(addressDTO.getId()).orElseThrow(
                () -> new NotFoundException("Address not found"));

        validateRelatedFieldsExist(addressDTO);

        Address updatedAddress = addressRepository.save(
                mapperUtil.convert(addressDTO, new Address()));

        return mapperUtil.convert(updatedAddress, new AddressDTO());
    }

    @Override
    public List<AddressDTO> findAllByStreetStartingWith(String prefix) {

        List<Address> addressList = addressRepository.findAllByStreetStartingWith(prefix);

        return addressList.stream().map(address -> mapperUtil.convert(
                address, new AddressDTO())).collect(Collectors.toList());
    }

    @Override
    public List<AddressDTO> getAddressListByCustomerId(Long id) {

        List<Address> addressList = addressRepository.retrieveByCustomerId(id);

        return addressList.stream().map(address -> mapperUtil.convert(
                address, new AddressDTO())).collect(Collectors.toList());
    }

    @Override
    public List<AddressDTO> findAllByCustomerAndName(Long customerId, String name) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        List<Address> addressList = addressRepository.findAllByCustomerAndName(customer, name);

        return addressList.stream().map(address -> mapperUtil.convert(
                address, new AddressDTO())).collect(Collectors.toList());
    }

    private void validateRelatedFieldsExist(AddressDTO addressDTO) {

        if (!customerService.existById(addressDTO.getId())) {

            throw new NotFoundException("Address not found");
        }
    }
}
