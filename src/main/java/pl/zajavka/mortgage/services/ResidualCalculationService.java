package pl.zajavka.mortgage.services;

import pl.zajavka.mortgage.model.InputData;
import pl.zajavka.mortgage.model.Installment;
import pl.zajavka.mortgage.model.InstallmentAmounts;
import pl.zajavka.mortgage.model.MortgageResidual;

public interface ResidualCalculationService {

    MortgageResidual calculate(InstallmentAmounts installmentAmounts, InputData inputData);

    MortgageResidual calculate(InstallmentAmounts installmentAmounts, final InputData inputData, Installment previousInstallment);

}
