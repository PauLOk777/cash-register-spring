package com.paulok777.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.paulok777.dto.ProductDTO;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"orderProducts"})
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
}
