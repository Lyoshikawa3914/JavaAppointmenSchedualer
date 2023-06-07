package controller;

import DBDatabase.DAOUsers;
import DBDatabase.DAOappointments;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.AttemptsCounter;
import main.LoginAlert;
import model.Appointment;
import model.Users;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.ResourceBundle;

/**this is the class that initializes and provides functionality to the login screen*/
public class LogInFormController implements Initializable {
    @FXML
    private Label loginLabel;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label dateTime;
    @FXML
    private TextField usernameLogin;
    @FXML
    private TextField passwordLogin;
    @FXML
    private Button onActionEnter;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private Integer attempts = 0;

    /** this lambda expression keeps track of the user's attempts when trying to log in
     * @ return this will return the total number of attempts*/
    // this is the lambda expression to count the number of attempts
    AttemptsCounter attemptsCounter = () -> {
        return attempts++;
    };

    /** this lambda expression notifies when the login is successful to improve efficiency and cut down on code.
     * if successful, a pop up window will apear
     * @param y this will take in the Resource Bundle to that will determine the locale of device*/
    // this lambda expression notifies user if their login was successful
    LoginAlert alertBox = (y) -> {
        ResourceBundle rb = ResourceBundle.getBundle("language/Nat", Locale.getDefault());
        if (y.equals("fr")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("Welcome"));
                alert.setContentText(
                        rb.getString("login") + " " +
                                rb.getString("successful"));
            alert.showAndWait();

        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("Welcome"));
                    alert.setContentText(
                            rb.getString("login") + " " +
                                    rb.getString("successful"));
            alert.showAndWait();

        }
    };

    /** this method initializes this class. This method sets and also populates the data from DAO tables into the program's tables.
     * the lambda expression is created to help count the number of attempts
     * @param resourceBundle creates locale-specific data
     *  @param url creates a pointer that will point to a resource on the internet*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResourceBundle rb = ResourceBundle.getBundle("language/Nat", Locale.getDefault());
        ZoneId zID = ZoneId.systemDefault();

        if(Locale.getDefault().getLanguage().equals("fr")) {
            System.out.println(rb.getString("hello") + " " + rb.getString("world"));
            loginLabel.setText(rb.getString("login"));
            userNameLabel.setText(rb.getString("username"));
            passwordLabel.setText(rb.getString("Password"));
            onActionEnter.setText(rb.getString("enter"));

            // displays the user's zone area
            dateTime.setText(zID.toString());
        }
        else {
            Locale.setDefault(new Locale("en", "US"));
            //rb = ResourceBundle.getBundle("language/Nat", Locale.getDefault());
            System.out.println(rb.getString("hello") + " " + rb.getString("world"));

            // displays the user's zone area
            dateTime.setText(zID.toString());
            //ZoneId.getAvailableZoneIds().stream().sorted().forEach(System.out::println);
        }
    }


    /**This method will display an alert telling the user that they have a meeting withing 15 min after login in.
     *
     * 'DAOappointments.getUserAppointments(userid)' will be called to check if the user has any appointments
     *
     * if there are no upcoming appointments within 15 min, a different message will be displayed saying you have
     * no upcoming appointments.
     * @param userid this will be need to find out if a the user has an appointment*/
    private void appointmentReminder(int userid) throws SQLException {
        // gets the specified user appointments from DAOappointments class 'getUserAppointments'
        ObservableList<Appointment> appointmentList = DAOappointments.getUserAppointments(userid);

        for (Appointment appointment: appointmentList) {
            LocalDateTime now = LocalDateTime.now();
            long timeDifference = ChronoUnit.MINUTES.between(now, appointment.getStart());
            System.out.println("Time difference is " + timeDifference + "Minutes");

            // this will check if user has an appointment within 15 min
            // if there is a past appointment that has not been deleted, the alert will not pop up

            if ((appointment.getStart().isAfter(now) && appointment.getStart().isBefore(now.plusMinutes(15)))) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setContentText("You have an appointment within 15 minutes\n" + "Appointment " +
                        appointment.getAppointments() + " on " + appointment.getStart().toLocalDate() + " at "
                        + appointment.getStart().toLocalTime());
                alert.showAndWait();
            }

        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Welcome");
        alert.setContentText("You have no upcoming appointments");
        alert.showAndWait();

    }
    /**this method will keep track of the time, date, number of attempts, failed attempts of the user that logged in
     * this method used 'FileWriter' class and 'PrintWriter' to enter the time, date, number of attempts, failed attempts of the user
     * into a document.
     *
     * @param user this be used to know which user logged in
     * @param attempts this will be used saved into the document to know how many login attempts were made*/
    private void activityLog(String user, int attempts) throws IOException {
        //creates or adds on info to "logActivity" the username, attempts, timestamps, etc

        // filename creation
        String fileName = "login_activity.txt", item;
        FileWriter logFileWriter = new FileWriter(fileName, true);
        Timestamp now = Timestamp.from(Instant.now());
        PrintWriter outputFile = new PrintWriter(logFileWriter);
        outputFile.println("Username: " + user + ", DateTime: " + LocalDateTime.now()
                + ", Timestamp: " + now + ", Attempts: " + attempts + ", Failed Attempts: " + (attempts - 1));
        outputFile.close();

    }

/**this method will call DAOUsers.getDataUsers(), save it as 'userList' and compare text from the username and password textfields,
 * and compare if the username and password text match the data from 'userList'
 *
 * if the textfields are left empty, an alert will display and tell user to not leave them empty
 *
 * if the user enters text that don't match any of the objects in 'userList', an alert will display telling user
 * that they entered in the wrong username or password.
 *
 * the amount of 'attempts' will be tracked using the 'AttemptsCounter' lambda expression interface
 *
 * if the user successfully entered in the right text, 'activitLog' is called with 'attempts' and 'username' as arguments
 * @param event this will allow method to do the action it needs to do
 * */
    @FXML void enterButtonLoginAction(ActionEvent event) throws IOException, SQLException {
        ResourceBundle rb = ResourceBundle.getBundle("language/Nat", Locale.getDefault());
        ObservableList<Users> userList = DAOUsers.getDataUsers();
        String userName = "";

// will look through the userList and check if the entered textfields match the user password and user login
//        activityLog();

        if (DAOUsers.checkUserValidation(usernameLogin.getText(), passwordLogin.getText()) == true) {
            try {
                for (Users user : userList) {
                    if ((usernameLogin.getText().equals(user.getUsername())) && (passwordLogin.getText().equals(user.getPassword()))) {
                        // root = the fxml file that the app will go to after clicking 'enter' button
                        root = FXMLLoader.load(getClass().getResource("/view/customers.fxml"));
                        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                        appointmentReminder(user.getId());
                        userName = user.getUsername();
//                   attempts += 1;
                        attemptsCounter.count();
                        activityLog(userName, attempts);
                    }

                }
                alertBox.showAlert(String.valueOf(Locale.getDefault().getLanguage()));
            } catch (IllegalStateException e) {
            }
        }
            else if (usernameLogin.getText().isEmpty() || passwordLogin.getText().isEmpty()) {
                if (Locale.getDefault().getLanguage().equals("fr")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(rb.getString("Error"));
                    alert.setContentText(
                            rb.getString("Please") +
                                    rb.getString("do") + " " +
                                    rb.getString("not") + " " +
                                    rb.getString("leave") + " " +
                                    rb.getString("any") + " " +
                                    rb.getString("fields") + " " +
                                    rb.getString("blank")
                    );
                    alert.showAndWait();
                    System.out.println(
                            rb.getString("Please") +
                                    rb.getString("do") + " " +
                                    rb.getString("not") + " " +
                                    rb.getString("leave") + " " +
                                    rb.getString("any") + " " +
                                    rb.getString("fields") + " " +
                                    rb.getString("blank")
                    );
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Please don't leave any fields blank.");
                    alert.showAndWait();
                }

                attemptsCounter.count();
            }

            else {
                if (Locale.getDefault().getLanguage().equals("fr")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(rb.getString("Error"));
                    alert.setContentText(
                            rb.getString("Please") + " " +
                                    rb.getString("enter") + " " +
                                    rb.getString("correct") + " " +
                                    rb.getString("Password") + " " +
                                    rb.getString("or") + " " +
                                    rb.getString("username")
                    );
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(rb.getString("Error"));
                    alert.setContentText(
                            rb.getString("Please") + " " +
                                    rb.getString("enter") + " " +
                                    rb.getString("correct") + " " +
                                    rb.getString("Password") + " " +
                                    rb.getString("or") + " " +
                                    rb.getString("username")
                    );
                    alert.showAndWait();
                }
            attemptsCounter.count();
            }

        }
    }



