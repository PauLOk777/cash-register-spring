package com.paulok777.service;

import com.paulok777.dto.ReportDTO;
import com.paulok777.entity.*;
import com.paulok777.exception.orderExc.IllegalOrderStateException;
import com.paulok777.exception.InvalidIdException;
import com.paulok777.exception.orderExc.NoSuchProductException;
import com.paulok777.exception.productExc.NotEnoughProductsException;
import com.paulok777.repository.OrderRepository;
import com.paulok777.util.ExceptionKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;

    public List<Order> getOrders() {
        return orderRepository.findByStatusOrderByCreateDateDesc(OrderStatus.NEW);
    }

    public Order saveNewOrder() {
        return orderRepository.save(
                Order.builder()
                        .totalPrice(0L)
                        .createDate(LocalDateTime.now())
                        .status(OrderStatus.NEW)
                        .user(userService.getCurrentUser())
                        .orderProducts(new HashSet<>())
                        .build()
        );
    }

    public Map<Long, Product> getProductsByOrderId(String id) {
        Order order = getOrderById(id);
        if (!order.getStatus().equals(OrderStatus.NEW)) {
            log.warn("(username: {}) {}.",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    ExceptionKeys.ILLEGAL_ORDER_STATE);
            throw new IllegalOrderStateException(ExceptionKeys.ILLEGAL_ORDER_STATE);
        }

         return order.getOrderProducts()
                .stream()
                .filter(orderProducts -> orderProducts.getAmount() > 0)
                .sorted(Comparator.comparing(op -> op.getProduct().getName()))
                .collect(Collectors.toMap(
                        OrderProducts::getAmount, OrderProducts::getProduct,
                        (x, y) -> y, LinkedHashMap::new));
    }

    @Transactional
    public void addProductToOrderByCodeOrName(String orderId, String productIdentifier, Long amount) {
        Order order = getOrderById(orderId);
        Product product = productService.findByIdentifier(productIdentifier);

        if (product.getAmount() < amount) {
            log.warn("(username: {}) {}.",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    ExceptionKeys.NOT_ENOUGH_PRODUCTS);
            throw new NotEnoughProductsException(ExceptionKeys.NOT_ENOUGH_PRODUCTS);
        }

        Set<OrderProducts> orderProductsSet = getOrderProductSet(order, product);
        OrderProducts orderProducts;

        if (orderProductsSet.size() > 0) {
            orderProducts = parseOptionalAndThrowInvalidId(orderProductsSet.stream().findFirst());
            order.getOrderProducts().remove(orderProducts);
            product.getOrderProducts().remove(orderProducts);
            orderProducts.setAmount(amount + orderProducts.getAmount());
        } else {
            orderProducts = OrderProducts.builder()
                            .order(order)
                            .product(product)
                            .amount(amount)
                            .build();
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

        OrderProducts orderProducts = parseOptionalAndThrowInvalidId(
                getOrderProductSet(order, product).stream().findFirst());

        if (orderProducts.getAmount() < 1) {
            log.warn("(username: {}) {}.",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    ExceptionKeys.ILLEGAL_ORDER_STATE_PRODUCT_CANCELED);
            throw new NoSuchProductException(ExceptionKeys.ILLEGAL_ORDER_STATE_PRODUCT_CANCELED);
        }

        long prevAmount = orderProducts.getAmount();

        if (prevAmount + product.getAmount() < amount) {
            log.warn("(username: {}) {}.",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    ExceptionKeys.NOT_ENOUGH_PRODUCTS);
            throw new NotEnoughProductsException(ExceptionKeys.NOT_ENOUGH_PRODUCTS);
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
        try {
            orderRepository.changeStatusToClosed(Long.valueOf(id), OrderStatus.CLOSED);
        } catch (NumberFormatException e) {
            log.warn("(username: {}) {}}.",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    ExceptionKeys.INVALID_ID_EXCEPTION);
            throw new InvalidIdException(ExceptionKeys.INVALID_ID_EXCEPTION);
        }
    }

    @Transactional
    public void cancelOrder(String id) {
        Order order = getOrderById(id);

        order.getOrderProducts().forEach(orderProducts -> {
            Product product = orderProducts.getProduct();
            product.setAmount(product.getAmount() + orderProducts.getAmount());
            productService.saveProduct(product);
        });

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    @Transactional
    public void cancelProduct(String orderId, String productId) {
        Order order = getOrderById(orderId);
        Product product = getProductById(productId);

        OrderProducts orderProducts = parseOptionalAndThrowInvalidId(
                getOrderProductSet(order, product).stream().findFirst());

        order.getOrderProducts().remove(orderProducts);
        product.getOrderProducts().remove(orderProducts);

        order.setTotalPrice(order.getTotalPrice() - product.getPrice() * orderProducts.getAmount());
        product.setAmount(product.getAmount() + orderProducts.getAmount());
        orderProducts.setAmount(0L);

        order.getOrderProducts().add(orderProducts);
        product.getOrderProducts().add(orderProducts);

        orderRepository.save(order);
        productService.saveProduct(product);
    }

    public Order getOrderById(String id) {
        try {
            return parseOptionalAndThrowInvalidId(orderRepository.findById(Long.parseLong(id)));
        } catch (NumberFormatException e) {
            log.warn("(username: {}) {}}.",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    ExceptionKeys.INVALID_ID_EXCEPTION);
            throw new InvalidIdException("Invalid value for order id.");
        }
    }

    public Product getProductById(String id) {
        try {
            return parseOptionalAndThrowInvalidId(productService.findById(Long.parseLong(id)));
        } catch (NumberFormatException e) {
            log.warn("(username: {}) {}}.",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    ExceptionKeys.INVALID_ID_EXCEPTION);
            throw new InvalidIdException("Invalid value for order id.");
        }
    }

    private <T> T parseOptionalAndThrowInvalidId(Optional<T> optional) {
        return optional.orElseThrow(() -> {
            log.warn("(username: {}) {}}.",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    ExceptionKeys.INVALID_ID_EXCEPTION);
            throw new InvalidIdException("Invalid value for order id.");
        });
    }

    private Set<OrderProducts> getOrderProductSet(Order order, Product product) {
        return order
                .getOrderProducts()
                .stream()
                .filter(orderProduct -> product.getId().equals(orderProduct.getProduct().getId()))
                .collect(Collectors.toSet());
    }

    public ReportDTO makeXReport() {
        return getReportInfo(getClosedOrders());
    }

    public ReportDTO makeZReport() {
        List<Order> orders = getClosedOrders();
        archiveOrders(orders);
        return getReportInfo(orders);
    }

    private List<Order> getClosedOrders() {
        return orderRepository.findByStatus(OrderStatus.CLOSED);
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
