package DBDatabase;
// file to "help" user use sql commands to insert, update, delete data from sql using java

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

import model.Country;
import model.Customer;

/**this class connects to JDBC and access the 'appointments' table*/
public abstract class DAOcustomers {
    private static int currentCustomerID = 0;
    private static Timestamp createDate;
    private static String createdBy;

    /**this method makes a connection to db, creates an observable list for customers, will get all customers from
     * the table, then will return the table
     * @return list This will return a list of customers to the customerController and will be displayed for the user to see
     * @throws SQLException this is the sql exception*/
    public static ObservableList<Customer> getDataCustomer() throws SQLException {
        Connection conn = JDBC.openConnection();
        ObservableList<Customer> list = FXCollections.observableArrayList();

        String sql = "SELECT * FROM customers";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Customer(rs.getInt("Customer_ID"),
                        rs.getString("Customer_Name"),
                        rs.getString("Address"),
                        rs.getString("Postal_Code"),
                        rs.getString("Phone"),
                        rs.getInt("division_ID")));

                currentCustomerID = rs.getInt("Customer_ID");
            }

            return list;
        } catch (Exception e) {

        }
        return null;
    }
    /** this method will get data entered by the user and insert it into the customer table
     * @param customerName this is the customer's name
     * @param customerAddress this is the customer's address
     * @param postalCode this is the customer's postal code
     * @param customerPhone this is the customer's phone number
     * @param divisionID this is the division identification number that represents the division the customer
     *                   resides in.
     * @return rowsaffected this will return the rows that were affected
     * @throws SQLException this is the sql exception*/
    public static int insert(String customerName, String customerAddress, String postalCode,
                             String customerPhone, Integer divisionID) throws SQLException {
        String sql = "INSERT INTO customers (" +
                "Customer_ID, " +
                "Customer_Name, " +
                "Address, " +
                "Postal_Code, " +
                "Phone, " +
                "Create_Date, " +
                "Created_By, " +
                "Last_Update, " +
                "Last_Updated_By, " +
                "Division_ID) VALUES(null, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);

            ps.setString(1, customerName);
            ps.setString(2, customerAddress);
            ps.setString(3, postalCode);
            ps.setString(4, customerPhone);
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(6, "admin");
            ps.setString(7, "admin");
            ps.setInt(8, divisionID);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected;
        } catch (Exception e) {

        }
        return 0;
    }
/** this method will update an appointment after the user selects an customer row from the customerController
 * @param customerName this is the customer's name
 * @param customerAddress this is the customer's address
 * @param postalCode this is the customer's postal code
 * @param customerPhone this is the customer's phone number
 * @param country this is the country where the customer resides in
 * @param divisionID this is the division identification number that represents the division the customer
 *                   resides in.
 * @param customerID this is the customer's identification number
 *                   @throws SQLException this is the sql exception
*/
    public static void update(Integer customerID, String customerName, String customerAddress, String postalCode,
                              String customerPhone, Country country, Integer divisionID) throws SQLException {
        String sqlString = "SELECT * FROM customers WHERE Customer_ID = ?";
        PreparedStatement psFirst = JDBC.connection.prepareStatement(sqlString);
        psFirst.setInt(1, customerID);
        ResultSet getRS = psFirst.executeQuery();

        while (getRS.next()) {
            createDate = getRS.getTimestamp("Create_Date");
            createdBy = getRS.getString("Created_By");
        }

        String sql = "UPDATE customers SET " +
                "Customer_Name = ?," +
                "Address = ?," +
                "Postal_Code = ?," +
                "Phone = ?, " +
                "Last_Update = ?," +
                "Division_ID = ? WHERE Customer_ID = ?";

        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);

            ps.setString(1, customerName);
            ps.setString(2, customerAddress);
            ps.setString(3, postalCode);
            ps.setString(4, customerPhone);

            Timestamp lastUpdate = Timestamp.valueOf(LocalDateTime.now());
            ps.setTimestamp(5, lastUpdate);

            ps.setInt(6, divisionID);
            ps.setInt(7, customerID);

            ps.executeUpdate();
        }
        catch (Exception e) {

        }
    }

    /**this method will delete the selected customer from the customer table in customerController
     * @param customerID this number will be used to find the customer in the customer table in db and delete it
     * @throws SQLException this is the sql exception
     * @return this will return the rows affected*/
    public static int delete(int customerID) throws SQLException {
        String sql = "DELETE FROM customers WHERE customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerID);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

/** method will output all rows from the customer table
 * @throws SQLException this is the sql exception*/
    public static void select() throws SQLException {
        String sql = "SELECT * FROM customers";

        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int customerID = rs.getInt("customer_ID");
                String customerName = rs.getString("Customer_Name");
                System.out.print(customerID + ": " + customerName + "\n");
        }
        }
        catch (Exception e) {

        }
    }
    /** this method will get the total number of a specific division and return the number
     * @param divisionID this will be used to find the specific division and the total number of
     * @return the total number of a specific divsion will be returned
     * @throws SQLException this is the sql exception*/
    public static int getDivisionCount(Integer divisionID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM customers WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, divisionID);

        try {
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                return rs.getInt("COUNT(*)");

            }
        } catch (Exception e) {

        }


        return 0;
    }

}
