package pl.zajavka.mortgage.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.util.ResourceUtils;
import pl.zajavka.mortgage.configuration.CalculatorConfiguration;
import pl.zajavka.mortgage.services.MortgageCalculationService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@SpringJUnitConfig(classes = {CalculatorConfiguration.class})
public class MortgageCalculationServiceIT {

    private static final Path RESULT_FILE_PATH = Paths.get("build/generated/calculationResult.txt");
//    private static final String EXPECTED_GENERATED_CONTENT = "src/test/resources/expectedGeneratedResult.txt";
    private static final String EXPECTED_GENERATED_CONTENT = "classpath:expectedGeneratedResult.txt";


    @Autowired
    @SuppressWarnings("unused")
    //@SuppressWarnings("unused") trzeba dodać bo Intellij w wersji community nie ogarnia, ze pole wykorzystywane jet w tescie
    private MortgageCalculationService mortgageCalculationService;

    @BeforeEach
    void setUp() {
        Assertions.assertNotNull(mortgageCalculationService);
    }

    @Test
    @DisplayName("That whole app calculation works correctly")
    void test() {
        //given, when
        mortgageCalculationService.calculate();

        //then
        //porownay zawartosci dwoch plikow (RESULT, ktory powstaje w build->generated) oraz EXPECTED, ktory znajduje sie
        //w resorces w testach

        final var generatedResultContent = readGeneratedResultContent();
        final var expectedGeneratedResultContent = readExpectedGeneratedResultContent();

        // czytanie z pliku linijka po linijce (każda linijka to element w liscie)
        for (int i = 0; i < expectedGeneratedResultContent.size(); i++) {
            Assertions.assertEquals(expectedGeneratedResultContent.get(i), generatedResultContent.get(i));
        }
    }

    private List<String> readGeneratedResultContent() {
        try {
            return Files.readAllLines(RESULT_FILE_PATH);
        } catch (Exception e) {
            Assertions.fail("Reading file failed", e);
        }
        return List.of();
    }

    private List<String> readExpectedGeneratedResultContent() {
        try {
//            return Files.readAllLines(Paths.get(EXPECTED_GENERATED_CONTENT));
            File file = ResourceUtils.getFile(EXPECTED_GENERATED_CONTENT);
            return Files.readAllLines(file.toPath());
        } catch (Exception e) {
            Assertions.fail("Reading file failed", e);
        }
        return List.of();
    }
}

