package com.example.demo.domain.income;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class SimpleIncomeCalculationResult {

    private BigDecimal value;
    private BigDecimal percent;
}
