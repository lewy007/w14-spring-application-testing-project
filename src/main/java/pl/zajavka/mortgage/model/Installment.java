package pl.zajavka.mortgage.model;

import lombok.Builder;
import lombok.With;

import java.math.BigDecimal;

@With
public record Installment(
    BigDecimal installmentNumber,
    TimePoint timePoint,
    InstallmentAmounts installmentAmounts,
    MortgageResidual mortgageResidual,
    MortgageReference mortgageReference
) {

    @Builder
    public Installment {
    }

}
