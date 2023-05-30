package pl.zajavka.mortgage.services;

import pl.zajavka.mortgage.model.InputData;
import pl.zajavka.mortgage.model.Installment;
import pl.zajavka.mortgage.model.InstallmentAmounts;
import pl.zajavka.mortgage.model.Overpayment;

public interface DecreasingAmountsCalculationService {

    InstallmentAmounts calculate(InputData inputData, Overpayment overpayment);

    InstallmentAmounts calculate(InputData inputData, Overpayment overpayment, Installment previousInstallment);
}
