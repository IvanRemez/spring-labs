
package com.cydeo.lab04springmvc.service;

import com.cydeo.lab04springmvc.model.Cart;
import com.cydeo.lab04springmvc.model.CartItem;

import java.util.List;
import java.util.UUID;

public interface CartService {
    List<Cart> retrieveCartList();

    List<CartItem> retrieveCartDetail(UUID cartId);

    void initialiseCartList();
}
