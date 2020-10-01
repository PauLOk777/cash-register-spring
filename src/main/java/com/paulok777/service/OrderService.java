package com.paulok777.service;

import com.paulok777.entity.*;
import com.paulok777.repository.OrderProductsRepository;
import com.paulok777.repository.OrderRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;
    private final OrderProductsRepository orderProductsRepository;

    public OrderService(OrderRepository orderRepository, UserService userService, ProductService productService, OrderProductsRepository orderProductsRepository) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.productService = productService;
        this.orderProductsRepository = orderProductsRepository;
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public Order saveNewOrder() {
        User user = userService.getCurrentUser();
        return orderRepository.save(new Order(user));
    }

    @Transactional
    public List<Product> getProductsByOrderId(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {

             Set<OrderProducts> orderProducts = order.get().getOrderProducts();
             List<Product> products = new ArrayList<>();
             for (OrderProducts orderProduct: orderProducts) {
                products.add(orderProduct.getProduct());
             }
             return products;
        }
        throw new NoSuchElementException("No order with this id");
    }

    @Transactional
    public void addProductToOrderByCodeOrName(String orderId, String productIdentifier, Long amount) {
        Optional<Order> order = orderRepository.findById(Long.valueOf(orderId));
        Product product = productService.findByIdentifier(productIdentifier);
        Order currOrder = order.orElseThrow();

        OrderProducts orderProducts = new OrderProducts();

        Long totalPrice = currOrder.getTotalPrice();
        currOrder.setTotalPrice(totalPrice + product.getPrice() * amount);

        Long productAmount = product.getAmount();
        product.setAmount(productAmount - amount);

        orderRepository.save(currOrder);
        productService.saveProduct(product);
        orderProductsRepository.save(orderProducts);
    }
}
