package com.paulok777.controller;

import com.paulok777.entity.Order;
import com.paulok777.entity.OrderProducts;
import com.paulok777.entity.Product;
import com.paulok777.service.OrderService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public String getOrders(Model model) {
        List<Order> orders = orderService.getOrders();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @PostMapping
    public String createNewOrder() {
        Order order = orderService.saveNewOrder();
        return "redirect:/orders/products/" + order.getId();
    }

    @GetMapping("/products/{id}")
    public String getOrderById(@PathVariable String id, Model model) {
        List<Product> products = orderService.getProductsByOrderId(Long.valueOf(id));
        model.addAttribute("orderId", id);
        model.addAttribute("products", products);
        return "orderProducts";
    }

    @PostMapping("/products/{id}")
    public String addProduct(@PathVariable String id, @RequestParam String productIdentifier,
                             @RequestParam Long amount, Model model) {
        orderService.addProductToOrderByCodeOrName(id, productIdentifier, amount);
        return "redirect:/orders/products/" + id;
    }

    @PostMapping("/products/{orderId}/{productId}")
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
