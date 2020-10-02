package com.paulok777.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long totalPrice;
    private LocalDateTime createDate = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderProducts> orderProducts;

    public Order(User user) {
        totalPrice = 0L;
        status = OrderStatus.NEW;
        this.user = user;
        orderProducts = new HashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                Objects.equals(totalPrice, order.totalPrice) &&
                Objects.equals(createDate, order.createDate) &&
                status == order.status &&
                Objects.equals(user, order.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, totalPrice, createDate, status, user);
    }

}
