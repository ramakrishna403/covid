package covid.analysis.entity;

import java.time.LocalDate;

public class CovidData {

	private int id;
	private LocalDate date;
	private String state;
	private String district;
	private int tested;
	private int confirmed;
	private int recovered;

	public CovidData() {
		super();
	}

	public CovidData(int id, LocalDate date, String state, String district, int tested, int confirmed, int recovered) {
		super();
		this.id = id;
		this.date = date;
		this.state = state;
		this.district = district;
		this.tested = tested;
		this.confirmed = confirmed;
		this.recovered = recovered;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public int getTested() {
		return tested;
	}

	public void setTested(int tested) {
		this.tested = tested;
	}

	public int getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(int confirmed) {
		this.confirmed = confirmed;
	}

	public int getRecovered() {
		return recovered;
	}

	public void setRecovered(int recovered) {
		this.recovered = recovered;
	}

	@Override
	public String toString() {
		return "CovidData [id=" + id + ", date=" + date + ", state=" + state + ", district=" + district + ", tested="
				+ tested + ", confirmed=" + confirmed + ", recovered=" + recovered + "]";
	}

	

}
