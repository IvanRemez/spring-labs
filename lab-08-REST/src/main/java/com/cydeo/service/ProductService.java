package com.cydeo.service;

import com.cydeo.dto.ProductDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    List<ProductDTO> retrieveProductList();

    ProductDTO updateProduct(ProductDTO productDTO);

    ProductDTO createProduct(ProductDTO productDTO);

    List<ProductDTO> retrieveAllProductsByCategoryAndPrice(List<Long> categoryList, BigDecimal price);

    ProductDTO retrieveProductByName(String name);

    List<ProductDTO> findTop3ByOrderByPriceDesc();

    Integer countProductByPriceGreaterThan(BigDecimal price);

    List<ProductDTO> retrieveProductListByCategory(Long id);

    List<ProductDTO> getProductsByPriceAndQuantity(BigDecimal price, Integer quantity);
}
