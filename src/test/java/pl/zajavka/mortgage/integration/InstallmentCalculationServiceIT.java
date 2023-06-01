package pl.zajavka.mortgage.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import pl.zajavka.mortgage.configuration.CalculatorConfiguration;
import pl.zajavka.mortgage.fixtures.TestDataFixtures;
import pl.zajavka.mortgage.model.InputData;
import pl.zajavka.mortgage.model.Installment;
import pl.zajavka.mortgage.services.InstallmentCalculationService;

import java.util.List;

@SpringJUnitConfig(classes = {CalculatorConfiguration.class})
public class InstallmentCalculationServiceIT {

    @Autowired
    private InstallmentCalculationService installmentCalculationService;

    @BeforeEach
    void setup() {
        Assertions.assertNotNull(installmentCalculationService);
    }

    @Test
    @DisplayName("Test installment calculation")
    void test() {
        //given
        InputData inputData = TestDataFixtures.someInputData();

        //when
        // to zostanie policzone tak jakbysmy normalnie uruchomili program, poniewaz podnieisony jest caly spring context,
        // a to znaczy, ze wszytskie beany springowe zostaly dobrze utworzone
        List<Installment> result = installmentCalculationService.calculate(inputData);

        //then
        //porównujmey ilosc rat
        Assertions.assertEquals(180,result.size());
        //porownujmey rate numer 5,10,40,80
        Assertions.assertEquals(TestDataFixtures.someInstallment5(),result.get(4));
        Assertions.assertEquals(TestDataFixtures.someInstallment10(),result.get(9));
        Assertions.assertEquals(TestDataFixtures.someInstallment40(),result.get(39));
        Assertions.assertEquals(TestDataFixtures.someInstallment80(),result.get(79));

    }
}
