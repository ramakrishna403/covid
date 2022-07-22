package covid.analysis.servicetest;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import covid.analysis.dao.CovidDataDao;
import covid.analysis.exception.InvalidDateException;
import covid.analysis.exception.InvalidDateRangeException;
import covid.analysis.exception.InvalidStateCodeException;
import covid.analysis.exception.NoDataFoundException;
import covid.analysis.service.CovidDataService;

class ServiceClassTest {

	private static CovidDataService covidDataService;

	@BeforeAll
	static void setUp() {
		covidDataService = new CovidDataService(new CovidDataDao());
	}

	@AfterAll
	static void tearDown() {
		covidDataService = null;
	}

	@Test
	@DisplayName("Get State Names")
	void stateNames() {
		Set<String> statesName = covidDataService.getStateNames();
		assertEquals(statesName.size(), 35);
		assertTrue(statesName.containsAll(Arrays.asList("JK", "HP", "DL", "PY", "HR", "DN", "WB", "BR", "KA", "SK",
				"GA", "UP", "MH", "UT", "ML", "MN", "KL", "OR", "MP", "GJ", "CH", "MZ", "AN", "AP", "CT", "AR", "PB",
				"AS", "TG", "LA", "RJ", "TN", "TR", "NL", "JH")));
	}

	@Test
	@DisplayName("Get District Names By State Name")
	void districtsByStateName() throws InvalidStateCodeException {

		Set<String> districtNamesUnderAState = covidDataService.getDistrictNamesUnderAState("PB");
		assertTrue(districtNamesUnderAState.size() > 0);
		assertTrue(districtNamesUnderAState
				.containsAll(Arrays.asList("Ludhiana", "Amritsar", "Hoshiarpur", "Barnala", "Bathinda")));

	}

	@Test()
	@DisplayName("If state Code is invalid throw InvalidStateCodeException")
	void districtsByStateNameException() {

		InvalidStateCodeException assertThrows2 = assertThrows(InvalidStateCodeException.class, () -> {
			covidDataService.getDistrictNamesUnderAState("P");
		});

		assertEquals("Invalid State Code Please Check Your input!!", assertThrows2.getMessage());
	}

	@Test
	@DisplayName("Get Confirmed Cases between Given Date Range")
	void confirmedCases() throws NoDataFoundException, InvalidDateException, InvalidDateRangeException {

		TreeMap<LocalDate, Map<String, IntSummaryStatistics>> dataBySatesWithInDateRange = covidDataService
				.getDataBySatesWithInDateRange(LocalDate.of(2020, Month.JUNE, 1), LocalDate.of(2020, Month.AUGUST, 1));

		assertTrue(dataBySatesWithInDateRange.size() > 0);

		assertEquals(dataBySatesWithInDateRange.get(LocalDate.of(2020, 7, 7)).get("UP").getSum(), 1332);
	}

	@Test
	@DisplayName("If given date is invalid throw InvalidDateException")
	void confirmedCasesInvalidDateException()
			throws NoDataFoundException, InvalidDateException, InvalidDateRangeException {

		assertThrows(InvalidDateException.class, () -> {
			covidDataService.getDataBySatesWithInDateRange(LocalDate.of(2019, Month.AUGUST, 1),
					LocalDate.of(2022, Month.JUNE, 1));
		});
	}

	@Test
	@DisplayName("If given date range is invalid throw InvalidDateRangeException")
	void confirmedCasesInvalidDateRangeException()
			throws NoDataFoundException, InvalidDateException, InvalidDateRangeException {

		assertThrows(InvalidDateRangeException.class, () -> {
			covidDataService.getDataBySatesWithInDateRange(LocalDate.of(2020, Month.AUGUST, 1),
					LocalDate.of(2020, Month.JUNE, 1));
		});
	}

	@Test
	@DisplayName("If No data is avilable between given date range throw NoDataFoundException")
	void confirmedCasesNoDataFoundException()
			throws NoDataFoundException, InvalidDateException, InvalidDateRangeException {

		assertThrows(InvalidDateRangeException.class, () -> {
			covidDataService.getDataBySatesWithInDateRange(LocalDate.of(2020, Month.AUGUST, 1),
					LocalDate.of(2020, Month.AUGUST, 1));
		});
	}

	@Test
	@DisplayName("Get Confirmed Cases between Given Date Range and between two states")
	void confirmedCasesbetweenTwoStates() throws NoDataFoundException, InvalidDateException, InvalidDateRangeException, InvalidDateException, InvalidStateCodeException {

		TreeMap<LocalDate, Map<String, IntSummaryStatistics>> confirmedCasesByComparingTwoStatesData = covidDataService
				.getConfirmedCasesByComparingTwoStatesData(LocalDate.of(2020, 8, 4), LocalDate.of(2020, 8, 7), "PB",
						"CH");
		assertTrue(confirmedCasesByComparingTwoStatesData.size() > 0);
		assertEquals(confirmedCasesByComparingTwoStatesData.get(LocalDate.of(2020, 8, 6)).get("PB").getSum(), 1035);
		assertEquals(confirmedCasesByComparingTwoStatesData.get(LocalDate.of(2020, 8, 6)).get("CH").getSum(), 57);
	}

	@Test
	@DisplayName("If given date is invalid for confirmedCasesbetweenTwoStates throw InvalidDateException")
	void confirmedCasesbetweenTwoStatesInvalidDateException() throws InvalidDateException {

		assertThrows(InvalidDateException.class, () -> {
			covidDataService.getConfirmedCasesByComparingTwoStatesData(LocalDate.of(2019, 8, 4),
					LocalDate.of(2020, 8, 7), "PB", "CH");
		});
	}

	@Test()
	@DisplayName("If state Code is invalid for confirmedCasesbetweenTwoStates throw InvalidStateCodeException")
	void confirmedCasesbetweenTwoStatesInvalidStateCodeException() {

		InvalidStateCodeException assertThrows2 = assertThrows(InvalidStateCodeException.class, () -> {
			covidDataService.getConfirmedCasesByComparingTwoStatesData(LocalDate.of(2020, 8, 4),
					LocalDate.of(2020, 8, 7), "P", "C");
		});

		assertEquals("Invalid State code, please check your input", assertThrows2.getMessage());
	}

	@Test
	@DisplayName("If given date range is invalid for confirmedCasesbetweenTwoStates throw InvalidDateRangeException")
	void confirmedCasesbetweenTwoStatesInvalidDateRangeException() throws InvalidDateRangeException {

		assertThrows(InvalidDateRangeException.class, () -> {
			covidDataService.getConfirmedCasesByComparingTwoStatesData(LocalDate.of(2020, 8, 8),
					LocalDate.of(2020, 8, 4), "PB", "CH");
		});
	}

	@Test
	@DisplayName("If No data is avilable for confirmedCasesbetweenTwoStates given date range then throw NoDataFoundException")
	void confirmedCasesbetweenTwoStatesNoDataFoundException() throws NoDataFoundException {

		assertThrows(InvalidDateRangeException.class, () -> {
			covidDataService.getConfirmedCasesByComparingTwoStatesData(LocalDate.of(2020, 8, 4),
					LocalDate.of(2020, 8, 4), "PB", "CH");
		});
	}
}
