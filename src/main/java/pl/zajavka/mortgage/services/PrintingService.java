package pl.zajavka.mortgage.services;

import pl.zajavka.mortgage.model.InputData;
import pl.zajavka.mortgage.model.Installment;
import pl.zajavka.mortgage.model.Summary;

import java.util.List;

public interface PrintingService {

    String SCHEDULE_TABLE_FORMAT =
        "%-4s %3s " +
            "%-4s %3s " +
            "%-7s %3s " +
            "%-7s %3s " +
            "%-4s %10s " +
            "%-7s %10s " +
            "%-7s %10s " +
            "%-7s %10s " +
            "%-8s %10s " +
            "%-8s %10s%n";

    List<String> INSTALLMENT_LINE_KEYS = List.of(
        "NR:",
        "ROK:",
        "MIESIĄC:",
        "DATA:",
        "RATA:",
        "ODSETKI:",
        "KAPITAŁ:",
        "NADPŁATA:",
        "PKWT:",
        "PMSC:"
    );

    String INTRO_INFORMATION = """
        KWOTA KREDYTU: %s ZŁ
        OKRES KREDYTOWANIA: %s MIESIĘCY
        ODSETKI: %s %%
        MIESIĄC ROZPOCZĘCIA NADPŁAT: %s MIESIĄC
        """;

    String OVERPAYMENT_INFORMATION = """
        %s
        SCHEMAT DOKONYWANIA NADPŁAT:
        %s""";

    String SUMMARY_INFORMATION = """
        
        SUMA ODSETEK: %s ZŁ
        PROWIZJA ZA NADPLATY: %s ZŁ
        SUMA STRAT: %s ZŁ
        SUMA KAPITAŁU: %s ZŁ
        
        """;

    String OVERPAYMENT_REDUCE_INSTALLMENT = "NADPŁATA - ZMNIEJSZENIE RATY";
    String OVERPAYMENT_REDUCE_PERIOD = "NADPŁATA - SKRÓCENIE OKRESU";
    String OVERPAYMENT_SCHEMA = "MIESIĄC: %s, KWOTA: %s ZŁ%n";

    void printIntroInformation(InputData inputData);

    void printSchedule(List<Installment> installments, final InputData inputData);

    void printSummary(Summary summaryNoOverpayment);
}
