
package com.example.demo.domain.income;

import com.example.demo.restapi.request.GrossSalaryRequest;
import com.example.demo.restapi.response.LiquidSalaryResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.config.IncomeDefaultConfig.VALUE_DISCOUT_FIX;

@Service
public class IncomeCalculatorService {

    private static final BigDecimal VALUE_DEPENDENTS_DEDUCTION = new BigDecimal("189.59");

    private final List<Income> inssRange;
    private final List<Income> irrfRange;
    private final Map<Integer, INSSCalculate> inssCalculatorMap;

    // é necessário manter a ordenação
    public IncomeCalculatorService(@Qualifier("inssRange") List<Income> inssRange,
                                   @Qualifier("irrfRange") List<Income> irrfRange) {
        this.inssRange = new ArrayList<>();
        this.inssRange.addAll(inssRange);

        this.irrfRange = new ArrayList<>();
        this.irrfRange.addAll(irrfRange);

        this.inssCalculatorMap = inssIncomeCalculatorMap();
    }


    private SimpleIncomeCalculationResult calculateINSS(final BigDecimal value) {
        final var inssValues = getINSSValues(value);
        BigDecimal inssVal = inssCalculatorMap.get(inssValues.getRange()).calculate(inssRange, inssValues, value);
        final var aliquota = inssVal.compareTo(VALUE_DISCOUT_FIX) != 0 ?
                inssVal.divide(value, RoundingMode.FLOOR) :
                null;

        return new SimpleIncomeCalculationResult(inssVal, aliquota);
    }

    private Income getINSSValues(final  BigDecimal value) {
        return inssRange.stream()
                .filter(income ->
                        income.getMinValue().compareTo(value) <= 0 &&
                                0 <= income.getMaxValue().compareTo(value))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("erro ao encontrar"));
    }

    public IncomeCalculationResult calculateINSSAndIRRF(final BigDecimal value, final Integer dependents) {
        final SimpleIncomeCalculationResult inssResult = calculateINSS(value);
        final Income irrfValues = getIRRFValues(value, inssResult);

        var irrfBase = value
                .subtract(inssResult.getValue())
                .subtract(new BigDecimal(dependents).multiply(VALUE_DEPENDENTS_DEDUCTION));
        var irrfPorcent = irrfValues.getDividedPercent();
        var irrfDeduc = irrfValues.getParcelTax();
        var irrfVal = irrfBase.multiply(irrfPorcent).subtract(irrfDeduc);
        var totalDiscount = inssResult.getValue().add(irrfVal);

        var aliquotaIrrf = irrfVal.divide(irrfBase, RoundingMode.FLOOR).multiply(new BigDecimal("100"));

        return new IncomeCalculationResult(
                inssResult.getValue(),
                inssResult.getPercent(),
                irrfVal,
                irrfPorcent,
                value,
                totalDiscount,
                value.subtract(totalDiscount),
                aliquotaIrrf);
    }

    private Income getIRRFValues(final BigDecimal value, final SimpleIncomeCalculationResult inss) {
        return irrfRange.stream()
                .filter(income ->
                    income.getMinValue().compareTo(value.subtract(inss.getValue())) < 0 &&
                            income.getMaxValue().compareTo(value.subtract(inss.getValue())) > 0)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("erro ao encontrar"));
    }



    public Map<Integer, INSSCalculate> inssIncomeCalculatorMap() {
        var result = new HashMap<Integer, INSSCalculate>();
        result.put(1,
                new INSSCalculate() {
                    @Override
                    public BigDecimal calculate(List<Income> range, Income inssValues, BigDecimal value) {
                        return inssValues.getDividedPercent().multiply(value);
                    }
                });
        result.put(2, new INSSCalculate() {
            @Override
            public BigDecimal calculate(List<Income> range, Income inssValues, BigDecimal value) {
                var secondRangeSalary = value
                        .subtract(range.get(0).getMaxValue())
                        .multiply(range.get(1).getDividedPercent());
                return getINSSRanges(range).get(1).add(secondRangeSalary);
            }
        });
        result.put(3, new INSSCalculate() {
            @Override
            public BigDecimal calculate(List<Income> range, Income inssValues, BigDecimal value) {
                var thirdRangeSalary = value
                        .subtract(range.get(1).getMaxValue())
                        .multiply(range.get(2).getDividedPercent());
                return getINSSRanges(range).get(1)
                        .add(getINSSRanges(range).get(2))
                        .add(thirdRangeSalary);
            }
        });
        result.put(4, new INSSCalculate() {
            @Override
            public BigDecimal calculate(List<Income> range, Income inssValues, BigDecimal value) {
                var fourthRangeSalary = value
                        .subtract(range.get(2).getMaxValue())
                        .multiply(range.get(3).getDividedPercent());
                return getINSSRanges(range).get(1)
                        .add(getINSSRanges(range).get(2))
                        .add(getINSSRanges(range).get(3))
                        .add(fourthRangeSalary);
            }
        });
        result.put(5, (range, inssValues, value) -> VALUE_DISCOUT_FIX);
        return result;
    }

    private Map<Integer, BigDecimal> getINSSRanges(List<Income> inssRange) {
        return new HashMap<>() {{
            put(1, inssRange.get(0).getPercentByMaxValue());
            put(2, inssRange.get(1).getMaxValue().subtract(inssRange.get(0).getMaxValue()).multiply(inssRange.get(1).getDividedPercent()));
            put(3, inssRange.get(2).getMaxValue().subtract(inssRange.get(1).getMaxValue()).multiply(inssRange.get(2).getDividedPercent()));
        }};
    }

    public interface INSSCalculate {
        BigDecimal calculate(List<Income> range, Income inssValues, BigDecimal value);
    }
}
