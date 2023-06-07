package controller;
import DBDatabase.DAOCountries;
import DBDatabase.DAOFirstLevelDivions;
import DBDatabase.DAOcustomers;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
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
import model.Country;
import model.FirstLevelDivisions;
import DBDatabase.DAOFirstLevelDivions;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**This class will allow the functionality of 'addCustomer.fxml'*/
public class AddCustomerController implements Initializable {

    private Parent root;
    private Scene scene;
    private Stage stage;
    private static int currentCustomerID;


    @FXML private TextField customerID;
    @FXML private TextField customerName;
    @FXML private TextField addressCustomers;
    @FXML private TextField postalCodeCustomers;
    @FXML private TextField phoneNumCustomers;

    @FXML private Label countryName;

    @FXML private ComboBox<FirstLevelDivisions> firstDivisionBox;
    @FXML private ComboBox<Country> countryBox;

    @FXML private Button submitButton;

    @FXML private Button backButton;

/**This method will go back the customer screen when user presses the 'backButton'
 * @param event this will allow the method to switch screens from this one to 'customers.fxml'*/
    @FXML void backAction(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/customers.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    /**This is the action that allows user to add a customer after filling in the fields and clicking 'addButton'
     * @param event an object of ActionEvent that will allow 'addButton' to submit or display error messages*/
    @FXML void submitAction(ActionEvent event) throws SQLException, IOException {
        String nameTextfield = customerName.getText();
        String addressTextfield = addressCustomers.getText();
        String postalCodeTextfield = postalCodeCustomers.getText();
        String phoneTextfield = phoneNumCustomers.getText();
        int divisionID = firstDivisionBox.getSelectionModel().getSelectedItem().getId();

        System.out.println(divisionID);

        if (nameTextfield.equals("") ||
                addressTextfield.equals("") ||
                postalCodeTextfield.equals("") ||
                phoneTextfield.equals("") ||
                countryBox.getSelectionModel().getSelectedItem() == null ||
                firstDivisionBox.getSelectionModel().getSelectedItem() == null
        ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a customer to update");
            alert.showAndWait();
        }
        else {
            DAOcustomers.insert(
                    nameTextfield,
                    addressTextfield,
                    postalCodeTextfield,
                    phoneTextfield,
                    divisionID);
        }
        root = FXMLLoader.load(getClass().getResource("/view/customers.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


/**This method will display a list of divisions after selects a country from the 'countryBox'.
 * Then, it will create an empty oberservable list 'divisionFilterList'.
 * When a user selects a country from the country combo box, in the 'if' statements, a list of divisions will appear
 * in the 'divisionCombobox' associated with the selected country
 * @param event this will allow this method to do what it needs to do*/
    @FXML void countrySelected(ActionEvent event) throws SQLException {
        // an observable list 'divisionList' is made
        // when country is selected from the combo box, division combo box
        // will display the division name from the selected country using their IDs

        ObservableList<FirstLevelDivisions> divisionList = DAOFirstLevelDivions.getDataDivision();
        ObservableList<FirstLevelDivisions> divisionFilterList = FXCollections.observableArrayList();

        String countryName = countryBox.getSelectionModel().getSelectedItem().getName();
        System.out.println(countryName);

        if (countryName.equals("U.S")) {
            for (FirstLevelDivisions division: divisionList) {
                if (division.getCountryID() == 1) {
                    divisionFilterList.add(division);
                }
            }
        }
        else if (countryName.equals("UK")) {
            for (FirstLevelDivisions division: divisionList) {
                if (division.getCountryID() == 2) {
                    divisionFilterList.add(division);
                }
            }
        }
        else if (countryName.equals("Canada")) {
            for (FirstLevelDivisions division: divisionList) {
                if (division.getCountryID() == 3) {
                    divisionFilterList.add(division);
                }
            }
        }
        firstDivisionBox.setItems(divisionFilterList);
    }

//
//    @FXML void firstDivisionSelected(ActionEvent event) throws SQLException {
//
//    }

    /**This will start up the screen. This will pre-set any text, disable the 'customerID', and call the 'getAllCountries' methods from
     * DAOCountries. This will also prepopulate the 'countryBox' with a list of countries
     * @param resourceBundle creates locale-specific data
     * @param url creates a pointer that will point to a resource on the internet*/
    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        customerID.setDisable(true);

        try {
            ObservableList<Country> countryList = DAOCountries.getAllCountries();
            ObservableList<Country> countryFilterList = FXCollections.observableArrayList();

            for (Country i: countryList) {
                countryFilterList.add(i);
            }
            countryBox.setItems(countryFilterList);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }



    }












}
