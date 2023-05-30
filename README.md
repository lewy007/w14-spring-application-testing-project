## Projekt - testy jednostkowe do aplikacji kredytu hipotecznego opartej na springu

### czyszczenie gradle -> clean -> build
### jacoco - java code coverage -> W skrócie, plugin Jacoco w Javie służy do mierzenia i generowania raportów dotyczących pokrycia kodu źródłowego przez testy jednostkowe, co pomaga programistom w ocenie jakości testów i identyfikacji luk w testowaniu. Raport znajduje sie -> build -> reports -> jacoco -> test -> html -> index.html

### Pierwszy commit dotyczył klasy TimePointCalculationServiceImpl
```java
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = CalculatorConfiguration.class)
class TimePointCalculationServiceImplTest {

    @InjectMocks
    // jak dodajemy interfejs jako injectMocks to musimy stworzyć obiekt, bo java nie wie, jaką implementacje użyć
    private TimePointCalculationService timePointCalculationService = new TimePointCalculationServiceImpl();

    @Test
    @DisplayName("Should calculate first installment time point successfully")
    public void calculateTimePointForFirstInstallment() {
        //given
        TimePoint expected = TestDataFixtures.someTimePoint();
        InputData inputData = TestDataFixtures.someInputData();

        //when
        TimePoint result = timePointCalculationService.calculate(BigDecimal.ONE, inputData);

        //then
        Assertions.assertEquals(expected, result);

    }

    @ParameterizedTest
    @MethodSource(value = "testMortgageData")
    @DisplayName("Should calculate other than first installment time point successfully")
    void calculateTimePointForOtherInstallment(
            LocalDate expectedDate,
            BigDecimal installmentNumber,
            BigDecimal year,
            BigDecimal month,
            LocalDate date
    ) {
        //given
        TimePoint timePoint = TestDataFixtures.someTimePoint()
                .withYear(year)
                .withMonth(month)
                .withDate(date);
        Installment installment = TestDataFixtures.someInstallment().withTimePoint(timePoint);
        TimePoint expected = timePoint.withDate(expectedDate);


        //when
        TimePoint result = timePointCalculationService.calculate(installmentNumber, installment);

        //then
        Assertions.assertEquals(expected, result);
    }

    public static Stream<Arguments> testMortgageData() {
        return Stream.of(
                Arguments.of(
                        LocalDate.of(2010, 2, 1),
                        BigDecimal.valueOf(12),
                        BigDecimal.ONE,
                        BigDecimal.valueOf(12),
                        LocalDate.of(2010, 1, 1)
                ),
                Arguments.of(
                        LocalDate.of(2010, 2, 1),
                        BigDecimal.valueOf(15),
                        BigDecimal.valueOf(2),
                        BigDecimal.valueOf(3),
                        LocalDate.of(2010, 1, 1)
                ),
                Arguments.of(
                        LocalDate.of(2013, 10, 1),
                        BigDecimal.valueOf(76),
                        BigDecimal.valueOf(7),
                        BigDecimal.valueOf(4),
                        LocalDate.of(2013, 9, 1)
                )
        );
    }
}
```
### Drugi commit dotyczy klasy ConstantAmountsCalculationServiceImpl
```java
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
```