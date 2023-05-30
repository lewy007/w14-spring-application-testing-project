package pl.zajavka.mortgage.services;

import pl.zajavka.mortgage.model.Installment;
import pl.zajavka.mortgage.model.Summary;

import java.util.List;

public interface SummaryService {

    Summary calculateSummary(List<Installment> installments);
}
