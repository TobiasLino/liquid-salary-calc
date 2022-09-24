package com.example.demo.config;

import com.example.demo.domain.income.Income;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class IncomeDefaultConfig {

    // 7087.23 * 0.14 - 163.82
    public static final BigDecimal VALUE_DISCOUT_FIX =  new BigDecimal("7087.23")
            .multiply(new BigDecimal("0.14"))
            .subtract(new BigDecimal("163.82"));

    @Bean
    public List<Income> inssRange() {
        return new ArrayList<>() {{
            add(new Income("7.5", "0", "1212.0", 1));
            add(new Income("9", "1212.01", "2427.35", 2));
            add(new Income("12", "2427.36", "3641.03", 3));
            add(new Income("14", "3641.04", "7087.22", 4));
            add(new Income(null, "7087.23", "999999.0", 5));
        }};
    }

    @Bean
    public List<Income> irrfRange() {
        return new ArrayList<>() {{
            add(new Income("0", "0", "0", "1903.98"));
            add(new Income("7.5", "142.8", "1903.99", "2826.65"));
            add(new Income("15", "354.8", "2826.66", "3751.05"));
            add(new Income("22.5", "636.13", "3751.06", "4664.68"));
            add(new Income("27.5", "869.36", "4664.69", "999999"));
        }};
    }
}
