package covid.analysis.service;

import java.time.LocalDate;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import covid.analysis.dao.CovidDataDao;
import covid.analysis.entity.CovidData;
import covid.analysis.exception.InvalidDateException;
import covid.analysis.exception.InvalidDateRangeException;
import covid.analysis.exception.InvalidStateCodeException;
import covid.analysis.exception.NoDataFoundException;

public class CovidDataService {

	private CovidDataDao covidDataDao;
	private List<CovidData> covidData;

	public CovidDataService(CovidDataDao covidDataDao) {
		this.covidDataDao = covidDataDao;
		this.covidData = this.covidDataDao.getCovidData();
	}

	public Set<String> getStateNames() {
		Map<String, List<CovidData>> filteredList = null;

//			retrieving all states from covidData List then apply grouping to get distinct states
		filteredList = this.covidData.stream().collect(Collectors.groupingBy(CovidData::getState));

		return filteredList.keySet();

	}

	public Set<String> getDistrictNamesUnderAState(String stateName) throws InvalidStateCodeException {

//			condition to check state equals or not
		Predicate<CovidData> predicate = cd -> cd.getState().equals(stateName);

//			checking whether the given state code exist in out database or not
		boolean anyMatch = this.covidData.stream().anyMatch(predicate);

		Set<String> collect = null;
		if (!anyMatch) {
			throw new InvalidStateCodeException("Invalid State Code Please Check Your input!!");
		}
//				retrieving district data which is equal to given state 
		collect = this.covidData.stream().filter(predicate).map(cv -> cv.getDistrict()).collect(Collectors.toSet());

		return collect;
	}

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @return Map
	 * @throws NoDataFoundException
	 * @throws InvalidDateException
	 * @throws InvalidDateRangeException
	 */
	public TreeMap<LocalDate, Map<String, IntSummaryStatistics>> getDataBySatesWithInDateRange(LocalDate startDate,
			LocalDate endDate) throws NoDataFoundException, InvalidDateException, InvalidDateRangeException {

		Map<LocalDate, Map<String, IntSummaryStatistics>> collect3 = null;

//			condition's to check whether given date is valid or not
		if (!this.covidData.stream().anyMatch(cd -> cd.getDate().isEqual(startDate))) {

			throw new InvalidDateException("Invalid End Date Please Check your input.");
		}

		else if (!this.covidData.stream().anyMatch(cd -> cd.getDate().isEqual(endDate))) {
			throw new InvalidDateException("Invalid Start Date Please Check your input.");
		}
//			condition to check end date is not before start date
		else if (!startDate.isBefore(endDate)) {
			throw new InvalidDateRangeException("Invalid Date Range, check Your Input.");
		}

		else {
//							predicate condition to filter Covid data between given date range
			Predicate<CovidData> predicate = cd -> cd.getDate().isAfter(startDate) && cd.getDate().isBefore(endDate);

//							applying condition retrieve all data between given date range 
			List<CovidData> collect = this.covidData.stream().filter(predicate).collect(Collectors.toList());

//							condition to check if list is empty then throw "No data Present Exception"
			if (collect.isEmpty()) {
				throw new NoDataFoundException("No Data Present.");
			} else {
//							grouping the list of elements on the basis of date and state then summing the total number of confirmed cases
				collect3 = collect.stream().collect(Collectors.groupingBy(CovidData::getDate, Collectors
						.groupingBy(CovidData::getState, Collectors.summarizingInt(CovidData::getConfirmed))));
			}

		}
//		storing HashMap elements into TreeMap to get the sorted Map
		return new TreeMap<LocalDate, Map<String, IntSummaryStatistics>>(collect3);
	}

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param fState
	 * @param sState
	 * @return Map
	 * @throws NoDataFoundException 
	 * @throws InvalidDateException 
	 * @throws InvalidDateRangeException 
	 * @throws InvalidStateCodeException 
	 */
	public TreeMap<LocalDate, Map<String, IntSummaryStatistics>> getConfirmedCasesByComparingTwoStatesData(
			LocalDate startDate, LocalDate endDate, String fState, String sState) throws NoDataFoundException, InvalidDateException, InvalidDateRangeException, InvalidStateCodeException {

		Map<LocalDate, Map<String, IntSummaryStatistics>> collect3 = null;
//		condition's to check whether given date is valid or not
		if (!this.covidData.stream().anyMatch(cd -> cd.getDate().isEqual(startDate))) {

			throw new InvalidDateException("Invalid End Date Please Check your input.");
		}

		else if (!this.covidData.stream().anyMatch(cd -> cd.getDate().isEqual(endDate))) {
			throw new InvalidDateException("Invalid Start Date Please Check your input.");
		}
//		condition to check end date is not before start date
		else if (!startDate.isBefore(endDate)) {
			throw new InvalidDateRangeException("Invalid Date Range, check Your Input.");
		}

		else {
//							condition to check state equals or not
			Predicate<CovidData> predicate = cd -> cd.getState().equals(fState);
			Predicate<CovidData> predicate1 = cd -> cd.getState().equals(sState);
//							checking whether the given state code exist in out database or not
			boolean fStateMatch = this.covidData.stream().anyMatch(predicate);
			boolean sStateMatch = this.covidData.stream().anyMatch(predicate1);

			if (!fStateMatch) {
				throw new InvalidStateCodeException("Invalid State code, please check your input");
			} else if (!sStateMatch) {
				throw new InvalidStateCodeException("Invalid State code, please check your input");
			}
//						predicate condition to filter data between given date range
			Predicate<CovidData> datePredicate = cd -> cd.getDate().isAfter(startDate)
					&& cd.getDate().isBefore(endDate);

//						predicate condition to filter data which matches given two state's
			Predicate<CovidData> statePredicate = cd -> cd.getState().equals(fState) || cd.getState().equals(sState);

//						applying filters on list of Covid data to retrieve a collection of data between given date range and given state
			List<CovidData> collect = this.covidData.stream().filter(datePredicate.and(statePredicate))
					.collect(Collectors.toList());

//						condition to check if list is empty then throw "No data Present Exception"
			if (collect.isEmpty()) {
				throw new NoDataFoundException("No Data Present.");
			} else {
//						grouping the list of elements on the basis of date and state then summing the total number of confirmed cases
				collect3 = collect.stream().collect(Collectors.groupingBy(CovidData::getDate, Collectors
						.groupingBy(CovidData::getState, Collectors.summarizingInt(CovidData::getConfirmed))));

			}

		}
//						storing HashMap elements into TreeMap to get the sorted Map
		return new TreeMap<LocalDate, Map<String, IntSummaryStatistics>>(collect3);

	}
}
