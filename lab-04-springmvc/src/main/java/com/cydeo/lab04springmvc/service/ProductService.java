package com.cydeo.lab04springmvc.service;

import com.cydeo.lab04springmvc.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> searchProduct(String name);

    void initialiseProductList();
}
