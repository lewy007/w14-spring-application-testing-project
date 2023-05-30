package pl.zajavka.mortgage.fixtures;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import pl.zajavka.mortgage.configuration.CalculatorConfiguration;
import pl.zajavka.mortgage.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class TestDataFixtures {

    public static Installment someInstallment() {
        return Installment.builder()
            .installmentNumber(BigDecimal.valueOf(10))
            .timePoint(someTimePoint())
            .installmentAmounts(someInstallmentAmounts())
            .mortgageResidual(someMortgageResidual())
            .mortgageReference(someMortgageReference())
            .build();
    }

    public static MortgageReference someMortgageReference() {
        return MortgageReference.builder()
            .referenceAmount(new BigDecimal("235243.12"))
            .referenceDuration(BigDecimal.valueOf(83))
            .build();
    }

    public static MortgageResidual someMortgageResidual() {
        return MortgageResidual.builder()
            .residualAmount(new BigDecimal("662364.12"))
            .residualDuration(BigDecimal.valueOf(16))
            .build();
    }

    public static InputData someInputData() {
        return InputData.builder()
            .repaymentStartDate(LocalDate.of(2010, 5, 10))
            .wiborPercent(BigDecimal.valueOf(2.70))
            .amount(BigDecimal.valueOf(198267.46))
            .monthsDuration(BigDecimal.valueOf(180))
            .installmentType(MortgageType.CONSTANT)
            .marginPercent(BigDecimal.valueOf(1.8))
            .overpaymentProvisionPercent(BigDecimal.valueOf(3))
            .overpaymentProvisionMonths(BigDecimal.valueOf(36))
            .overpaymentStartMonth(BigDecimal.valueOf(1))
            .overpaymentSchema(Map.of())
            .overpaymentReduceWay(Overpayment.REDUCE_PERIOD)
            .mortgagePrintPayoffsSchedule(true)
            .mortgageInstallmentNumberToPrint(1)
            .build();
    }

    public static TimePoint someTimePoint() {
        return TimePoint.builder()
            .year(BigDecimal.valueOf(1))
            .month(BigDecimal.valueOf(1))
            .date(LocalDate.of(2010, 5, 10))
            .build();
    }

    public static Overpayment someOverpayment() {
        return Overpayment.builder()
            .amount(new BigDecimal("1255.02"))
            .provisionAmount(new BigDecimal("102"))
            .build();
    }

    public static InstallmentAmounts someInstallmentAmounts() {
        return InstallmentAmounts.builder()
            .installmentAmount(new BigDecimal("1516.73"))
            .capitalAmount(new BigDecimal("773.23"))
            .interestAmount(new BigDecimal("743.50"))
            .build();
    }

    public static Installment someInstallment5() {
        return Installment.builder()
            .installmentNumber(BigDecimal.valueOf(5))
            .timePoint(TimePoint.builder()
                .year(BigDecimal.ONE)
                .month(BigDecimal.valueOf(5))
                .date(LocalDate.of(2010, 9, 10))
                .build())
            .installmentAmounts(InstallmentAmounts.builder()
                .installmentAmount(new BigDecimal("1516.73"))
                .interestAmount(new BigDecimal("731.84"))
                .capitalAmount(new BigDecimal("784.89"))
                .overpayment(Overpayment.builder()
                    .amount(BigDecimal.ZERO)
                    .provisionAmount(BigDecimal.ZERO)
                    .build())
                .build())
            .mortgageResidual(MortgageResidual.builder()
                .residualAmount(new BigDecimal("194372.21"))
                .residualDuration(new BigDecimal("175"))
                .build())
            .mortgageReference(MortgageReference.builder()
                .referenceAmount(new BigDecimal("198267.46"))
                .referenceDuration(new BigDecimal("180"))
                .build())
            .build();
    }

    public static Installment someInstallment10() {
        return Installment.builder()
            .installmentNumber(BigDecimal.valueOf(10))
            .timePoint(TimePoint.builder()
                .year(BigDecimal.ONE)
                .month(BigDecimal.valueOf(10))
                .date(LocalDate.of(2011, 2, 10))
                .build())
            .installmentAmounts(InstallmentAmounts.builder()
                .installmentAmount(new BigDecimal("1516.73"))
                .interestAmount(new BigDecimal("717.01"))
                .capitalAmount(new BigDecimal("799.72"))
                .overpayment(Overpayment.builder()
                    .amount(BigDecimal.ZERO)
                    .provisionAmount(BigDecimal.ZERO)
                    .build())
                .build())
            .mortgageResidual(MortgageResidual.builder()
                .residualAmount(new BigDecimal("190403.39"))
                .residualDuration(new BigDecimal("170"))
                .build())
            .mortgageReference(MortgageReference.builder()
                .referenceAmount(new BigDecimal("198267.46"))
                .referenceDuration(new BigDecimal("180"))
                .build())
            .build();
    }

    public static Installment someInstallment40() {
        return Installment.builder()
            .installmentNumber(BigDecimal.valueOf(40))
            .timePoint(TimePoint.builder()
                .year(BigDecimal.valueOf(4))
                .month(BigDecimal.valueOf(4))
                .date(LocalDate.of(2013, 8, 10))
                .build())
            .installmentAmounts(InstallmentAmounts.builder()
                .installmentAmount(new BigDecimal("1516.73"))
                .interestAmount(new BigDecimal("621.98"))
                .capitalAmount(new BigDecimal("894.75"))
                .overpayment(Overpayment.builder()
                    .amount(BigDecimal.ZERO)
                    .provisionAmount(BigDecimal.ZERO)
                    .build())
                .build())
            .mortgageResidual(MortgageResidual.builder()
                .residualAmount(new BigDecimal("164965.45"))
                .residualDuration(new BigDecimal("140"))
                .build())
            .mortgageReference(MortgageReference.builder()
                .referenceAmount(new BigDecimal("198267.46"))
                .referenceDuration(new BigDecimal("180"))
                .build())
            .build();
    }

    public static Installment someInstallment80() {
        return Installment.builder()
            .installmentNumber(BigDecimal.valueOf(80))
            .timePoint(TimePoint.builder()
                .year(BigDecimal.valueOf(7))
                .month(BigDecimal.valueOf(8))
                .date(LocalDate.of(2016, 12, 10))
                .build())
            .installmentAmounts(InstallmentAmounts.builder()
                .installmentAmount(new BigDecimal("1516.73"))
                .interestAmount(new BigDecimal("477.47"))
                .capitalAmount(new BigDecimal("1039.26"))
                .overpayment(Overpayment.builder()
                    .amount(BigDecimal.ZERO)
                    .provisionAmount(BigDecimal.ZERO)
                    .build())
                .build())
            .mortgageResidual(MortgageResidual.builder()
                .residualAmount(new BigDecimal("126284.86"))
                .residualDuration(new BigDecimal("100"))
                .build())
            .mortgageReference(MortgageReference.builder()
                .referenceAmount(new BigDecimal("198267.46"))
                .referenceDuration(new BigDecimal("180"))
                .build())
            .build();
    }
}
