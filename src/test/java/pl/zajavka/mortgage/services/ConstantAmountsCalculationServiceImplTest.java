package pl.zajavka.mortgage.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import pl.zajavka.mortgage.fixtures.TestDataFixtures;
import pl.zajavka.mortgage.model.InputData;
import pl.zajavka.mortgage.model.Installment;
import pl.zajavka.mortgage.model.InstallmentAmounts;
import pl.zajavka.mortgage.model.Overpayment;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = ContextConfiguration.class)
class ConstantAmountsCalculationServiceImplTest {

    @InjectMocks
    ConstantAmountsCalculationService constantAmountsCalculationService = new ConstantAmountsCalculationServiceImpl();

    @Test
    @DisplayName("Calculate installment amounts for first installment")
    void shouldCalculateFirstInstallmentAmountsCorrectly() {
        //given
        InputData inputData = TestDataFixtures.someInputData();
        Overpayment overpayment = TestDataFixtures.someOverpayment();
        InstallmentAmounts expected = TestDataFixtures.someInstallmentAmounts().withOverpayment(overpayment);

        //when
        InstallmentAmounts result = constantAmountsCalculationService.calculate(inputData, overpayment);

        //then
        Assertions.assertEquals(expected, result);

    }

    @Test
    @DisplayName("Calculate installment amounts for other installment")
    void shouldCalculateOtherInstallmentsAmountsCorrectly() {
        //given
        InputData inputData = TestDataFixtures.someInputData();
        Overpayment overpayment = TestDataFixtures.someOverpayment();
        Installment installment = TestDataFixtures.someInstallment();

        InstallmentAmounts expected = TestDataFixtures.someInstallmentAmounts()
                .withInstallmentAmount(new BigDecimal("3303.45"))
                .withInterestAmount(new BigDecimal("2483.87"))
                .withCapitalAmount(new BigDecimal("819.58"));

        //when
        InstallmentAmounts result = constantAmountsCalculationService.calculate(inputData, null, installment);

        //then
        Assertions.assertEquals(expected, result);
//        Assertions.assertNotNull(result);
    }

}