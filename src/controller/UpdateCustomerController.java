package controller;

import DBDatabase.DAOCountries;
import DBDatabase.DAOFirstLevelDivions;
import DBDatabase.DAOcustomers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.FirstLevelDivisions;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

/**this is the class that will update the customer data sent by the customerContorller*/
public class UpdateCustomerController implements Initializable {
    private Parent root;
    private Scene scene;
    private Stage stage;

    @FXML private TextField id;
    @FXML private TextField name;
    @FXML private TextField address;
    @FXML private TextField postalCode;
    @FXML private TextField phone;
    @FXML private Button updateButton;
    @FXML private Button backButton;
    @FXML private ComboBox<Country> countryBox;
    @FXML private ComboBox<FirstLevelDivisions> divisionBox;

    /** method allows user to go back to the customer.fxml
     * @param event this will allow method to do the action it needs to do*/
    @FXML void backAction(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/customers.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    /**this method will update the customer row once it is clicked.
     * if the user leaves any fields empty, an alert will tell the user to not leave any fields empty
     * once the data is accepted, the customer data will update and the screen will return to the customerController
     * @param event this will allow method to do the action it needs to do*/
    @FXML void updateAction (ActionEvent event) throws SQLException, IOException {
        Integer customerIDTextfield = Integer.valueOf(id.getText());
        String customerNameTextfield = name.getText();
        String customerAddressTextfield = address.getText();
        String customerPostalCodeTextfield = postalCode.getText();
        String customerPhoneTextfield = phone.getText();

        if (customerNameTextfield == null ||
        customerAddressTextfield == null ||
        customerPostalCodeTextfield == null ||
        customerPhoneTextfield == null ||
        divisionBox.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please do not leave any field blank");
            alert.showAndWait();
        }
        else {
            DAOcustomers.update(customerIDTextfield, customerNameTextfield,
                    customerAddressTextfield, customerPostalCodeTextfield,
                    customerPhoneTextfield, countryBox.getSelectionModel().getSelectedItem(),
                    divisionBox.getSelectionModel().getSelectedItem().getId());

            root = FXMLLoader.load(getClass().getResource("/view/customers.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

    }
    /** this is the method that will recieve the customer data from the customerController.
     * All the data will prepopulate all of the textfields after setting the data to the textfields
     * DAOFirstLevelDivions.getDataDivision() and DAOCountries.getAllCountries() will be called and will be placed in
     * observable lists 'divisionLis' and 'countryList'.
     * when an item is selected from countryList, the divisions that share an id with the selected country will populate
     * divisionList
     * @param object this contains the customer object that was sent from customerController
     * @throws SQLException this is the sql exception
     * @throws InterruptedException this is the interrupted exception*/
    public void sendCustomerInfo(Customer object) throws SQLException, InterruptedException {
        ObservableList<FirstLevelDivisions> divisionList = DAOFirstLevelDivions.getDataDivision();
        ObservableList<Country> countryList = DAOCountries.getAllCountries();

        ObservableList<FirstLevelDivisions> filteredDivisionList = FXCollections.observableArrayList();
        ObservableList<Country> filteredCountryList = FXCollections.observableArrayList();

        // this will set each object variables into each textfield
        id.setDisable(true);
        id.setText(String.valueOf(Integer.valueOf(object.getCustomerID())));
        name.setText(object.getCustomerName());
        address.setText(object.getAddress());
        postalCode.setText(object.getPostalCode());
        phone.setText(object.getPhone());

        countryBox.setItems(countryList);


        // will set the countrybox prompt to the selected customer's country
        String countryName = object.getCountry().toString();


       for (FirstLevelDivisions divisiion: divisionList) {
           if (divisiion.getId() == object.getDivisionID()) {
               filteredDivisionList.add(divisiion);
           }
       }
        if (countryName.equals("U.S")) {
            for (FirstLevelDivisions division: divisionList) {
                if (division.getCountryID() == 1) {
                    filteredDivisionList.add(division);
                }
            }
        }
        else if (countryName.equals("UK")) {
            for (FirstLevelDivisions division: divisionList) {
                if (division.getCountryID() == 2) {
                    filteredDivisionList.add(division);
                }
            }
        }
        else if (countryName.equals("Canada")) {
            for (FirstLevelDivisions division: divisionList) {
                if (division.getCountryID() == 3) {
                    filteredDivisionList.add(division);
                }
            }
        }


        countryBox.getSelectionModel().select(object.getCountry());
        divisionBox.setItems(filteredDivisionList);
        divisionBox.getSelectionModel().select(filteredDivisionList.get(0));

    }
/**method that will provides the functionality of selecting the country from the combobox.
 * when country is selected from the combo box, division combo box
 *  will display the division name from the selected country using their IDs
 *  @param event this will allow method to do the action it needs to do*/
    @FXML void updateCountry(ActionEvent event) throws SQLException {
            // an observable list 'divisionList' is made
            // when country is selected from the combo box, division combo box
            // will display the division name from the selected country using their IDs

        ObservableList<FirstLevelDivisions> divisionList = DAOFirstLevelDivions.getDataDivision();
        ObservableList<FirstLevelDivisions> divisionFilterList = FXCollections.observableArrayList();

        Country countryName = countryBox.getSelectionModel().getSelectedItem();
        System.out.println(countryName);

        if (countryName.toString().equals("U.S")) {
            for (FirstLevelDivisions division: divisionList) {
                if (division.getCountryID() == 1) {
                    divisionFilterList.add(division);
                }
            }
        }
        else if (countryName.toString().equals("UK")) {
            for (FirstLevelDivisions division: divisionList) {
                if (division.getCountryID() == 2) {
                    divisionFilterList.add(division);
                }
            }
        }
        else if (countryName.toString().equals("Canada")) {
            for (FirstLevelDivisions division: divisionList) {
                if (division.getCountryID() == 3) {
                    divisionFilterList.add(division);
                }
            }
        }
        divisionBox.setItems(divisionFilterList);

    }

//    @FXML
//    void updateDivision(ActionEvent event) {
//
//    }

    /** this method will initilize this controller, allowing it to display the screen
     * @param resourceBundle creates locale-specific data
     * @param url creates a pointer that will point to a resource on the internet*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
