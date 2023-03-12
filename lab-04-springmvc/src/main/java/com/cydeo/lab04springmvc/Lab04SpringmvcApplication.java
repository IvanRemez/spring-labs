package com.cydeo.lab04springmvc;

import com.cydeo.lab04springmvc.service.CartService;
import com.cydeo.lab04springmvc.service.ProductService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Lab04SpringmvcApplication {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(Lab04SpringmvcApplication.class, args);
        ProductService productService = context.getBean(ProductService.class);
        productService.initialiseProductList();

        CartService cartService = context.getBean(CartService.class);
        cartService.initialiseCartList();

    }

}
