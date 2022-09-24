package com.example.demo.restapi.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GrossSalaryRequest {

    private String grossSalary;
    private String discount;
    private Integer dependents;
}
