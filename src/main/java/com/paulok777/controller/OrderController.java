package com.paulok777.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @GetMapping
    public String getOrders() {
        return "orders";
    }

    @PostMapping
    public String createNewOrder() {
        return "/orders/products/{id}";
    }

    @GetMapping("/products/{id}")
    public String getOrderById() {
        return "orderProducts";
    }

    @PostMapping("/products")
    public String addProduct() {
        return "orderProducts";
    }

    @PostMapping("/products/{id}")
    public String changeAmountOfProduct() {
        return "orders/products/{id}";
    }

    @PostMapping("/close")
    public String closeOrder() {
        return "orders";
    }

    @PostMapping("/cancel")
    public String cancelOrder() {
        return "orders";
    }

    @PostMapping("/products/cancel")
    public String cancelProduct() {
        return "orders/products/{id}";
    }
}
