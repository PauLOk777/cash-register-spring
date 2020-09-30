package com.paulok777.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductController {
    @GetMapping
    public String getProducts() {
        return "products";
    }

    @PostMapping
    public String createProduct() {
        return "products";
    }

    @PostMapping("/{id}")
    public String changeAmountOfProduct() {
        return "products";
    }
}
