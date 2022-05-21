package com.paulok777.controllers;

import com.paulok777.entities.Product;
import com.paulok777.exceptions.CashRegisterException;
import com.paulok777.services.OrderService;
import com.paulok777.services.UserService;
import com.paulok777.utils.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@Log4j2
@RequiredArgsConstructor
public class OrderController {

    private static final String RESOURCE_DELIMITER = "/";
    private static final String ORDERS = "/orders";
    private static final String ID_PATH_VARIABLE = "{id}";
    private static final String ORDER_ID_PATH_VARIABLE = "{orderId}";
    private static final String PRODUCT_ID_PATH_VARIABLE = "{productId}";
    private static final String CANCEL = "/cancel";
    private static final String CLOSE = "/close";

    private static final String SENIOR_CASHIER = "/senior_cashier";
    private static final String SENIOR_CASHIER_ORDERS = "/senior_cashier/orders";
    private static final String SENIOR_CASHIER_ORDER_BY_ID = SENIOR_CASHIER_ORDERS + RESOURCE_DELIMITER
            + ID_PATH_VARIABLE;
    private static final String SENIOR_CASHIER_CANCEL_ORDER = SENIOR_CASHIER_ORDERS + CANCEL + RESOURCE_DELIMITER
            + ID_PATH_VARIABLE;
    private static final String SENIOR_CASHIER_CANCEL_PRODUCT_IN_ORDER = SENIOR_CASHIER_ORDERS + CANCEL +
            RESOURCE_DELIMITER + ORDER_ID_PATH_VARIABLE + RESOURCE_DELIMITER + PRODUCT_ID_PATH_VARIABLE;

    private static final String CASHIER = "/cashier";
    private static final String CASHIER_ORDERS = "/cashier/orders";
    private static final String CASHIER_ORDER_BY_ID = CASHIER_ORDERS + RESOURCE_DELIMITER + ID_PATH_VARIABLE;
    private static final String CASHIER_ORDER_PRODUCTS_BY_ID = CASHIER_ORDERS + RESOURCE_DELIMITER
            + ORDER_ID_PATH_VARIABLE + RESOURCE_DELIMITER + PRODUCT_ID_PATH_VARIABLE;
    private static final String CASHIER_CLOSE_ORDER = CASHIER_ORDERS + CLOSE + RESOURCE_DELIMITER + ID_PATH_VARIABLE;

    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String ORDERS_ATTRIBUTE = "orders";
    private static final String ORDER_ID_ATTRIBUTE = "orderId";
    private static final String PRODUCTS_ATTRIBUTE = "products";
    private static final String SENIOR_CASHIER_PAGE = "ordersSeniorCashier";
    private static final String SENIOR_CASHIER_PRODUCTS_PAGE = "orderProductsSeniorCashier";
    private static final String CASHIER_PAGE = "ordersCashier";
    private static final String CASHIER_PRODUCTS_PAGE = "orderProductsCashier";

    private final OrderService orderService;
    private final UserService userService;

    @GetMapping(SENIOR_CASHIER_ORDERS)
    public String getOrdersSeniorCashier(Model model) {
        model.addAttribute(ORDERS_ATTRIBUTE, orderService.getOrders());
        return SENIOR_CASHIER_PAGE;
    }

    @GetMapping(CASHIER_ORDERS)
    public String getOrdersCashier(Model model) {
        model.addAttribute(ORDERS_ATTRIBUTE, orderService.getOrders());
        return CASHIER_PAGE;
    }

    @PostMapping(CASHIER_ORDERS)
    public String createNewOrderCashier() {
        log.info("(username: {}) create new order", SecurityContextHolder.getContext().getAuthentication().getName());
        long id = orderService.saveNewOrder().getId();
        log.info("(username: {}) new order id: {}", id,
                SecurityContextHolder.getContext().getAuthentication().getName());
        return REDIRECT_PREFIX + getSpecificToRolePathPrefix() + ORDERS + RESOURCE_DELIMITER + id;
    }

    @GetMapping(SENIOR_CASHIER_ORDER_BY_ID)
    public String getOrderByIdSeniorCashier(@PathVariable String id, Model model) {
        try {
            getOrderById(id, model);
        } catch (CashRegisterException e) {
            e.setRedirectUrl(REDIRECT_PREFIX + SENIOR_CASHIER_ORDERS);
            throw e;
        }
        return SENIOR_CASHIER_PRODUCTS_PAGE;
    }

    @GetMapping(CASHIER_ORDER_BY_ID)
    public String getOrderByIdCashier(@PathVariable String id, Model model) {
        try {
            getOrderById(id, model);
        } catch (CashRegisterException e) {
            e.setRedirectUrl(REDIRECT_PREFIX + CASHIER_ORDERS);
            throw e;
        }
        return CASHIER_PRODUCTS_PAGE;
    }

    private void getOrderById(String id, Model model) {
        log.info("(username: {}) get order by id: {}", id,
                SecurityContextHolder.getContext().getAuthentication().getName());
        Map<Product, Long> products = orderService.getProductsByOrderId(id);
        model.addAttribute(ORDER_ID_ATTRIBUTE, id);
        model.addAttribute(PRODUCTS_ATTRIBUTE, products);
    }

    @PostMapping(CASHIER_ORDER_BY_ID)
    public String addProductCashier(@PathVariable String id, @RequestParam String productIdentifier,
                             @RequestParam Long amount) {
        String redirectUrl = REDIRECT_PREFIX + getSpecificToRolePathPrefix() + ORDERS + RESOURCE_DELIMITER + id;
        log.info("(username: {}) add product (code or name:{}) to order (id:{}) in amount: {}",
                productIdentifier, id, amount, SecurityContextHolder.getContext().getAuthentication().getName());
        try {
            Validator.validateAmountForCashier(amount);
            orderService.addProductToOrderByCodeOrName(id, productIdentifier, amount);
        } catch (CashRegisterException e) {
            e.setRedirectUrl(redirectUrl);
            throw e;
        }
        return redirectUrl;
    }

    @PostMapping(CASHIER_ORDER_PRODUCTS_BY_ID)
    public String changeAmountOfProductCashier(@PathVariable String orderId, @PathVariable String productId,
                                        @RequestParam Long amount) {
        String redirectUrl = REDIRECT_PREFIX + getSpecificToRolePathPrefix() + ORDERS + RESOURCE_DELIMITER + orderId;
        log.info("(username: {}) change product amount (id:{}) to order (id:{}) to: {}",
                productId, orderId, amount, SecurityContextHolder.getContext().getAuthentication().getName());
        try {
            Validator.validateAmountForCashier(amount);
            orderService.changeAmountOfProduct(orderId, productId, amount);
        } catch (CashRegisterException e) {
            e.setRedirectUrl(redirectUrl);
            throw e;
        }
        return redirectUrl;
    }

    @PostMapping(CASHIER_CLOSE_ORDER)
    public String closeOrderCashier(@PathVariable String id) {
        String redirectUrl = REDIRECT_PREFIX + getSpecificToRolePathPrefix() + ORDERS;
        log.info("(username: {}) close order (id:{})", id,
                SecurityContextHolder.getContext().getAuthentication().getName());
        try {
            orderService.makeStatusClosed(id);
        } catch (CashRegisterException e) {
            e.setRedirectUrl(redirectUrl);
            throw e;
        }
        return redirectUrl;
    }

    @PostMapping(SENIOR_CASHIER_CANCEL_ORDER)
    public String cancelOrder(@PathVariable String id) {
        String redirectUrl = REDIRECT_PREFIX + SENIOR_CASHIER_ORDERS;
        log.info("(username: {}) cancel order (id:{})", id,
                SecurityContextHolder.getContext().getAuthentication().getName());
        try {
            orderService.cancelOrder(id);
        } catch (CashRegisterException e) {
            e.setRedirectUrl(redirectUrl);
            throw e;
        }
        return redirectUrl;
    }

    @PostMapping(SENIOR_CASHIER_CANCEL_PRODUCT_IN_ORDER)
    public String cancelProduct(@PathVariable String orderId, @PathVariable String productId) {
        String redirectUrl = REDIRECT_PREFIX + SENIOR_CASHIER_ORDERS + RESOURCE_DELIMITER + orderId;
        log.info("(username: {}) cancel product (id:{}) in order (id:{})",
                productId, orderId, SecurityContextHolder.getContext().getAuthentication().getName());
        try {
            orderService.cancelProduct(orderId, productId);
        } catch (CashRegisterException e) {
            e.setRedirectUrl(redirectUrl);
            throw e;
        }
        return redirectUrl;
    }

    private String getSpecificToRolePathPrefix() {
        if (userService.isUserSeniorCashier()) {
            return SENIOR_CASHIER;
        }
        return CASHIER;
    }
}
