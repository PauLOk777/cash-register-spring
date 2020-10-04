package com.paulok777.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private String code;
    private String name;
    private int price;
    private long amount;
    private String measure;
}
