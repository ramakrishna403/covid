package covid.analysis.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import covid.analysis.entity.CovidData;

public class CovidDataDao {

	public List<CovidData> getCovidData() {

		Connection con = CreateOrCloseConnection.getConnection();
		PreparedStatement statement = null;
		String sql = "Select * from covid_data; ";
		List<CovidData> covidData = new ArrayList<CovidData>();
		try {

			statement = con.prepareStatement(sql);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				CovidData covid = new CovidData();
				covid.setId(result.getInt("id"));
				covid.setState(result.getString("state"));
				covid.setDistrict(result.getString("district"));
				covid.setConfirmed(result.getInt("confirmed"));
				covid.setTested(result.getInt("tested"));
				covid.setRecovered(result.getInt("recovered"));
				covid.setDate(LocalDate.parse(result.getDate("date").toString()));

				covidData.add(covid);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null)
					CreateOrCloseConnection.closeConnection(con);
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return covidData;
	}

}
