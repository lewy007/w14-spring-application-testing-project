package pl.zajavka.mortgage.model;

import lombok.Builder;
import lombok.With;

import java.math.BigDecimal;

@With
public record InstallmentAmounts(
    BigDecimal installmentAmount,
    BigDecimal interestAmount,
    BigDecimal capitalAmount,
    Overpayment overpayment
) {

    @Builder
    public InstallmentAmounts {
    }

}
