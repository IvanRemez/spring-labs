package com.cydeo.repository;

import com.cydeo.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    //Write a derived query to get discount by name
    //Write a derived query to get all discounts greater than discount amount
    //Write a derived query to get all discounts by specific discount type
    //Write a JPQL query to get all discounts amount between range of discount amount
}