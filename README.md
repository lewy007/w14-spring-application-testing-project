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
### Trzeci commit dotyczy klasy InstallmentCalculationServiceImpl
```java
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
        // z tej metody z
        List<Installment> result = installmentCalculationService.calculate(inputData);

        //then
        Assertions.assertEquals(expected, result);
    }

    private Installment createInstallment(Integer index) {
        return TestDataFixtures.someInstallment()
                .withInstallmentNumber(BigDecimal.valueOf(index));
    }
}
```

### Czwarty commit dotyczy testu inegracyjnego, wiec podnosimy caly kontekst springowy
```java
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
```