package pl.zajavka.mortgage.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.zajavka.mortgage.model.InputData;
import pl.zajavka.mortgage.model.Installment;
import pl.zajavka.mortgage.model.InstallmentAmounts;
import pl.zajavka.mortgage.model.Overpayment;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class DecreasingAmountsCalculationServiceImpl implements DecreasingAmountsCalculationService {

    @Override
    public InstallmentAmounts calculate(final InputData inputData, final Overpayment overpayment) {
        BigDecimal interestPercent = inputData.interestPercent();
        log.info("InterestPercent: [{}]", interestPercent);

        final BigDecimal residualAmount = inputData.amount();
        final BigDecimal residualDuration = inputData.monthsDuration();

        BigDecimal interestAmount = AmountsCalculationService.calculateInterestAmount(residualAmount, interestPercent);
        BigDecimal capitalAmount = AmountsCalculationService.compareCapitalWithResidual(
            calculateDecreasingCapitalAmount(residualAmount, residualDuration), residualAmount);
        BigDecimal installmentAmount = capitalAmount.add(interestAmount);

        log.info(
            "Calculated installment: [{}], residualAmount: [{}], interestAmount: [{}], capitalAmount: [{}], installmentAmount: [{}]",
            1, residualAmount, interestAmount, capitalAmount, installmentAmount);
        return new InstallmentAmounts(installmentAmount, interestAmount, capitalAmount, overpayment);
    }

    @Override
    public InstallmentAmounts calculate(final InputData inputData, final Overpayment overpayment, final Installment previousInstallment) {
        BigDecimal interestPercent = inputData.interestPercent();

        BigDecimal residualAmount = previousInstallment.mortgageResidual().residualAmount();
        BigDecimal referenceAmount = previousInstallment.mortgageReference().referenceAmount();
        BigDecimal referenceDuration = previousInstallment.mortgageReference().referenceDuration();

        BigDecimal interestAmount = AmountsCalculationService.calculateInterestAmount(residualAmount, interestPercent);
        BigDecimal capitalAmount = AmountsCalculationService.compareCapitalWithResidual(
            calculateDecreasingCapitalAmount(referenceAmount, referenceDuration), residualAmount);
        BigDecimal installmentAmount = capitalAmount.add(interestAmount);

        log.info(
            "Calculated installment: [{}], residualAmount: [{}], interestAmount: [{}], capitalAmount: [{}], installmentAmount: [{}], " +
                "referenceAmount: [{}], referenceDuration: [{}]",
            previousInstallment.installmentNumber().add(BigDecimal.ONE), residualAmount, interestAmount, capitalAmount, installmentAmount,
            referenceAmount, referenceDuration);
        return new InstallmentAmounts(installmentAmount, interestAmount, capitalAmount, overpayment);
    }

    private BigDecimal calculateDecreasingCapitalAmount(final BigDecimal residualAmount, final BigDecimal residualDuration) {
        return residualAmount.divide(residualDuration, 2, RoundingMode.HALF_UP);
    }
}
