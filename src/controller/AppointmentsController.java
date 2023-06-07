package controller;

import DBDatabase.DAOappointments;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Users;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**This is the appointmentController. This gives gives the appointment funcitonality
 * */
public class AppointmentsController implements Initializable {

    private Parent root;
    private Scene scene;
    private Stage stage;

    /** this will hold a single appointment object when it is selected from the appointment table*/
    public static Appointment appointmentObject;
    private static Users user;



    @FXML private Button toCustomrsButton;
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button reportButton;

    @FXML private Button allButton;
    @FXML private Button weekButton;
    @FXML private Button monthButton;

    @FXML private Tab all;
    @FXML private TableView<Appointment> allTable;
    @FXML private TableColumn<Appointment, Integer> allAppointmentCol;
    @FXML private TableColumn<Appointment, String> allTitleCol;
    @FXML private TableColumn<Appointment, Integer> allCustomerCol;
    @FXML private TableColumn<Appointment, String> allTypeCol;
    @FXML private TableColumn<Appointment, String> allLocationCol;
    @FXML private TableColumn<Appointment, String> allDescriptionCol;
    @FXML private TableColumn<Appointment, String> allStartCol;
    @FXML private TableColumn<Appointment, String> allEndCol;
    @FXML private TableColumn<Appointment, String> allContactCol;
    @FXML private TableColumn<Appointment, Integer> allUserCol;

    /**This method will take the user to the 'addAppointment' screen after clicking the 'add' button
     *
     * @param event this will allow method to do the action it needs to do*/
    @FXML void addAppointments(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/addAppointments2.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    /**This method allows user to switch to see the 'reports.fxml'
     * @param event this will allow method to do the action it needs to do
     * @throws IOException this will throw an input output exception*/
    public void toReports(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/reports.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    /**This method allows user to switch to see the 'appointments.fxml'
     * @param event this will allow method to do the action it needs to do*/
    @FXML void backToCustomers(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/customers.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    /** this method deletes an appointment from the table after selecing the row and pressing the delete button
     * Alerts occur to double check if you want to delete an appointment row
     * Once user selects a row and presses delete, the program will will call DAOappointments.delete(weeklyAppointmentID)'
     * and delete the appointment from DAO table.
     * Then the screen will update the program's appointment table
     * @param event this will allow method to do the action it needs to do*/
    @FXML void deleteAppointment(ActionEvent event) throws IOException, SQLException {
        //creates an Alert object "alert"
        int appointmentID;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setContentText("Are you sure you wish to delete this row?");

        if (allTable.getSelectionModel().getSelectedItem() == null ) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("You must select a row to delete from the Table");
            error.showAndWait();
        }
        else {
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                int weeklyAppointmentID = allTable.getSelectionModel().getSelectedItem().getAppointments();

                DAOappointments.delete(weeklyAppointmentID);
                System.out.println("delete Button clicked");

                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setContentText("Appointment: " + allTable.getSelectionModel().getSelectedItem().getAppointments() +
                        " Type: " + allTable.getSelectionModel().getSelectedItem().getType() + " | Deleted");
                confirmation.showAndWait();
            }

            else if (result.isPresent() && result.get() == ButtonType.CANCEL){
                System.out.println("No deletion");
                allTable.getSelectionModel().clearSelection();

                Alert confirmation = new Alert(Alert.AlertType.ERROR);
                confirmation.setContentText("No deletion");
                confirmation.showAndWait();
            }
        }

        // this will 'refresh' the page to show the updated table
        root = FXMLLoader.load(getClass().getResource("/view/appointments.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }


/** this allows user to update appointmen data by selecing the appointment row and pressing 'update' button.
 *  After clicking 'update' button, this will take all data from that appointment and prepopulate 'updateAppointmentController's
 *  textfields.
 *  This method will grab the customer object from the table and use AMDController to send it to the 'updateAppointment' screen
 *
 *  @param event this will allow method to do the action it needs to do*/
    @FXML void updateAppointments(ActionEvent event) throws IOException{
        // checks to see if a row of parts of selected. If not, execute if statment
        System.out.println("click");
        if (allTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a row from the table to update");
            alert.showAndWait();
        }
        else {
            try {
                appointmentObject = allTable.getSelectionModel().getSelectedItem();

                FXMLLoader loader = new FXMLLoader();

                // assigns this pathway to a loader
                loader.setLocation(getClass().getResource("../view/updateAppointment2.fxml"));
                loader.load();

                // gets the controller associated with the fxml file thats stored on the loader
                UpdateAppointmentsController ADMController = loader.getController();

                ADMController.sendAppointmentsInfo(appointmentObject);

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setWidth(1300);
                stage.setHeight(575);
                stage.setScene(new Scene(scene));
                stage.showAndWait();
            } catch (IllegalStateException | SQLException | InterruptedException e) {

            }
        }
    }
    /** this method will display all of the saved appointments when the 'show all' button is clicked*/
    @FXML void showAll(ActionEvent event) throws SQLException {
        allTable.setItems(DAOappointments.getDataAppointment());
    }

    /** this method will display appointments of the current month when the 'month' button is clicked*/
    @FXML void showMonth(ActionEvent event) throws SQLException {
        ObservableList<Appointment> monthAppointments = DAOappointments.filterMonthAppointments();
        allTable.setItems(monthAppointments);
    }

    /** this method will display appointments of the current week when the 'week' button is clicked*/
    @FXML void showWeek(ActionEvent event) throws SQLException {
        System.out.println("Click");
        ObservableList<Appointment> WeekAppointments = DAOappointments.filterWeekAppointments();

        allTable.setItems(WeekAppointments);
    }

/** this method initializes this class. This method sets and also populates the data from DAO tables into the program's tables
 * @param resourceBundle creates locale-specific data
 *  @param url creates a pointer that will point to a resource on the internet*/
    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        allAppointmentCol.setCellValueFactory(new PropertyValueFactory<>("appointments"));
        allTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        allDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        allLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        allTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        allStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        allEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        allCustomerCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        allUserCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        allContactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));

        try {

            allTable.setItems(DAOappointments.getDataAppointment());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

