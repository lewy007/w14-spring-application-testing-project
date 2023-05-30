package pl.zajavka.mortgage.services;

import pl.zajavka.mortgage.model.InputData;
import pl.zajavka.mortgage.model.Installment;
import pl.zajavka.mortgage.model.InstallmentAmounts;
import pl.zajavka.mortgage.model.MortgageReference;

public interface ReferenceCalculationService {

    MortgageReference calculate(InstallmentAmounts installmentAmounts, InputData inputData);

    MortgageReference calculate(InstallmentAmounts installmentAmounts, final InputData inputData, Installment previousInstallment);

}
