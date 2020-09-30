package com.paulok777.entity;

import com.paulok777.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String code;
    private String name;
    private Integer price;
    private Long amount;
    @Enumerated(EnumType.STRING)
    private Measure measure;
    @OneToMany(mappedBy = "product")
    Set<OrderProducts> orderProducts;

    public Product(ProductDTO productDTO) {
        code = productDTO.getCode();
        name = productDTO.getName();
        price = productDTO.getPrice();
        amount = productDTO.getAmount();
        measure = Measure.valueOf(productDTO.getMeasure());
    }
}
