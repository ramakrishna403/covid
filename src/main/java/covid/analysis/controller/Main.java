package covid.analysis.controller;

import java.time.LocalDate;
import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import covid.analysis.dao.CovidDataDao;
import covid.analysis.exception.InvalidDateException;
import covid.analysis.exception.InvalidDateRangeException; 
import covid.analysis.exception.InvalidStateCodeException;
import covid.analysis.exception.NoDataFoundException;
import covid.analysis.service.CovidDataService;


public class Main {

	private static final Scanner scanner = new Scanner(System.in);
	private static final CovidDataService service = new CovidDataService(new CovidDataDao());

	public static void main(String[] args)
			throws InvalidStateCodeException, NoDataFoundException, InvalidDateException, InvalidDateRangeException {

		int choice = 0;

		while (choice != 5) {

			System.out.println("*******************************");
			System.out.println("1. Get State Names");
			System.out.println("2. Get District for given state");
			System.out.println("3. Display Data by State within Date Range");
			System.out.println("4. Display Confirmed Cases by Comparing two States for a Given Date Range");
			System.out.println("5. Exit");

			System.out.print("Please select option : ");
			choice = scanner.nextInt();

			switch (choice) {
			case 1:
//				calling GetStateMethod to Display all States
				GetStateName();
				break;
			case 2:
				System.out.print("Please enter state code : ");
				String state = scanner.next().toUpperCase();
//				calling getDistrictName method to display district under a given specific state.
				GetDistrictName(state);
				break;
			case 3:
				System.out.print("Please enter start date (yyyy-MM-dd) : ");
				LocalDate startDate = LocalDate.parse(scanner.next());
				System.out.print("Please enter end date (yyyy-MM-dd) : ");
				LocalDate endDate = LocalDate.parse(scanner.next());
//				calling diaplyDataByStateWithInDateRange to display total Confirmed cases of any all states comes
//				under given date range.
				diaplyDataByStateWithInDateRange(startDate, endDate);
				break;
			case 4:
				System.out.print("Please enter start date (yyyy-MM-dd) : ");
				LocalDate sDate = LocalDate.parse(scanner.next());
				System.out.print("Please enter end date (yyyy-MM-dd) : ");
				LocalDate eDate = LocalDate.parse(scanner.next());
				System.out.print("Please Enter First State code : ");
				String firstSc = scanner.next().toUpperCase();
				System.out.print("Please Enter Second State code : ");
				String secondSc = scanner.next().toUpperCase();
//				calling confirmedCasesByComparingTwoStates to display total Confirmed cases between two states comes
//				under given date range.
				confirmedCasesByComparingTwoStates(sDate, eDate, firstSc, secondSc);
				break;
			case 5:
				System.out.println("Exit");
				break;
			default:
				System.out.println("Please choose appropriate option given between 1 to 5.");

			}

		}

	}


	private static void GetStateName() {
		Set<String> stateNames = service.getStateNames();
		stateNames.forEach(System.out::println);
	}


	private static void GetDistrictName(String state) throws InvalidStateCodeException {
		Set<String> districtNamesUnderAState = service.getDistrictNamesUnderAState(state);
		districtNamesUnderAState.stream().sorted((s1, s2) -> s1.compareTo(s2)).forEach(System.out::println);
	}

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * 
	 *                  diaplyDataByStateWithInDateRange method can start and end
	 *                  date as parameter. Display Date, State and Confirmed Cases
	 *                  data between given date range
	 * @throws InvalidDateRangeException
	 * @throws InvalidDateException
	 * @throws NoDataFoundException
	 * 
	 * 
	 */
	public static void diaplyDataByStateWithInDateRange(LocalDate startDate, LocalDate endDate)
			throws NoDataFoundException, InvalidDateException, InvalidDateRangeException {
		TreeMap<LocalDate, Map<String, IntSummaryStatistics>> withInDateRange = service
				.getDataBySatesWithInDateRange(startDate, endDate);
		System.out.println("      Date| State| Confirmed total");
		for (Entry<LocalDate, Map<String, IntSummaryStatistics>> entry : withInDateRange.entrySet()) {
			entry.getValue().forEach((k, v) -> {
				System.out.println(entry.getKey() + "|    " + k + "|       " + v.getSum());
			});
		}
	}

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param fState
	 * @param sState
	 * 
	 *                  confirmedCasesByComparingTwoStates take startDate, endDate,
	 *                  firstState, Second State as parameter Display comparison
	 *                  confirmed cases between two state within given date range
	 * @throws InvalidStateCodeException
	 * @throws InvalidDateRangeException
	 * @throws InvalidDateException
	 * @throws NoDataFoundException
	 * 
	 * 
	 * 
	 */
	public static void confirmedCasesByComparingTwoStates(LocalDate startDate, LocalDate endDate, String fState,
			String sState)
			throws NoDataFoundException, InvalidDateException, InvalidDateRangeException, InvalidStateCodeException {
		TreeMap<LocalDate, Map<String, IntSummaryStatistics>> comparingTwoStatesData = service
				.getConfirmedCasesByComparingTwoStatesData(startDate, endDate, fState, sState);
		System.out.println(
				"DATE       |      FIRST STATE     |     FIRST STATE CONFIRMED TOTAL     |     SECOND STATE     |    SECOND STATE CONFIRMED TOTAL	");
		for (Entry<LocalDate, Map<String, IntSummaryStatistics>> entry : comparingTwoStatesData.entrySet()) {

			System.out.println(entry.getKey() + " |          " + fState + "          |                 "
					+ entry.getValue().get(fState).getSum() + "                 |          " + sState
					+ "          |               " + entry.getValue().get(sState).getSum());
		}
	}

}
