package com.cydeo.repository;


import com.cydeo.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
    //Write a derived query to check balance exists for specific customer
    //Write a derived query to get balance for specific customer
    //Write a native query to get top 5 max balance
    //Write a derived query to get all balances greater than or equal specific balance amount
    //Write a native query to get all balances less than specific balance amount
}