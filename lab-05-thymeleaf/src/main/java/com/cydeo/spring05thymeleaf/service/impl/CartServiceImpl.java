package com.cydeo.spring05thymeleaf.service.impl;

import com.cydeo.spring05thymeleaf.model.Cart;
import com.cydeo.spring05thymeleaf.model.CartItem;
import com.cydeo.spring05thymeleaf.model.Product;
import com.cydeo.spring05thymeleaf.service.CartService;
import com.cydeo.spring05thymeleaf.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class CartServiceImpl implements CartService {

    public static Cart CART = new Cart(BigDecimal.ZERO,new ArrayList<>());

    private final ProductService productService;

    public CartServiceImpl(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public Cart addToCart(UUID productId, Integer quantity){

        //todo retrieve product from repository method - done
        Product product = productService.findProductById(productId);
        //todo initialise cart item - done
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        //todo add to cart - done
        CART.getCartItemList().add(cartItem);
        //todo calculate cart total amount - done
        CART.setCartTotalAmount(CART.getCartTotalAmount().add(cartItem.getTotalAmount()));

        return CART;
    }

    @Override
    public boolean deleteFromCart(UUID productId){
        //todo delete product object from cart using stream

//  find cartItem to delete using Product ID
        CartItem itemToDelete = CART.getCartItemList().stream()
                .filter(c -> c.getProduct().getId().equals(productId))
                .findAny().orElseThrow();
//  Total cart amount needs to be updated by subtracting the itemToDelete amount
        CART.setCartTotalAmount(CART.getCartTotalAmount().
                subtract(itemToDelete.getTotalAmount()));
//  remove itemToDelete from List<cartItem>
        CART.getCartItemList().remove(itemToDelete);

        return true;
    }
}
