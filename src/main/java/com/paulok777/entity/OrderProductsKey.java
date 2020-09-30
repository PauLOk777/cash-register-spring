package com.paulok777.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class OrderProductsKey implements Serializable {
    @Column(name = "order_id")
    Long orderId;
    @Column(name = "product_id")
    Long productId;
}
