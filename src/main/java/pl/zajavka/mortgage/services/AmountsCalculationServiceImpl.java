package pl.zajavka.mortgage.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajavka.mortgage.model.InputData;
import pl.zajavka.mortgage.model.Installment;
import pl.zajavka.mortgage.model.InstallmentAmounts;
import pl.zajavka.mortgage.model.Overpayment;

@Service
@AllArgsConstructor
public class AmountsCalculationServiceImpl implements AmountsCalculationService {

    private final ConstantAmountsCalculationService constantAmountsCalculationService;

    private final DecreasingAmountsCalculationService decreasingAmountsCalculationService;

    @Override
    public InstallmentAmounts calculate(final InputData inputData, final Overpayment overpayment) {
        return switch (inputData.installmentType()) {
            case CONSTANT -> constantAmountsCalculationService.calculate(inputData, overpayment);
            case DECREASING -> decreasingAmountsCalculationService.calculate(inputData, overpayment);
        };
    }

    @Override
    public InstallmentAmounts calculate(final InputData inputData, final Overpayment overpayment, final Installment previousInstallment) {
        return switch (inputData.installmentType()) {
            case CONSTANT -> constantAmountsCalculationService.calculate(inputData, overpayment, previousInstallment);
            case DECREASING -> decreasingAmountsCalculationService.calculate(inputData, overpayment, previousInstallment);
        };
    }


}
