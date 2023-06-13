package com.cydeo.service.impl;

import com.cydeo.repository.CartRepository;
import com.cydeo.service.CartService;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public boolean existById(Long cartId) {

        return cartRepository.existsById(cartId);
    }
}
