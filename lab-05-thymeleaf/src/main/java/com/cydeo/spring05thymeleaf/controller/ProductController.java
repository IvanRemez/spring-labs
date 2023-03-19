package com.cydeo.spring05thymeleaf.controller;

import com.cydeo.spring05thymeleaf.model.Product;
import com.cydeo.spring05thymeleaf.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
//@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    public String productList(Model model) {

// need list of products to decide if list should be shown or show "No Product Yet!"
        model.addAttribute("productList", productService.listProduct());

        return "/product/list";
    }

    // create-form endpoint
    // return create-product page
    //update html to get the info from browser (user input)
    // update controller and print info on the console
    // navigate back to list endpoint

    @GetMapping("/create-form")
    public String createForm(Model model) {

        model.addAttribute("product", new Product());


        return "/product/create-product";
    }

    @PostMapping("/create-product")
    public String createProduct(@ModelAttribute("product") Product product) {

        productService.productCreate(product);

        return "redirect:/list";
    }

}
