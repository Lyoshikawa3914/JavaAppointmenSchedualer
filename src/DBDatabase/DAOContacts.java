package DBDatabase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contacts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**this method connects to JDBC and access the 'contacts' table
 */
public class DAOContacts {
    private static int currentContactID = 0;

    /** this method will get the contact data from the database
     *@throws SQLException this is the sql exception
     * @return this will return the list, else it will return null*/
    public static ObservableList<Contacts> getDataContacts() throws SQLException {
        Connection conn = JDBC.openConnection();
        ObservableList<Contacts> list = FXCollections.observableArrayList();

        String sql = "SELECT * FROM contacts";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Contacts(rs.getInt("Contact_ID"),
                        rs.getString("Contact_Name"),
                        rs.getString("Email")));

                currentContactID = rs.getInt("Contact_ID");
            }

            return list;
        } catch (Exception e) {
            return null;
        }

    }


}
