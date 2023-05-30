package pl.zajavka.mortgage.model;

import lombok.Builder;
import lombok.With;

import java.math.BigDecimal;
import java.time.LocalDate;

@With
public record TimePoint(BigDecimal year, BigDecimal month, LocalDate date) {

    @Builder
    public TimePoint {
    }
}
