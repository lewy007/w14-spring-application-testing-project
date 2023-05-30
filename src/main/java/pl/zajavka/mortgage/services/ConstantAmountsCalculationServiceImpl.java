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
public class ConstantAmountsCalculationServiceImpl implements ConstantAmountsCalculationService {

    @Override
    public InstallmentAmounts calculate(final InputData inputData, final Overpayment overpayment) {
        BigDecimal interestPercent = inputData.interestPercent();
        log.debug("InterestPercent: [{}]", interestPercent);
        BigDecimal q = AmountsCalculationService.calculateQ(interestPercent);
        log.trace("Q: [{}]", q);

        BigDecimal residualAmount = inputData.amount();

        BigDecimal interestAmount = AmountsCalculationService.calculateInterestAmount(residualAmount, interestPercent);
        BigDecimal installmentAmount = calculateConstantInstallmentAmount(q, interestAmount, residualAmount, inputData.amount(), inputData.monthsDuration());
        BigDecimal capitalAmount = AmountsCalculationService.compareCapitalWithResidual(installmentAmount.subtract(interestAmount), residualAmount);

        log.info(
            "Calculated installment: [{}], residualAmount: [{}], interestAmount: [{}], capitalAmount: [{}], installmentAmount: [{}]",
            1, residualAmount, interestAmount, capitalAmount, installmentAmount);
        return new InstallmentAmounts(installmentAmount, interestAmount, capitalAmount, overpayment);
    }

    @Override
    public InstallmentAmounts calculate(final InputData inputData, final Overpayment overpayment, final Installment previousInstallment) {
        BigDecimal interestPercent = inputData.interestPercent();
        BigDecimal q = AmountsCalculationService.calculateQ(interestPercent);

        BigDecimal residualAmount = previousInstallment.mortgageResidual().residualAmount();
        BigDecimal referenceAmount = previousInstallment.mortgageReference().referenceAmount();
        BigDecimal referenceDuration = previousInstallment.mortgageReference().referenceDuration();

        BigDecimal interestAmount = AmountsCalculationService.calculateInterestAmount(residualAmount, interestPercent);
        BigDecimal installmentAmount = calculateConstantInstallmentAmount(q, interestAmount, residualAmount, referenceAmount, referenceDuration);
        BigDecimal capitalAmount = AmountsCalculationService.compareCapitalWithResidual(installmentAmount.subtract(interestAmount), residualAmount);

        log.info(
            "Calculated installment: [{}], residualAmount: [{}], interestAmount: [{}], capitalAmount: [{}], installmentAmount: [{}], " +
                "referenceAmount: [{}], referenceDuration: [{}]",
            previousInstallment.installmentNumber().add(BigDecimal.ONE), residualAmount, interestAmount, capitalAmount, installmentAmount,
            referenceAmount, referenceDuration);

        return new InstallmentAmounts(installmentAmount, interestAmount, capitalAmount, overpayment);
    }

    private BigDecimal calculateConstantInstallmentAmount(
        final BigDecimal q,
        final BigDecimal interestAmount,
        final BigDecimal residualAmount,
        final BigDecimal referenceAmount,
        final BigDecimal referenceDuration
    ) {
        BigDecimal installmentAmount = referenceAmount
            .multiply(q.pow(referenceDuration.intValue()))
            .multiply(q.subtract(BigDecimal.ONE))
            .divide(q.pow(referenceDuration.intValue()).subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);

        return compareInstallmentWithResidual(installmentAmount, interestAmount, residualAmount);
    }

    private BigDecimal compareInstallmentWithResidual(
        final BigDecimal installmentAmount,
        final BigDecimal interestAmount,
        final BigDecimal residualAmount
    ) {
        if (installmentAmount.subtract(interestAmount).compareTo(residualAmount) >= 0) {
            return residualAmount.add(interestAmount);
        }
        return installmentAmount;
    }

}
