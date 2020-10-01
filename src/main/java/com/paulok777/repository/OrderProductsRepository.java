package com.paulok777.repository;

import com.paulok777.entity.OrderProducts;
import com.paulok777.entity.OrderProductsKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductsRepository extends JpaRepository<OrderProducts, OrderProductsKey> {
}
