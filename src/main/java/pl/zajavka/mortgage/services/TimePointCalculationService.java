package pl.zajavka.mortgage.services;

import pl.zajavka.mortgage.model.InputData;
import pl.zajavka.mortgage.model.Installment;
import pl.zajavka.mortgage.model.TimePoint;

import java.math.BigDecimal;

public interface TimePointCalculationService {

    TimePoint calculate(final BigDecimal installmentNumber, final InputData inputData);

    TimePoint calculate(BigDecimal installmentNumber, Installment previousInstallment);

}
