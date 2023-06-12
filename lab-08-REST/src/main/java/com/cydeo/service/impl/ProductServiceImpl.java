package com.cydeo.service.impl;

import com.cydeo.dto.ProductDTO;
import com.cydeo.entity.Product;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.ProductRepository;
import com.cydeo.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final MapperUtil mapperUtil;
    private final ProductRepository productRepository;

    public ProductServiceImpl(MapperUtil mapperUtil, ProductRepository productRepository) {
        this.mapperUtil = mapperUtil;
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDTO> retrieveProductList() {

        return productRepository.findAll().stream()
                .map(product -> mapperUtil.convert(product, new ProductDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO) {

        Product product = productRepository.save(mapperUtil
                .convert(productDTO, new Product()));

        return mapperUtil.convert(product, new ProductDTO());
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {

        Product product = productRepository.save(mapperUtil
                .convert(productDTO, new Product()));

        return mapperUtil.convert(product, new ProductDTO());
    }

    @Override
    public List<ProductDTO> retrieveAllProductsByCategoryAndPrice(List<Long> categoryList, BigDecimal price) {

        List<Product> productList = productRepository.retrieveProductListByCategory(categoryList, price);

        return productList.stream()
                .map(product -> mapperUtil.convert(product, new ProductDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO retrieveProductByName(String name) {

        return mapperUtil.convert(productRepository.findFirstByName(name), new ProductDTO());
    }

    @Override
    public List<ProductDTO> findTop3ByOrderByPriceDesc() {

        List<Product> top3 = productRepository.findTop3ByOrderByPriceDesc();

        return top3.stream().map(product -> mapperUtil.convert(product, new ProductDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public Integer countProductByPriceGreaterThan(BigDecimal price) {

        return productRepository.countProductByPriceGreaterThan(price);
    }

    @Override
    public List<ProductDTO> retrieveProductListByCategory(Long id) {

        return productRepository.retrieveProductListByCategory(id).stream()
                .map(product -> mapperUtil.convert(product, new ProductDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getProductsByPriceAndQuantity(BigDecimal price, Integer quantity) {

        return productRepository.retrieveProductListGreaterThanPriceAndLowerThanRemainingQuantity(price, quantity)
                .stream().map(product -> mapperUtil.convert(product, new ProductDTO()))
                .collect(Collectors.toList());
    }
}
