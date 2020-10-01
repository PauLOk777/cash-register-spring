package com.paulok777.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OrderProductsKey implements Serializable {
    @Column(name = "order_id")
    Long orderId;
    @Column(name = "product_id")
    Long productId;
}
