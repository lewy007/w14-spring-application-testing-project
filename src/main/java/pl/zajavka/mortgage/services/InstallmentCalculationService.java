package pl.zajavka.mortgage.services;

import pl.zajavka.mortgage.model.InputData;
import pl.zajavka.mortgage.model.Installment;

import java.util.List;

public interface InstallmentCalculationService {

    List<Installment> calculate(InputData inputData);
}
