package DBDatabase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Contacts;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**this method connects to JDBC and access the 'appointments' table*/
public class DAOappointments {
    private static final ZoneId localZoneID = ZoneId.systemDefault();
    private static int currentAppointmentID = 0;
    private static Timestamp createDate;
    private static String createdBy;


    /**this method makes a connection to db, creates an observable list for appointments, will get all appointments from
     * the table, then will return the table
     * @return list This will return a list of appointments to the appointmentController and will be displayed for the user to see
     * @throws SQLException this is the sql exception*/
    public static ObservableList<Appointment> getDataAppointment() throws SQLException {
//        Connection conn = JDBC.openConnection();
        ObservableList<Appointment> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments";

        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
//        PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Timestamp toStart = rs.getTimestamp("Start");
                Timestamp toEnd = rs.getTimestamp("End");
                LocalDateTime start = toStart.toLocalDateTime();
                LocalDateTime end = toEnd.toLocalDateTime();

                list.add(new Appointment(rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        start,
                        end,
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID")));
            }
        }
        catch (Exception e){

        }
        return list;
    }

/** this method will get data entered by the user and insert it into the appointment table
 *

 * @param title this is the title of the appointment
 * @param description this is a description of the kind of appointment
 * @param location this is the location of the appointment
 * @param type this is the type of appointment
 * @param start this is the start time and date of the appointment
 * @param end this is the end time and date of the appointment
 * @param customerID this is the customer identification number
 * @param userID this is the user identification number that identifies who the user is
 * @param contactID this is the customers' contact identification number that can be used to get the customers contact
 *                  from the contact table
 * @return rowsaffected this will return the rows that were affected
 * @throws SQLException this is the sql exception*/
    public static int insert(String title, String description, String location,
                             String type, LocalDateTime start, LocalDateTime end,
                             Integer customerID, Integer userID, Integer contactID) throws SQLException {

        String sql = "INSERT INTO appointments (" +
                "Appointment_ID, " +
                "Title, " +
                "Description, " +
                "Location," +
                " Type, " +
                "Start, " +
                "End, " +
                "Create_Date, " +
                "Created_By," +
                "Last_Update, " +
                "Last_Updated_By," +
                " Customer_ID, " +
                "User_ID, " +
                "Contact_ID) VALUES(null, ?, ?, ?, ?, ?, ?, now(), ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);

        Timestamp startldt = Timestamp.valueOf(start);
        ps.setTimestamp(5, startldt);

        Timestamp endldt = Timestamp.valueOf(end);
        ps.setTimestamp(6, endldt);

        ps.setString(7, "test");
        ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(9, "test");
        ps.setInt(10, customerID);
        ps.setInt(11, userID);
        ps.setInt(12, contactID);

        try {
            int rowsAffected = ps.executeUpdate();
            return rowsAffected;
        }
        catch (Exception e) {

        }
        return 0;
    }
/** this method will update an appointment after the user selects an appointment row from the appointmentController
 * @param customerID this is the customer identification number
 *  @param contactID this is the customers' contact identification number that can be used to get the customers contact
 *  *                  from the contact table
 *  @param description this is a description of the kind of appointment
 *  @param end this is the end time and date of the appointment
 *  @param start this is the start time and date of the appointment
 *  @param location this is the location of the appointment
 *  @param title this is the title of the appointment
 *  @param type this is the type of appointment
 *  @param userID this is the user identification number that identifies who the user is
 *  @param appointment this is the appointment identificaiton number that will help identify what appointment is being updated
 *  and will impliment those changes
 *  @throws SQLException this is the sql exception*/
    public static void update(String title, String description, String location,
                             String type, LocalDateTime start, LocalDateTime end,
                             Integer customerID, Integer userID, Integer contactID, Integer appointment) throws SQLException {

        String sqlFirst = "SELECT * FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement psFirst = JDBC.connection.prepareStatement(sqlFirst);
        psFirst.setInt(1, appointment);
        ResultSet getRS = psFirst.executeQuery();

        while (getRS.next()) {
            createDate = getRS.getTimestamp("Create_Date");
            createdBy = getRS.getString("Created_By");
        }

        String sql = "UPDATE appointments SET " +
                "Title = ?," +
                "Description = ?," +
                "Location = ?," +
                "Type = ?," +
                "Start = ?," +
                "End = ?, " +
                " Customer_ID = ?, " +
                "User_ID = ?, " +
                "Contact_ID = ?" +
                " WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);

        Timestamp startldt = Timestamp.valueOf(start);
        ps.setTimestamp(5, startldt);

        Timestamp endldt = Timestamp.valueOf(end);
        ps.setTimestamp(6, endldt);

        ps.setInt(7, customerID);
        ps.setInt(8, userID);
        ps.setInt(9, contactID);
        ps.setInt(10, appointment);

        try {
            ps.executeUpdate();
        }
        catch(Exception e) {

        }

    }
/**this method will delete the selected appointment from the appointment table in appointmentController
 * @param appointmentID this number will be used to find the appointment in the appointment table in db and delete it
 * @throws SQLException this is the sql exception
 * @return this will return the rows affected*/
    public static int delete(int appointmentID) throws SQLException {
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setInt(1, appointmentID);


        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
/**this method will occur when a user wishes to delete a customer from the customer table
 * this method will delete any appointments that are associated with the deleted customer
 * @param customerID this will be used to find appointments that have the customer id
 * @return this will return the rows affected, else will return null
 * @throws SQLException this is the sql exception*/
    public static int deleteCustomerAppointment(int customerID) throws SQLException {
        String sql = "DELETE FROM appointments WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setInt(1, customerID);
        try {
            int rowsAffected = ps.executeUpdate();
            return rowsAffected;
        }
        catch (Exception e) {

        }
        return 0;
    }
/** this method will get all rows with a specific customerID and return all the rows
 * @param customerID this is the customer identification number
 * @return this will return an observable list of appointments or will return null if there are no appointments
 * @throws SQLException this is the sql exception*/
    public static ObservableList<Appointment> getAppointmentsByCustomerID(int customerID) throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

//        String sql = "SELECT * FROM appointments AS A INNER JOIN contacts AS C ON A.Contact_ID = C.Contact_ID WHERE Customer_ID=?";
        String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setInt(1, customerID);

        try {
            ps.execute();
            ResultSet rs = ps.getResultSet();

            // Forward scroll resultSet
            while (rs.next()) {
                Appointment newAppointment = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getTimestamp("Start").toLocalDateTime(),
                        rs.getTimestamp("End").toLocalDateTime(),
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID")
                );

                appointments.add(newAppointment);
            }
            return appointments;
        } catch (Exception e) {
            return null;
        }
    }

//    /** this method will get all rows with a specific contactID and return all the rows
//     * @param contactID this is the customer identification number
//     * @return this will return an observable list of appointments or will return null if there are no appointments*/
//    public static ObservableList<Appointment> getAppointmentsByContactID(int contactID) throws SQLException {
//        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
//
////        String sql = "SELECT * FROM appointments AS A INNER JOIN contacts AS C ON A.Contact_ID = C.Contact_ID WHERE Customer_ID=?";
//        String sql = "SELECT * FROM appointments WHERE Contact_ID = ?";
//
//        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
//
//        ps.setInt(1, contactID);
//
//        try {
//            ps.execute();
//            ResultSet rs = ps.getResultSet();
//
//            // Forward scroll resultSet
//            while (rs.next()) {
//                Appointment newAppointment = new Appointment(
//                        rs.getInt("Appointment_ID"),
//                        rs.getString("Title"),
//                        rs.getString("Description"),
//                        rs.getString("Location"),
//                        rs.getString("Type"),
//                        rs.getTimestamp("Start").toLocalDateTime(),
//                        rs.getTimestamp("End").toLocalDateTime(),
//                        rs.getInt("Customer_ID"),
//                        rs.getInt("User_ID"),
//                        rs.getInt("Contact_ID")
//                );
//
//                appointments.add(newAppointment);
//            }
//            return appointments;
//        } catch (Exception e) {
//            return null;
//        }
//    }


/** this method will get the appointment associated with a user.
 * @param userid this is the user identification number
 * @return list this will return a list of appointments associated with the user
 * @throws SQLException this will sql exception*/
    public static ObservableList<Appointment> getUserAppointments(int userid) throws SQLException {
        ObservableList<Appointment> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setInt(1, userid);

        try {
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Timestamp toStart = rs.getTimestamp("Start");
                Timestamp toEnd = rs.getTimestamp("End");
                LocalDateTime start = toStart.toLocalDateTime();
                LocalDateTime end = toEnd.toLocalDateTime();

                list.add(new Appointment(rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        start,
                        end,
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID")));
            }
            return list;
        } catch (Exception e) {

        }
        return null;
    }
    /** this method will filter appointments that are happening this week.
     * @return list this will return the filtered list
     * @throws SQLException this will throw an sql exception*/
    public static ObservableList<Appointment> filterWeekAppointments() throws SQLException {
        ObservableList<Appointment> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments WHERE WEEK(Start) = WEEK(CURRENT_DATE())";
//        String sql = "SELECT * FROM appointments WHERE Start < CURRENT_DATE() + INTERVAL 7 day AND Start > CURRENT_DATE()";


        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();


            while (rs.next()) {
                Timestamp toStart = rs.getTimestamp("Start");
                Timestamp toEnd = rs.getTimestamp("End");
                LocalDateTime start = toStart.toLocalDateTime();
                LocalDateTime end = toEnd.toLocalDateTime();
                list.add(new Appointment(rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        start,
                        end,
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID")));
            }
            return list;
        }
        catch (Exception e) {
            return null;
        }

    }
    /** this method will filter appointments that are happening this month.
     * @return list this will return the filtered list
     * @throws SQLException this is the sql exception*/
        public static ObservableList<Appointment> filterMonthAppointments() throws SQLException {
        ObservableList<Appointment> list = FXCollections.observableArrayList();

        String sql = "SELECT * FROM appointments WHERE MONTH(Start) = MONTH(CURRENT_DATE())";

            try {
                PreparedStatement ps = JDBC.connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();


                while (rs.next()) {
                    Timestamp toStart = rs.getTimestamp("Start");
                    Timestamp toEnd = rs.getTimestamp("End");
                    LocalDateTime start = toStart.toLocalDateTime();
                    LocalDateTime end = toEnd.toLocalDateTime();
                    list.add(new Appointment(rs.getInt("Appointment_ID"),
                            rs.getString("Title"),
                            rs.getString("Description"),
                            rs.getString("Location"),
                            rs.getString("Type"),
                            start,
                            end,
                            rs.getInt("Customer_ID"),
                            rs.getInt("User_ID"),
                            rs.getInt("Contact_ID")));
                }
                return list;
            }catch (Exception e) {

        }
        return null;
    }
/** this method will return a list of appointment objects with a matching number of the month (ex: January = 1, May= 5)
 * @param month this get the number that represents the month
 * @return this will return the list of appointment objects that have mathcing months
 * @throws SQLException this is the sql exception*/
    public static ObservableList<Appointment> getMonth(int month) throws SQLException {
            ObservableList<Appointment> list = FXCollections.observableArrayList();
            String sql = "SELECT FROM Appointments WHERE MONTH(Start) = ?";

            try {
                PreparedStatement ps = JDBC.connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                Timestamp toStart = rs.getTimestamp("Start");
                Timestamp toEnd = rs.getTimestamp("End");
                LocalDateTime start = toStart.toLocalDateTime();
                LocalDateTime end = toEnd.toLocalDateTime();

                while (rs.next()) {
                    list.add(new Appointment(rs.getInt("Appointment_ID"),
                            rs.getString("Title"),
                            rs.getString("Description"),
                            rs.getString("Location"),
                            rs.getString("Type"),
                            start,
                            end,
                            rs.getInt("Customer_ID"),
                            rs.getInt("User_ID"),
                            rs.getInt("Contact_ID")));
                }
                return list;
            }
           catch (Exception e) {

           }
            return null;
    }
    /** this method will return the total number of appointments if the appointment has a matching Month and appointment
     * type from the input.
     * @param appointmentType this is the type of appointment that is passed into method
     * @param monthNum this is the number that represents a month (ex: Jan = 1, May = 5, Dec = 12)
     * @param count this is the total number of appointments that will continuosly be passed into this method
     * until there are no more matching appointments
     * @return count this will return the total appointments
     *  @throws SQLException this is the sql exception*/
    public static int getAppointmentCount(Integer monthNum, String appointmentType, int count) throws SQLException {
            String sql = "SELECT COUNT(*) FROM appointments WHERE MONTH(Start) = ? AND Type = ?";

            try {
                PreparedStatement ps = JDBC.connection.prepareStatement(sql);

                ps.setInt(1, monthNum);
                ps.setString(2, appointmentType);

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    count += rs.getInt("COUNT(*)");
                }

                return count;
            }
            catch (Exception e) {

            }
        return 0;
    }

}
