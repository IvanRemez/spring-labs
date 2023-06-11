package com.cydeo.repository;


import com.cydeo.entity.Balance;
import com.cydeo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {

    //Write a derived query to check balance exists for specific customer
    boolean existsByCustomer(Customer customer);
    boolean existsByCustomerId(Long id);

    //Write a derived query to get balance for specific customer
    Balance findByCustomer(Customer customer);
    Balance findByCustomerId(Long id);

    //Write a native query to get top 5 max balance
    @Query(value = "", nativeQuery = true)
    List<Balance> getTop5MaxBalance();

    //Write a derived query to get all balances greater than or equal specific balance amount
    //Write a native query to get all balances less than specific balance amount
}