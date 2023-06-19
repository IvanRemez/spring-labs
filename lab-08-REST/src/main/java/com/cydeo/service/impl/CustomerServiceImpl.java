package com.cydeo.service.impl;

import com.cydeo.dto.CustomerDTO;
import com.cydeo.entity.Customer;
import com.cydeo.exception.NotFoundException;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.CustomerRepository;
import com.cydeo.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final MapperUtil mapperUtil;

    public CustomerServiceImpl(CustomerRepository customerRepository, MapperUtil mapperUtil) {
        this.customerRepository = customerRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public boolean existById(Long customerId) {

        return customerRepository.existsById(customerId);
    }

    @Override
    public List<CustomerDTO> getCustomerList() {

        return customerRepository.findAll().stream().map(customer -> mapperUtil.convert(
                customer, new CustomerDTO())).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {

        Customer customer = customerRepository.save(mapperUtil.convert(customerDTO, new Customer()));

        return mapperUtil.convert(customer, new CustomerDTO());
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {

        Customer customerToUpdate = customerRepository.findById(customerDTO.getId())
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        Customer updatedCustomer = customerRepository.save(
                mapperUtil.convert(customerDTO, new Customer()));

        return mapperUtil.convert(updatedCustomer, new CustomerDTO());
    }

    @Override
    public CustomerDTO getCustomerByEmail(String email) {

        Customer customer = customerRepository.retrieveByCustomerEmail(email);

        return mapperUtil.convert(customer, new CustomerDTO());
    }


}
