package DBDatabase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FirstLevelDivisions;
import model.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** this method will connect to JDBC and get all rows from users*/
public class DAOUsers {
    /** this is the method that will get the user data from the database
     * @throws SQLException this is the sql exception
     * @return this will return the user list*/
    public static ObservableList<Users> getDataUsers() throws SQLException {
        Connection conn = JDBC.openConnection();
        ObservableList<Users> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM users";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Users(rs.getInt("User_ID"),
                        rs.getString("User_Name"),
                        rs.getString("Password")));
            }

        }catch (Exception e) {

        }
        return list;
    }
/** this method will check if the username and password are valid from the 'loginController'
 * @param password this is the password string used to check the user credentials
 * @param username this is the username string used to check the user credentials
 * @return  this method will return either true or false
 * @throws SQLException this is the sql exception*/
    public static boolean checkUserValidation(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setString(1, username);
        ps.setString(2, password);

        try {
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {

        }
        return false;
    }
}
