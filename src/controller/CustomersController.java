package controller;

import DBDatabase.DAOappointments;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
//package view;
import DBDatabase.DAOcustomers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;
import javafx.fxml.Initializable;
import model.Users;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**This is the customerController. This gives funcionality to 'appointment.fxml'*/
public class CustomersController implements Initializable {

    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> customerID;
    @FXML private TableColumn<Customer, String> customerName;
    @FXML private TableColumn<Customer, String> addressCustomers;
    @FXML private TableColumn<Customer, String> postalCodeCustomers;
    @FXML private TableColumn<Customer, String> phoneNumCustomers;
    @FXML private TableColumn<Customer, String> countryCol;

    @FXML private TableColumn<Customer, String> divisionCol;
    @FXML private Button enterButtonCustomers;
    @FXML private Button deleteButtonCustomers;
    @FXML private Button backButtonCustomers;
    @FXML private Button toAppointmentButton;
    @FXML private Button reportButton;


    private Parent root;
    private Scene scene;
    private Stage stage;

    /** this is the customer object that is used to hold the customer that is selected from the table*/
    public static Customer object;
    private static int theUserID;


    /**This method allows user to switch to see the 'appointments.fxml'
     * @param event this will allow method to do the action it needs to do*/
    @FXML void toAppointment(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/appointments.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        System.out.println("Clicked back");
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

/** method allows user to go back to the main.fxml
 * @param event this will allow method to do the action it needs to do*/
    @FXML void backButtonAction(ActionEvent event) throws IOException {
        // root = the fxml file that the app will go to after clicking 'enter' button
        root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        System.out.println("Clicked back");
    }
    /**This method will delete one row of the customers by clicking the customer row, and pressing the delete button
     * Once the delete button is clicked after selecting a customer, 'DAOappointments.delete(weeklyAppointmentID)' is called,
     * checking the appoinment table to see if there are any appointments with that customer id, deleting those appointmens
     * before finally deleting that customer. After deletion, the screen will reupload the screen, showing the updated table.
     *
     * Alerts will occur if a customer isn't selected before the delete button is pressed
     *
     * @param event this will allow method to do the action it needs to do*/
    @FXML void deleteRow(ActionEvent event) throws SQLException, IOException {
        //creates an Alert object "alert"
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setContentText("Are you sure you wish to delete this row?");

        if (customerTable.getSelectionModel().getSelectedItem() == null) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("You must select a row to delete from the Table");
            error.showAndWait();
        }
        else {
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                int customerID = customerTable.getSelectionModel().getSelectedItem().getCustomerID();

                // delete appointments of the selected customer from appointment table
                DAOappointments.deleteCustomerAppointment(customerID);

                // deletes the customer from the customer db
                DAOcustomers.delete(customerID);
                System.out.println("delete Button clicked");

                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setContentText("Customer: " + customerTable.getSelectionModel().getSelectedItem().getCustomerID() + " | Deleted");
                confirmation.showAndWait();
            }
            else if (result.isPresent() && result.get() == ButtonType.CANCEL){
                System.out.println("No deletion");

                Alert confirmation = new Alert(Alert.AlertType.ERROR);
                confirmation.setContentText("No deletion");
                confirmation.showAndWait();
            }
        }

        // this will 'refresh' the page to show the updated table
        root = FXMLLoader.load(getClass().getResource("/view/customers.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
/**This method will switch to the 'addCustomer.fxml' screen
 * @param event this will allow method to do the action it needs to do*/
    @FXML void switchToAddCustomer(ActionEvent event) throws IOException {
        // root = the fxml file that the app will go to after clicking 'enter' button
        root = FXMLLoader.load(getClass().getResource("/view/addCustomer.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        System.out.println("Clicked back");
    }
/** this allows user to update customer data by selecing the customer row and pressing 'update' button.
 *  After clicking 'update' button, this will take all data from that customer and prepopulate 'updateAppointmentController's
 *  textfields.
 *  This method will grab the customer object from the table and use AMDController to send it to the 'updateCustomer' screen
 *
 *  @param event this will allow method to do the action it needs to do*/
    @FXML void updateRow(ActionEvent event) throws IOException {

        // checks to see if a row of parts of selected. If not, execute if statment
        if (customerTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a customer to update");
            alert.showAndWait();
        }
        else {
            try {
                object = customerTable.getSelectionModel().getSelectedItem();

                FXMLLoader loader = new FXMLLoader();

                // assigns this pathway to a loader
                loader.setLocation(getClass().getResource("../view/updateCustomer.fxml"));
                loader.load();

                // gets the controller associated with the fxml file thats stored on the loader
                UpdateCustomerController ADMController = loader.getController();

                ADMController.sendCustomerInfo(object);

                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setWidth(1300);
                stage.setHeight(575);
                stage.setScene(new Scene(scene));
                stage.showAndWait();
            }
            catch(IllegalStateException | SQLException | InterruptedException e) {

            }
        }
    }

    /** this method initializes this class. This method sets and also populates the data from DAO tables into the program's tables
     * @param resourceBundle creates locale-specific data
     *  @param url creates a pointer that will point to a resource on the internet*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressCustomers.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeCustomers.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneNumCustomers.setCellValueFactory(new PropertyValueFactory<>("phone"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        divisionCol.setCellValueFactory(new PropertyValueFactory<>("divisionID"));

        try {
            customerTable.setItems(DAOcustomers.getDataCustomer());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }
}
