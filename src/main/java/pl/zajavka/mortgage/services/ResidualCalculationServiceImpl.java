package pl.zajavka.mortgage.services;

import org.springframework.stereotype.Service;
import pl.zajavka.mortgage.model.InputData;
import pl.zajavka.mortgage.model.Installment;
import pl.zajavka.mortgage.model.InstallmentAmounts;
import pl.zajavka.mortgage.model.MortgageResidual;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ResidualCalculationServiceImpl implements ResidualCalculationService {

    @Override
    public MortgageResidual calculate(InstallmentAmounts installmentAmounts, InputData inputData) {
        if (BigDecimal.ZERO.equals(inputData.amount())) {
            return new MortgageResidual(BigDecimal.ZERO, BigDecimal.ZERO);
        } else {
            BigDecimal residualAmount = calculateResidualAmount(inputData.amount(), installmentAmounts);
            BigDecimal residualDuration = calculateResidualDuration(inputData, residualAmount, inputData.monthsDuration(), installmentAmounts);
            return new MortgageResidual(residualAmount, residualDuration);
        }
    }

    @Override
    public MortgageResidual calculate(InstallmentAmounts installmentAmounts, final InputData inputData, Installment previousInstallment) {
        BigDecimal previousResidualAmount = previousInstallment.mortgageResidual().residualAmount();
        BigDecimal previousResidualDuration = previousInstallment.mortgageResidual().residualDuration();

        if (BigDecimal.ZERO.equals(previousResidualAmount)) {
            return new MortgageResidual(BigDecimal.ZERO, BigDecimal.ZERO);
        } else {
            BigDecimal residualAmount = calculateResidualAmount(previousResidualAmount, installmentAmounts);
            BigDecimal residualDuration = calculateResidualDuration(inputData, residualAmount, previousResidualDuration, installmentAmounts);
            return new MortgageResidual(residualAmount, residualDuration);
        }
    }

    private BigDecimal calculateResidualDuration(
        InputData inputData,
        BigDecimal residualAmount,
        BigDecimal previousResidualDuration,
        InstallmentAmounts installmentAmounts
    ) {
        // jak wystąpi nadpłata to zaczynają się schody,
        // trzeba przeliczyć kredyt w zależności od tego czy podczas nadpłaty zmniejszamy czas trwania czy wysokość raty
        if (installmentAmounts.overpayment().amount().compareTo(BigDecimal.ZERO) > 0) {
            return switch (inputData.installmentType()) {
                case CONSTANT -> calculateConstantResidualDuration(inputData, residualAmount, installmentAmounts);
                case DECREASING -> calculateDecreasingResidualDuration(residualAmount, installmentAmounts);
            };
        } else {
            // w każdym normalnym przypadku z miesiąca na miesiąc ilość pozostałych miesięcy jest zmniejszona o 1
            return previousResidualDuration.subtract(BigDecimal.ONE);
        }
    }

    private BigDecimal calculateDecreasingResidualDuration(BigDecimal residualAmount, InstallmentAmounts installmentAmounts) {
        return residualAmount.divide(installmentAmounts.capitalAmount(), 0, RoundingMode.CEILING);
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    // tutaj mamy zaszytą logikę z tego co wspomniałem w trakcie nagrań,
    // czyli jak oszacować ilość miesięcy jaka nam pozostała do spłaty przy nadpłacie, ratach stałych i zmniejszeniu czasu trwania.
    // Wyjaśnienie stosowanych wzorów zostało dostarczone w pliku z rozwiązaniem
    private BigDecimal calculateConstantResidualDuration(InputData inputData, BigDecimal residualAmount, InstallmentAmounts installmentAmounts) {
        // log_y(x) = log(x) / log (y)
        BigDecimal q = AmountsCalculationService.calculateQ(inputData.interestPercent());

        // licznik z naszego logarytmu z licznika wzoru końcowego
        BigDecimal xNumerator = installmentAmounts.installmentAmount();
        // mianownik z naszego logarytmu z licznika wzoru końcowego. b/m to równie dobrze q-1
        BigDecimal xDenominator = installmentAmounts.installmentAmount().subtract(residualAmount.multiply(q.subtract(BigDecimal.ONE)));

        BigDecimal x = xNumerator.divide(xDenominator, 10, RoundingMode.HALF_UP);
        BigDecimal y = q;

        // logarytm z licznika
        BigDecimal logX = BigDecimal.valueOf(Math.log(x.doubleValue()));
        // logarytm z mianownika
        BigDecimal logY = BigDecimal.valueOf(Math.log(y.doubleValue()));

        return logX.divide(logY, 0, RoundingMode.CEILING);
    }

    private BigDecimal calculateResidualAmount(final BigDecimal residualAmount, final InstallmentAmounts installmentAmounts) {
        return residualAmount
            .subtract(installmentAmounts.capitalAmount())
            .subtract(installmentAmounts.overpayment().amount())
            .max(BigDecimal.ZERO);
    }

}
