package com.paulok777.repositories;

import com.paulok777.entities.Order;
import com.paulok777.entities.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Transactional
    @Modifying
    @Query("update orders o set o.status = :status where o.id = :id")
    void changeStatusToClosed(@Param(value = "id") Long id, @Param(value = "status") OrderStatus status);

    List<Order> findByStatusOrderByCreateDateDesc(@Param(value = "status") OrderStatus status);

    List<Order> findByStatus(OrderStatus status);
}
