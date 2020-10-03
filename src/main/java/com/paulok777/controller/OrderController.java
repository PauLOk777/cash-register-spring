package com.paulok777.controller;

import com.paulok777.entity.Order;
import com.paulok777.entity.Product;
import com.paulok777.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/senior_cashier/orders")
    public String getOrdersSeniorCashier(Model model) {
        getOrders(model);
        return "ordersSeniorCashier";
    }

    @GetMapping("/cashier/orders")
    public String getOrdersCashier(Model model) {
        getOrders(model);
        return "ordersCashier";
    }

    private void getOrders(Model model) {
        List<Order> orders = orderService.getOrders();
        model.addAttribute("orders", orders);
    }


    @PostMapping("/senior_cashier/orders")
    public String createNewOrderSeniorCashier() {
        return "redirect:/senior_cashier/orders/" + createNewOrder();
    }

    @PostMapping("/cashier/orders")
    public String createNewOrderCashier() {
        return "redirect:/cashier/orders/" + createNewOrder();
    }

    private Long createNewOrder() {
        return orderService.saveNewOrder().getId();
    }

    @GetMapping("/senior_cashier/orders/{id}")
    public String getOrderByIdSeniorCashier(@PathVariable String id, Model model) {
        getOrderById(id, model);
        return "orderProductsSeniorCashier";
    }

    @GetMapping("/cashier/orders/{id}")
    public String getOrderByIdCashier(@PathVariable String id, Model model) {
        getOrderById(id, model);
        return "orderProductsCashier";
    }

    public void getOrderById(String id, Model model) {
        Map<Long, Product> products = orderService.getProductsByOrderId(Long.valueOf(id));
        model.addAttribute("orderId", id);
        model.addAttribute("products", products);
    }

    @PostMapping("/senior_cashier/orders/{id}")
    public String addProductSeniorCashier(@PathVariable String id, @RequestParam String productIdentifier,
                             @RequestParam Long amount) {
        addProduct(id, productIdentifier, amount);
        return "redirect:/senior_cashier/orders/" + id;
    }

    @PostMapping("/cashier/orders/{id}")
    public String addProductCashier(@PathVariable String id, @RequestParam String productIdentifier,
                             @RequestParam Long amount) {
        addProduct(id, productIdentifier, amount);
        return "redirect:/cashier/orders/" + id;
    }

    private void addProduct(String id, String productIdentifier, Long amount) {
        orderService.addProductToOrderByCodeOrName(id, productIdentifier, amount);
    }

    @PostMapping("/senior_cashier/orders/{orderId}/{productId}")
    public String changeAmountOfProductSeniorCashier(@PathVariable String orderId, @PathVariable String productId,
                                        @RequestParam Long amount) {
        changeAmountOfProduct(orderId, productId, amount);
        return "redirect:/senior_cashier/orders/" + orderId;
    }

    @PostMapping("/cashier/orders/{orderId}/{productId}")
    public String changeAmountOfProductCashier(@PathVariable String orderId, @PathVariable String productId,
                                        @RequestParam Long amount) {
        changeAmountOfProduct(orderId, productId, amount);
        return "redirect:/cashier/orders/" + orderId;
    }

    public void changeAmountOfProduct(String orderId, String productId, Long amount) {
        orderService.changeAmountOfProduct(orderId, productId, amount);
    }

    @PostMapping("/senior_cashier/orders/close/{id}")
    public String closeOrderSeniorCashier(@PathVariable String id) {
        closeOrder(id);
        return "redirect:/senior_cashier/orders";
    }

    @PostMapping("/cashier/orders/close/{id}")
    public String closeOrderCashier(@PathVariable String id) {
        closeOrder(id);
        return "redirect:/cashier/orders";
    }

    public void closeOrder(String id) {
        orderService.makeStatusClosed(id);
    }

    @PostMapping("/senior_cashier/orders/cancel/{id}")
    public String cancelOrder(@PathVariable String id) {
        orderService.cancelOrder(id);
        return "redirect:/senior_cashier/orders";
    }

    @PostMapping("/senior_cashier/orders/cancel/{orderId}/{productId}")
    public String cancelProduct(@PathVariable String orderId, @PathVariable String productId) {
        orderService.cancelProduct(orderId, productId);
        return "redirect:/senior_cashier/orders/" + orderId;
    }
}
