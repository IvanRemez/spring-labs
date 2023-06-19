package com.cydeo.service;

import com.cydeo.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {
    boolean existById(Long customerId);

    List<CustomerDTO> getCustomerList();

    CustomerDTO createCustomer(CustomerDTO customerDTO);

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    CustomerDTO getCustomerByEmail(String email);
}
