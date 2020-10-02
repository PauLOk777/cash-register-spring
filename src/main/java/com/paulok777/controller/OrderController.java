package com.paulok777.controller;

import com.paulok777.entity.Order;
import com.paulok777.entity.Product;
import com.paulok777.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String getOrders(Model model) {
        List<Order> orders = orderService.getOrders();
        model.addAttribute("orders", orders);
        return "ordersCashier";
    }

    @PostMapping
    public String createNewOrder() {
        Order order = orderService.saveNewOrder();
        return "redirect:/orders/" + order.getId();
    }

    @GetMapping("/{id}")
    public String getOrderById(@PathVariable String id, Model model) {
        Map<Long, Product> products = orderService.getProductsByOrderId(Long.valueOf(id));
        model.addAttribute("orderId", id);
        model.addAttribute("products", products);
        return "orderProductsCashier";
    }

    @PostMapping("/{id}")
    public String addProduct(@PathVariable String id, @RequestParam String productIdentifier,
                             @RequestParam Long amount, Model model) {
        orderService.addProductToOrderByCodeOrName(id, productIdentifier, amount);
        return "redirect:/orders/" + id;
    }

    @PostMapping("/{orderId}/{productId}")
    public String changeAmountOfProduct(@PathVariable String orderId, @PathVariable String productId,
                                        @RequestParam Long amount) {
        orderService.changeAmountOfProduct(orderId, productId, amount);
        return "redirect:/orders/" + orderId;
    }

    @PostMapping("/close/{id}")
    public String closeOrder() {
        return "ordersCashier";
    }

    @PostMapping("/cancel/{id}")
    public String cancelOrder() {
        return "ordersCashier";
    }

    @PostMapping("/cancel/{order_id}/{product_id}")
    public String cancelProduct() {
        return "orders/{id}";
    }
}
