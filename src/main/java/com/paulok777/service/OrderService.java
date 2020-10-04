package com.paulok777.service;

import com.paulok777.dto.ReportDTO;
import com.paulok777.entity.*;
import com.paulok777.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;

    public List<Order> getOrders() {
        return orderRepository.findByStatusOrderByCreateDateDesc(OrderStatus.NEW);
    }

    public Order saveNewOrder() {
        Order order = Order.builder()
                .totalPrice(0L)
                .createDate(LocalDateTime.now())
                .status(OrderStatus.NEW)
                .user(userService.getCurrentUser())
                .orderProducts(new HashSet<>())
                .build();
        return orderRepository.save(order);
    }

    public Map<Long, Product> getProductsByOrderId(String id) {
        Order order = getOrderById(id);
        if (!order.getStatus().equals(OrderStatus.NEW)) {
            throw new IllegalArgumentException("Order was closed or canceled");
        }

         return order.getOrderProducts()
                .stream()
                .filter(orderProducts -> orderProducts.getAmount() > 0)
                .sorted(Comparator.comparing(op -> op.getProduct().getName()))
                .collect(Collectors.toMap(
                        OrderProducts::getAmount, OrderProducts::getProduct,
                        (x, y) -> y, LinkedHashMap::new
                        )
                );
    }

    @Transactional
    public void addProductToOrderByCodeOrName(String orderId, String productIdentifier, Long amount) {
        Order order = getOrderById(orderId);
        Product product = productService.findByIdentifier(productIdentifier);

        Set<OrderProducts> orderProductsSet = getOrderProductSet(order, product);
        OrderProducts orderProducts;

        if (orderProductsSet.size() > 0) {
            Optional<OrderProducts> orderProductsOptional = orderProductsSet.stream().findFirst();
            orderProducts = orderProductsOptional.orElseThrow();
            order.getOrderProducts().remove(orderProducts);
            product.getOrderProducts().remove(orderProducts);
            orderProducts.setAmount(amount + orderProducts.getAmount());
        } else {
            orderProducts = new OrderProducts();
            orderProducts.setOrder(order);
            orderProducts.setProduct(product);
            orderProducts.setAmount(amount);
        }

        order.setTotalPrice(order.getTotalPrice() + product.getPrice() * amount);
        product.setAmount(product.getAmount() - amount);

        order.getOrderProducts().add(orderProducts);
        product.getOrderProducts().add(orderProducts);

        orderRepository.save(order);
        productService.saveProduct(product);
    }

    @Transactional
    public void changeAmountOfProduct(String orderId, String productId, Long amount) {
        Order order = getOrderById(orderId);
        Product product = getProductById(productId);

        Set<OrderProducts> orderProductsSet = getOrderProductSet(order, product);

        Optional<OrderProducts> orderProductsOptional = orderProductsSet.stream().findFirst();
        OrderProducts orderProducts = orderProductsOptional.orElseThrow();
        if (orderProducts.getAmount() < 1) throw new IllegalArgumentException("Product was removed from order");

        long prevAmount = orderProducts.getAmount();

        if (prevAmount + product.getAmount() < amount) {
            throw new IllegalArgumentException("No so many products.");
        }

        order.getOrderProducts().remove(orderProducts);
        product.getOrderProducts().remove(orderProducts);
        orderProducts.setAmount(amount);

        long differenceInAmount = amount - prevAmount;
        long differenceIntPrice = differenceInAmount * product.getPrice();

        order.setTotalPrice(order.getTotalPrice() + differenceIntPrice);
        order.getOrderProducts().add(orderProducts);

        product.setAmount(product.getAmount() - differenceInAmount);
        product.getOrderProducts().add(orderProducts);

        orderRepository.save(order);
        productService.saveProduct(product);
    }

    public void makeStatusClosed(String id) {
        orderRepository.changeStatusToClosed(Long.valueOf(id), OrderStatus.CLOSED);
    }

    @Transactional
    public void cancelOrder(String id) {
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
        Order order = getOrderById(orderId);
        Product product = getProductById(productId);

        Set<OrderProducts> orderProductsSet = getOrderProductSet(order, product);

        Optional<OrderProducts> orderProductsOptional = orderProductsSet.stream().findFirst();
        OrderProducts orderProducts = orderProductsOptional.orElseThrow();

        order.getOrderProducts().remove(orderProducts);
        product.getOrderProducts().remove(orderProducts);

        long productPrice = product.getPrice();
        long amount = orderProducts.getAmount();

        order.setTotalPrice(order.getTotalPrice() - productPrice * amount);
        product.setAmount(product.getAmount() + amount);

        orderProducts.setAmount(0L);
        order.getOrderProducts().add(orderProducts);
        product.getOrderProducts().add(orderProducts);

        orderRepository.save(order);
        productService.saveProduct(product);
    }

    public Order getOrderById(String id) {
        Optional<Order> order = orderRepository.findById(Long.valueOf(id));
        return order.orElseThrow();
    }

    public Product getProductById(String id) {
        Optional<Product> product = productService.findById(Long.valueOf(id));
        return product.orElseThrow();
    }

    private Set<OrderProducts> getOrderProductSet(Order order, Product product) {
        return order
                .getOrderProducts()
                .stream()
                .filter(orderProduct -> product.getId().equals(orderProduct.getProduct().getId()))
                .collect(Collectors.toSet());
    }

    public ReportDTO makeXReport() {
        List<Order> orders = orderRepository.findByStatus(OrderStatus.CLOSED);
        return getReportInfo(orders);
    }

    public ReportDTO makeZReport() {
        List<Order> orders = orderRepository.findByStatus(OrderStatus.CLOSED);
        archiveOrders(orders);
        return getReportInfo(orders);
    }

    private ReportDTO getReportInfo(List<Order> orders) {
        return ReportDTO.builder()
                .amount((long) orders.size())
                .totalPrice(
                        orders.stream()
                        .map(Order::getTotalPrice)
                        .reduce(0L, Long::sum))
                .build();
    }

    @Transactional
    public void archiveOrders(List<Order> orders) {
        orders.forEach(order -> {
            order.setStatus(OrderStatus.ARCHIVED);
            orderRepository.save(order);
        });
    }
}
