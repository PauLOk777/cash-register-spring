package com.paulok777.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportDTO {
    private Long amount;
    private Long totalPrice;
}
