package com.paulok777.repositories;

import com.paulok777.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Transactional
    @Modifying
    @Query("update products p set p.amount = :amount where p.id = :id")
    void updateAmountById(@Param(value = "amount") Long amount, @Param(value = "id") Long id);

    Optional<Product> findByCode(String code);

    Optional<Product> findByName(String name);

    Page<Product> findByOrderByName(Pageable pageable);
}
