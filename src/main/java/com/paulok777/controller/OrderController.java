package com.paulok777.controller;

import com.paulok777.entity.Order;
import com.paulok777.entity.Product;
import com.paulok777.service.OrderService;
import com.paulok777.util.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@Log4j2
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

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
        log.info("(username: {}) create new order", SecurityContextHolder.getContext().getAuthentication().getName());
        long id = orderService.saveNewOrder().getId();
        log.info("(username: {}) new order id: {}", id,
                SecurityContextHolder.getContext().getAuthentication().getName());
        return id;
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
        log.info("(username: {}) get order by id: {}", id,
                SecurityContextHolder.getContext().getAuthentication().getName());
        Map<Product, Long> products = orderService.getProductsByOrderId(id);
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
        log.info("(username: {}) add product (code or name:{}) to order (id:{}) in amount: {}",
                productIdentifier, id, amount, SecurityContextHolder.getContext().getAuthentication().getName());
        Validator.validateAmountForCashier(amount);
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
        log.info("(username: {}) change product amount (id:{}) to order (id:{}) to: {}",
                productId, orderId, amount, SecurityContextHolder.getContext().getAuthentication().getName());
        Validator.validateAmountForCashier(amount);
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
        log.info("(username: {}) close order (id:{})", id,
                SecurityContextHolder.getContext().getAuthentication().getName());
        orderService.makeStatusClosed(id);
    }

    @PostMapping("/senior_cashier/orders/cancel/{id}")
    public String cancelOrder(@PathVariable String id) {
        log.info("(username: {}) cancel order (id:{})", id,
                SecurityContextHolder.getContext().getAuthentication().getName());
        orderService.cancelOrder(id);
        return "redirect:/senior_cashier/orders";
    }

    @PostMapping("/senior_cashier/orders/cancel/{orderId}/{productId}")
    public String cancelProduct(@PathVariable String orderId, @PathVariable String productId) {
        log.info("(username: {}) cancel product (id:{}) in order (id:{})",
                productId, orderId, SecurityContextHolder.getContext().getAuthentication().getName());
        orderService.cancelProduct(orderId, productId);
        return "redirect:/senior_cashier/orders/" + orderId;
    }
}
