package com.paulok777.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.paulok777.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private Integer price;
    private Long amount;
    @Enumerated(EnumType.STRING)
    private Measure measure;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    Set<OrderProducts> orderProducts = new HashSet<>();

    public Product(ProductDTO productDTO) {
        code = productDTO.getCode();
        name = productDTO.getName();
        price = productDTO.getPrice();
        amount = productDTO.getAmount();
        measure = Measure.valueOf(productDTO.getMeasure());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(code, product.code) &&
                Objects.equals(name, product.name) &&
                Objects.equals(price, product.price) &&
                Objects.equals(amount, product.amount) &&
                measure == product.measure;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, price, amount, measure);
    }
}
