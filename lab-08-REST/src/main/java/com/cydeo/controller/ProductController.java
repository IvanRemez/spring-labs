package com.cydeo.controller;

import com.cydeo.dto.ProductDTO;
import com.cydeo.dto.ProductRequest;
import com.cydeo.model.ResponseWrapper;
import com.cydeo.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getProductList() {

        return ResponseEntity.ok(new ResponseWrapper("Products are successfully retrieved",
                productService.retrieveProductList(), HttpStatus.OK));
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper> updateProduct(@RequestBody ProductDTO productDTO) {

        return ResponseEntity.ok(new ResponseWrapper("Product is updated",
                productService.updateProduct(productDTO), HttpStatus.OK));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createProduct(@RequestBody ProductDTO productDTO) {

        return ResponseEntity.ok(new ResponseWrapper("Product is created",
                productService.createProduct(productDTO), HttpStatus.OK));
    }

    @PostMapping("/categoryandprice")
    public ResponseEntity<ResponseWrapper> retrieveProductByCategoryAndPrice(
            @RequestBody ProductRequest productRequest) {

        return ResponseEntity.ok(new ResponseWrapper("Products are successfully retrieved",
                productService.retrieveAllProductsByCategoryAndPrice(productRequest.getCategoryList(),
                        productRequest.getPrice()), HttpStatus.OK));
    }

    @GetMapping("/{name}")
    public ResponseEntity<ResponseWrapper> getProductListByName(
            @PathVariable("name") String name) {

        return ResponseEntity.ok(new ResponseWrapper("Product successfully retrieved",
                productService.retrieveProductByName(name), HttpStatus.OK));
    }

    @GetMapping("/top3")
    public ResponseEntity<ResponseWrapper> getTop3ProductList() {

        return ResponseEntity.ok(new ResponseWrapper("Products are successfully retrieved",
                productService.findTop3ByOrderByPriceDesc(), HttpStatus.OK));
    }

    @GetMapping("/price/{price}")
    public ResponseEntity<ResponseWrapper> countProductsByPrice(
            @PathVariable("price") BigDecimal price) {

        return ResponseEntity.ok(new ResponseWrapper("Products are successfully retrieved",
                productService.countProductByPriceGreaterThan(price), HttpStatus.OK));
    }

    @GetMapping("/price/{price}/quantity/{quantity}")
    public ResponseEntity<ResponseWrapper> getProductsByPriceAndQuantity(
            @PathVariable("price") BigDecimal price,
            @PathVariable("quantity") Integer quantity) {

        return ResponseEntity.ok(new ResponseWrapper("Products are successfully retrieved",
                productService.getProductsByPriceAndQuantity(price, quantity), HttpStatus.OK));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<ResponseWrapper> getProductListByCategory(
            @PathVariable("id") Long id) {

        return ResponseEntity.ok(new ResponseWrapper("Products are successfully retrieved",
                productService.retrieveProductListByCategory(id), HttpStatus.OK));
    }

}
