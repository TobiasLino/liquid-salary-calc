package com.example.demo.restapi.response;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LiquidSalaryResponse {

    private String grossSalary;
    private String otherDiscounts;
    private String inssPercent;
    private String inssValue;
    private String irrfPercent;
    private String irrfValue;
    private String totalDiscount;
    private String liquidSalary;
}
