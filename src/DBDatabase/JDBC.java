package DBDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** this is the class that will open and close the connection with the database*/
public abstract class JDBC {

    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    /** this will establish a connction*/
    public static Connection connection;  // Connection Interface

    /**this method will open a connection with the database
     * @return connection, this will return the connection needed to get data from database*/
    public static Connection openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object

            System.out.println("Connection successful!");
            return connection;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("Error:" + e.getMessage());

        }
        return null;
    }
    /**this method will make a connection witht the database
     * @return connection this will return a connection*/
    public static Connection getConnection() {
        return connection;
    }

/** this method will close the connection to the database*/
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }


}
