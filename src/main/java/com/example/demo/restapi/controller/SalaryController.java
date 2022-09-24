package com.example.demo.restapi.controller;

import com.example.demo.domain.income.IncomeCalculatorService;
import com.example.demo.restapi.request.GrossSalaryRequest;
import com.example.demo.restapi.response.LiquidSalaryResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@RestController
@RequestMapping("/salary/")
@AllArgsConstructor
public class SalaryController {

    private final IncomeCalculatorService service;

    @GetMapping()
    public LiquidSalaryResponse getLiquidSalary(@RequestBody GrossSalaryRequest grossSalary) {
        var incomeResult = service.calculateINSSAndIRRF(new BigDecimal(grossSalary.getGrossSalary()), grossSalary.getDependents());
        var inss = incomeResult.getInssPercent().multiply(new BigDecimal(100));
        var irrf = incomeResult.getIrrfPercent().multiply(new BigDecimal(100));

        var totalDiscount = new BigDecimal(grossSalary.getDiscount())
                .add(incomeResult.getInssValue())
                .add(incomeResult.getIrrfValue());
        var liquidSalary = new BigDecimal(grossSalary.getGrossSalary())
                .subtract(totalDiscount);

        var numberFormat = NumberFormat
                .getCurrencyInstance(new Locale("pt", "BR"));
        return LiquidSalaryResponse.builder()
                .grossSalary(grossSalary.getGrossSalary())
                .otherDiscounts(grossSalary.getDiscount())
                .inssPercent(inss + "%")
                .inssValue(numberFormat.format(incomeResult.getInssValue()))
                .irrfPercent(irrf + "%")
                .irrfValue(numberFormat.format(incomeResult.getIrrfValue()))
                .totalDiscount(numberFormat.format(totalDiscount))
                .liquidSalary(numberFormat.format(liquidSalary))
                .build();
    }
}
