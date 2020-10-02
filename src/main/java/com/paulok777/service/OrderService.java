package com.paulok777.service;

import com.paulok777.entity.*;
import com.paulok777.repository.OrderRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, UserService userService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.productService = productService;
    }

    public List<Order> getOrders() {
        return orderRepository.findByStatus(OrderStatus.NEW);
    }

    public Order saveNewOrder() {
        User user = userService.getCurrentUser();
        return orderRepository.save(new Order(user));
    }

    @Transactional
    public Map<Long, Product> getProductsByOrderId(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        Order currOrder = order.orElseThrow();
        if (!currOrder.getStatus().equals(OrderStatus.NEW)) {
            throw new IllegalArgumentException("Order was closed or canceled");
        }
        Set<OrderProducts> orderProducts = currOrder.getOrderProducts();
        Map<Long, Product> products = new HashMap<>();
        for (OrderProducts orderProduct: orderProducts) {
            products.put(orderProduct.getAmount(), orderProduct.getProduct());
        }
        return products;
    }

    @Transactional
    public void addProductToOrderByCodeOrName(String orderId, String productIdentifier, Long amount) {
        Optional<Order> order = orderRepository.findById(Long.valueOf(orderId));
        Product product = productService.findByIdentifier(productIdentifier);
        Order currOrder = order.orElseThrow();

        OrderProducts orderProducts = new OrderProducts();
        orderProducts.setOrder(currOrder);
        orderProducts.setProduct(product);
        orderProducts.setAmount(amount);

        long totalPrice = currOrder.getTotalPrice();
        currOrder.setTotalPrice(totalPrice + product.getPrice() * amount);
        currOrder.getOrderProducts().add(orderProducts);

        long productAmount = product.getAmount();
        product.setAmount(productAmount - amount);
        product.getOrderProducts().add(orderProducts);

        orderRepository.save(currOrder);
        productService.saveProduct(product);
    }

    @Transactional
    public void changeAmountOfProduct(String orderId, String productId, Long amount) {
        Optional<Order> order = orderRepository.findById(Long.valueOf(orderId));
        Optional<Product> product = productService.findById(Long.valueOf(productId));
        Order currOrder = order.orElseThrow();
        Product currProduct = product.orElseThrow();

        Set<OrderProducts> orderProductsSet = currOrder
                .getOrderProducts()
                .stream()
                .filter(orderProduct -> Long.valueOf(productId).equals(orderProduct.getProduct().getId()))
                .collect(Collectors.toSet());

        OrderProducts orderProducts = new OrderProducts();
        long prevAmount = -1;
        for (OrderProducts orderProduct: orderProductsSet) {
            prevAmount = orderProduct.getAmount();
            orderProducts = orderProduct;
        }

        if (prevAmount == -1) {
            throw new RuntimeException("previousAmount == -1");
        }
        if (prevAmount + currProduct.getAmount() < amount) {
            throw new IllegalArgumentException("No so many products.");
        }

        currOrder.getOrderProducts().remove(orderProducts);
        currProduct.getOrderProducts().remove(orderProducts);
        orderProducts.setAmount(amount);

        long differenceInAmount = amount - prevAmount;
        long differenceIntPrice = differenceInAmount * currProduct.getPrice();
        currOrder.setTotalPrice(currOrder.getTotalPrice() + differenceIntPrice);
        currOrder.getOrderProducts().add(orderProducts);

        currProduct.setAmount(currProduct.getAmount() - differenceInAmount);
        currProduct.getOrderProducts().add(orderProducts);

        orderRepository.save(currOrder);
        productService.saveProduct(currProduct);
    }

    public void makeStatusClosed(String id) {
        orderRepository.changeStatusToClosed(Long.valueOf(id), OrderStatus.CLOSED);
    }

    @Transactional
    public void cancelOrder(String id) {
        // change status, add amount to all products
        Optional<Order> order = orderRepository.findById(Long.valueOf(id));
        Order currOrder = order.orElseThrow();

        for (OrderProducts orderProduct: currOrder.getOrderProducts()) {
            Product product = orderProduct.getProduct();
            product.setAmount(product.getAmount() + orderProduct.getAmount());
            productService.saveProduct(product);
        }

        currOrder.setStatus(OrderStatus.CANCELED);
        orderRepository.save(currOrder);
    }

    @Transactional
    public void cancelProduct(String orderId, String productId) {

    }
}
