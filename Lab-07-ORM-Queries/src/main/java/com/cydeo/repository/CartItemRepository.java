package com.cydeo.repository;


import com.cydeo.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    //Write a derived query to get count cart items
    //Write a derived query to get cart items for specific cart state
    //Write a native query to get cart items for specific cart state and product name
    //Write a native query to get cart items for specific cart state and without discount
    //Write a native query to get cart items for specific cart state and with specific Discount type
}