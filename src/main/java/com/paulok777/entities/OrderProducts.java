package com.paulok777.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class OrderProducts {
    @EmbeddedId
    private OrderProductsKey id = new OrderProductsKey();

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    private Long amount;
}
