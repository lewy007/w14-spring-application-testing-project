package pl.zajavka.mortgage.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.zajavka.mortgage.model.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class InstallmentCalculationServiceImpl implements InstallmentCalculationService {

    private final TimePointCalculationService timePointCalculationService;

    private final AmountsCalculationService amountsCalculationService;

    private final ResidualCalculationService residualCalculationService;

    private final ReferenceCalculationService referenceCalculationService;

    private final OverpaymentCalculationService overpaymentCalculationService;

    @Override
    public List<Installment> calculate(final InputData inputData) {
        List<Installment> installmentList = new ArrayList<>();

        BigDecimal installmentNumber = BigDecimal.ONE;

        Installment zeroInstallment = calculateZeroInstallment(installmentNumber, inputData);

        Installment previousInstallment = zeroInstallment;
        installmentList.add(zeroInstallment);
        log.debug("Calculated: [{}] installment", 1);

        for (BigDecimal i = installmentNumber.add(BigDecimal.ONE); i.compareTo(inputData.monthsDuration()) <= 0; i = i.add(BigDecimal.ONE)) {
            Installment nextInstallment = calculateNextInstallment(i, inputData, previousInstallment);
            previousInstallment = nextInstallment;
            installmentList.add(nextInstallment);

            log.debug("Calculated: [{}] installment", i);
            log.trace("Calculated: [{}] installment: [{}]", i, nextInstallment);
            if (BigDecimal.ZERO.equals(nextInstallment.mortgageResidual().residualAmount().setScale(0, RoundingMode.HALF_UP))) {
                break;
            }
        }

        return installmentList;
    }

    private Installment calculateZeroInstallment(final BigDecimal installmentNumber, final InputData inputData) {
        TimePoint timePoint = timePointCalculationService.calculate(installmentNumber, inputData);
        Overpayment overpayment = overpaymentCalculationService.calculate(installmentNumber, inputData);
        InstallmentAmounts installmentAmounts = amountsCalculationService.calculate(inputData, overpayment);
        MortgageResidual mortgageResidual = residualCalculationService.calculate(installmentAmounts, inputData);
        MortgageReference mortgageReference = referenceCalculationService.calculate(installmentAmounts, inputData);

        return new Installment(installmentNumber, timePoint, installmentAmounts, mortgageResidual, mortgageReference);
    }

    private Installment calculateNextInstallment(final BigDecimal installmentNumber, final InputData inputData, final Installment previousInstallment) {
        TimePoint timepoint = timePointCalculationService.calculate(installmentNumber, previousInstallment);
        Overpayment overpayment = overpaymentCalculationService.calculate(installmentNumber, inputData);
        InstallmentAmounts installmentAmounts = amountsCalculationService.calculate(inputData, overpayment, previousInstallment);
        MortgageResidual mortgageResidual = residualCalculationService.calculate(installmentAmounts, inputData, previousInstallment);
        MortgageReference mortgageReference = referenceCalculationService.calculate(installmentAmounts, inputData, previousInstallment);

        return new Installment(installmentNumber, timepoint, installmentAmounts, mortgageResidual, mortgageReference);
    }

}
