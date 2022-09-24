package com.example.demo.domain.income;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@AllArgsConstructor
@Getter
public class Income {

    private BigDecimal percent;
    private BigDecimal parcelTax;
    private BigDecimal minValue;
    private BigDecimal maxValue;
    private Integer range;

    public Income(String percent, String minValue, String maxValue, Integer range) {
        if (percent != null ) this.percent = new BigDecimal(percent);
        if (minValue != null) this.minValue = new BigDecimal(minValue);
        if (maxValue != null) this.maxValue = new BigDecimal(maxValue);
        this.range = range;
    }


    public Income(String percent, String parcelTax, String minValue, String maxValue) {
        if (percent != null ) this.percent = new BigDecimal(percent);
        if (parcelTax != null) this.parcelTax = new BigDecimal(parcelTax);
        if (minValue != null) this.minValue = new BigDecimal(minValue);
        if (maxValue != null) this.maxValue = new BigDecimal(maxValue);
    }

    public BigDecimal getDividedPercent() {
        return getPercent()
                .divide(new BigDecimal(100), 3, RoundingMode.HALF_UP);
    }

    public BigDecimal getPercentByMaxValue() {
        return getMaxValue().multiply(getDividedPercent());
    }
}
