package com.paulok777.dto;

import com.paulok777.entity.OrderProducts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    long totalPrice;
    String createDate;
    String status;
    long user;
    Set<OrderProducts> orderProducts;
}
