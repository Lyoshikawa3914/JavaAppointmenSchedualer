package main;

import DBDatabase.DAOcustomers;
import DBDatabase.JDBC;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;

/** This class creates the login screen that the user will interact*/
public class Main extends Application {
    static int customerID = 0;
    static int appointmentID = 0;

    /**This is the start method. The main entry point that begins the program in the login screen and
     * displays the scene to the user.
     * @param primaryStage the initial scene that opens when program runs*/
    @Override public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/loginForm.fxml"));
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

/** this creates the app that will will start the program and open and close a connection to database
 * @param args this is the a string list argument
 * @throws SQLException this is the sql exception*/
    public static void main(String[] args) throws SQLException {
       JDBC.openConnection();

        launch(args);

        JDBC.closeConnection();


    }
}
