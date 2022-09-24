package com.example.demo.domain.income;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class IncomeCalculationResult {

    private BigDecimal inssValue;
    private BigDecimal inssPercent;
    private BigDecimal irrfValue;
    private BigDecimal irrfPercent;
    private BigDecimal grossValue;
    private BigDecimal totalDiscount;
    private BigDecimal liquidValue;
    private BigDecimal aliquotaIRRF;
}
