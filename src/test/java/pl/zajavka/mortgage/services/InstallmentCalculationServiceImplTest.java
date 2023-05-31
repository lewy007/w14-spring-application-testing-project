package pl.zajavka.mortgage.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import pl.zajavka.mortgage.configuration.CalculatorConfiguration;
import pl.zajavka.mortgage.fixtures.TestDataFixtures;
import pl.zajavka.mortgage.model.InputData;
import pl.zajavka.mortgage.model.Installment;
import pl.zajavka.mortgage.model.InstallmentAmounts;
import pl.zajavka.mortgage.model.Overpayment;
import pl.zajavka.mortgage.model.TimePoint;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = CalculatorConfiguration.class)
class InstallmentCalculationServiceImplTest {

    @InjectMocks
    private InstallmentCalculationServiceImpl installmentCalculationService;

    @Mock
    private TimePointCalculationService timePointCalculationService;
    @Mock
    private AmountsCalculationService amountsCalculationService;
    @Mock
    private ResidualCalculationService residualCalculationService;
    @Mock
    private ReferenceCalculationService referenceCalculationService;
    @Mock
    private OverpaymentCalculationService overpaymentCalculationService;

    @Test
    void thatInstallmentAreCalculatedCorrectly() {
        //given
        InputData inputData = TestDataFixtures.someInputData();
        final var expected = IntStream.rangeClosed(1, 180).boxed()
                .map(this::createInstallment).toList();

        Mockito.when(timePointCalculationService.calculate(
                        ArgumentMatchers.any(BigDecimal.class),
                        ArgumentMatchers.any(InputData.class)))
                .thenReturn(TestDataFixtures.someTimePoint());
        Mockito.when(timePointCalculationService.calculate(
                        ArgumentMatchers.any(BigDecimal.class),
                        ArgumentMatchers.any(Installment.class)))
                .thenReturn(TestDataFixtures.someTimePoint());

        Mockito.when(amountsCalculationService.calculate(
                        ArgumentMatchers.any(InputData.class),
                        ArgumentMatchers.any(Overpayment.class)))
                .thenReturn(TestDataFixtures.someInstallmentAmounts());
        Mockito.when(amountsCalculationService.calculate(
                        ArgumentMatchers.any(InputData.class),
                        ArgumentMatchers.any(Overpayment.class),
                        ArgumentMatchers.any(Installment.class)))
                .thenReturn(TestDataFixtures.someInstallmentAmounts());

        Mockito.when(residualCalculationService.calculate(
                        ArgumentMatchers.any(InstallmentAmounts.class),
                        ArgumentMatchers.any(InputData.class)))
                .thenReturn(TestDataFixtures.someMortgageResidual());
        Mockito.when(residualCalculationService.calculate(
                        ArgumentMatchers.any(InstallmentAmounts.class),
                        ArgumentMatchers.any(InputData.class),
                        ArgumentMatchers.any(Installment.class)))
                .thenReturn(TestDataFixtures.someMortgageResidual());

        Mockito.when(referenceCalculationService.calculate(
                        ArgumentMatchers.any(InstallmentAmounts.class),
                        ArgumentMatchers.any(InputData.class)))
                .thenReturn(TestDataFixtures.someMortgageReference());
        Mockito.when(referenceCalculationService.calculate(
                        ArgumentMatchers.any(InstallmentAmounts.class),
                        ArgumentMatchers.any(InputData.class),
                        ArgumentMatchers.any(Installment.class)))
                .thenReturn(TestDataFixtures.someMortgageReference());

        Mockito.when(overpaymentCalculationService.calculate(
                        ArgumentMatchers.any(BigDecimal.class),
                        ArgumentMatchers.any(InputData.class)))
                .thenReturn(TestDataFixtures.someOverpayment());
        ;

        //when
        // z tej metody została wygenerowana lista 180 rat
        List<Installment> result = installmentCalculationService.calculate(inputData);

        //then
        //spodziewalimsy sie 180 rat, wszytskie raty maja te same wartosci, poniewaz wykorzystalismy metody
        // dostepne w klasie TestDataFixtures
        Assertions.assertEquals(expected, result);
    }

    private Installment createInstallment(Integer index) {
        return TestDataFixtures.someInstallment()
                .withInstallmentNumber(BigDecimal.valueOf(index));
    }
}