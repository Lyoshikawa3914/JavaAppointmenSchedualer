package DBDatabase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * this class connects to JDBC and access the 'countries' table
 */
public class DAOCountries {
    /** this method will get all countries when it is called
     * @return list this will return a list of countries
     * @throws SQLException this is the sql exception*/
    public static ObservableList<Country> getAllCountries() throws SQLException {
        Connection conn = JDBC.openConnection();
        ObservableList<Country> list = FXCollections.observableArrayList();

        String sql = "SELECT * FROM countries";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Country(rs.getInt("Country_ID"),
                        rs.getString("Country")));
            }

            return list;
        } catch(Exception e) {

        }
        return null;
    }

    /**this method will get the division from the country table
     * @throws SQLException this is the sql exception
     * @return this will return null
     * @param divisionID this is the division id*/
    public static Country getCountryByDivision(int divisionID) throws SQLException {
        Connection conn = JDBC.openConnection();

        String sql = "SELECT c.* FROM countries as C inner join first_level_divisions as D on D.country_ID = C.country_ID " +
                "and D.division_ID = ?";


        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, divisionID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new Country(rs.getInt("Country_ID"),
                        rs.getString("Country"));
            }
        } catch(Exception e) {

        }
        return null;
    }
/**this method will select all countries from the country table
 * @param countryID this is the country id
 * @throws SQLException this is the sql exception
 * @return this will return the country object, else it will return null*/
    public static Country getCountry(int countryID) throws SQLException {
        Connection conn = JDBC.openConnection();

        String sql = "SELECT * FROM countries WHERE country_ID = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, countryID);

        try {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                return new Country(rs.getInt("Country_ID"),
                        rs.getString("Country"));
            }
        } catch (Exception e) {

        }
        return null;
    }


}
