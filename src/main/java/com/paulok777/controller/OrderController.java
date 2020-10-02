package com.paulok777.controller;

import com.paulok777.entity.Order;
import com.paulok777.entity.Product;
import com.paulok777.entity.Role;
import com.paulok777.service.OrderService;
import com.paulok777.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping
    public String getOrders(Model model) {
        List<Order> orders = orderService.getOrders();
        model.addAttribute("orders", orders);
        Role role = userService.getCurrentUserRole();
        switch (role) {
            case CASHIER:
                return "ordersCashier";
            case SENIOR_CASHIER:
                return "ordersSeniorCashier";
            default:
                return "main";
        }
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
        Role role = userService.getCurrentUserRole();
        switch (role) {
            case CASHIER:
                return "orderProductsCashier";
            case SENIOR_CASHIER:
                return "orderProductsSeniorCashier";
            default:
                return "main";
        }
    }

    @PostMapping("/{id}")
    public String addProduct(@PathVariable String id, @RequestParam String productIdentifier,
                             @RequestParam Long amount) {
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
    public String closeOrder(@PathVariable String id) {
        orderService.makeStatusClosed(id);
        return "redirect:/orders";
    }

    @PostMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable String id) {
        orderService.cancelOrder(id);
        return "redirect:/orders";
    }

    @PostMapping("/cancel/{orderId}/{productId}")
    public String cancelProduct(@PathVariable String orderId, @PathVariable String productId) {
        orderService.cancelProduct(orderId, productId);
        return "redirect:/orders/" + orderId;
    }
}
