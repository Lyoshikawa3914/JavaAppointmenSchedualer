package DBDatabase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FirstLevelDivisions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**this class connects to JDBC and access the 'appointments' table*/
public class DAOFirstLevelDivions {

    /** this method will get all division rows from the division table
     * @return list this will return a list of all of the divisions
     * @throws SQLException this is the sql exception*/
    public static ObservableList<FirstLevelDivisions> getDataDivision() throws SQLException {
        Connection conn = JDBC.openConnection();
        ObservableList<FirstLevelDivisions> list = FXCollections.observableArrayList();

        String sql = "SELECT * FROM first_level_divisions";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new FirstLevelDivisions(rs.getInt("Division_ID"),
                        rs.getString("Division"),
                        rs.getInt("COUNTRY_ID")));
            }
            return list;
        } catch (Exception e) {

        }
        return null;
    }

    /** this method will get the division name
     * @param divisionID this is the divion identification number that will be used to get the
     * division from the division table from db
     @throws SQLException this is the sql exception
     * @return "" will return an empty string if nothing is found from table*/
    public static String getDivisionName(int divisionID) throws SQLException {
        //ObservableList<FirstLevelDivisions> list = FXCollections.observableArrayList();

        String sql = "SELECT Division FROM first_level_divisions WHERE Division_ID = ?";

        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, divisionID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                return rs.getString("Division");
            }
        } catch(Exception e) {

        }

        return "";
    }
}
