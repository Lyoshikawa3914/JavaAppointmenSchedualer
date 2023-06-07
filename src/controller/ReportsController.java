package controller;

import DBDatabase.*;
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
import javafx.stage.Stage;
import main.AttemptsCounter;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Month;
import java.util.HashMap;
import java.util.ResourceBundle;

/** this class will make the reportsController and give it functionality to 'reports.fxml*/
public class ReportsController implements Initializable {
    private Parent root;
    private Scene scene;
    private Stage stage;
    private String totalCustomerAppointmentsTypeByMonth = "";
    private String contactSchedule = "";
    private String demographics = "";


    @FXML private ComboBox<Contacts> contactCombo;
    @FXML
    private Tab contactSchedualeTab;
    @FXML private Tab customerReportsTab;

    @FXML private Button customersButton;
    @FXML private Button appointmentButton;
    @FXML private Button enterButton;

    @FXML private Label demographicsLabel;
    @FXML private Label customerReportLabel;
    @FXML private Label contactSchedualeLabel;
    @FXML private ComboBox<String> monthCombo;
    @FXML private ComboBox<String> appointmentType;
    private String[] appointmentTypes = {"Lunch Break", "Coffee Break", "Meeting", "De-Briefing", "Planning Session",
    "Other"};



    /** this method will switch screens to the appointment.fxml when 'to appointment' button is clicked
     * @param event this will allow this method to take the actionevent object and allow it to have functionality */
    @FXML void toAppointment(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/appointments.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /** this method will switch screens to the customer.fxml when 'to customer' button is clicked
     * @param event this will allow this method to take the actionevent object and allow it to have functionality */
    @FXML void toCustomers(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/customers.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
//    @FXML void monthAction (ActionEvent event) throws SQLException {
//        monthCombo.getSelectionModel().getSelectedItem();
//    }
//
//    @FXML void typeAction (ActionEvent event) throws SQLException {
//
//    }
/** this method will take the selected month and selected appointment type, call the databases, use for loops
 * to determine how many appointments of that type are in that month, then will display the types
 * @param event this is the actionevent*/
    @FXML void enterAction (ActionEvent event) throws SQLException  {
        ObservableList<Appointment> appointmentList = DAOappointments.getDataAppointment();
        int total = 0;

        String monthSelected = monthCombo.getSelectionModel().getSelectedItem();
        String typeSelected = appointmentType.getSelectionModel().getSelectedItem();

        for (Appointment appointment: appointmentList) {
            if (appointment.getType().equals(typeSelected) &&
                    appointment.getStart().getMonth().toString().equals(monthSelected)) {
                total += 1;
            }
        }

        customerReportLabel.setText("Total " + typeSelected + "s in " + monthSelected + ": " + total);
    }


    /** this method will display the contact's schedule when the user selects a contact
     * from the combo box
     *
     * @param event this will allow the method to have functionality*/
    @FXML void contactComboAction (ActionEvent event) throws SQLException {

        Integer contactID = contactCombo.getSelectionModel().getSelectedItem().getId();
        ObservableList<Appointment> appointmentList = DAOappointments.getDataAppointment();


        for (Appointment appointment: appointmentList) {
            if (contactID == appointment.getContactID()) {
                contactSchedule += contactID + " " +
                        contactCombo.getSelectionModel().getSelectedItem().getName() + ": " +
                        "Appointments: " + appointment.getAppointments() + " | " +
                        "Title: " + appointment.getTitle() + " | " +
                        "Type: " + appointment.getType() + " | " +
                        "Description: " + appointment.getDescription() + " | " +
                        "Start: " + appointment.getStart() + " | " +
                        "End: " + appointment.getEnd() + " | " +
                        "CustomerID: " + appointment.getCustomerID() + "\n";
                    }
                }

        contactSchedualeLabel.setText(contactSchedule);
        contactSchedule = "";
            }

    /** this method will populate three different tabs, contaceSchedule, the total of appointment types for each month,
     * and customer demographics by calling DAOappointments.getDataAppointment(), DAOContacts.getDataContacts(),
     * DAOappointments.getAppointmentCount(i, appointmentTypes[j], count), and DAOcustomers.getDataCustomer().
     * @param resourceBundle creates locale-specific data
     *  @param url creates a pointer that will point to a resource on the internet*/
    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // contact's appointments

            ObservableList<Contacts> contactsList = DAOContacts.getDataContacts();
            ObservableList<Contacts> contactSchedulePersons = FXCollections.observableArrayList();

            for (Contacts contacts: contactsList) {
                contactSchedulePersons.add(contacts);
            }
            contactCombo.setItems(contactSchedulePersons);


            // Number of customer appointments type by month
            HashMap<String, Integer> appointmentTypeHash = new HashMap<>();
            HashMap<String, Integer> monthHash = new HashMap<>();

            monthCombo.setPromptText("Month");
            appointmentType.setPromptText("Appointment Type");
            ObservableList<Appointment> appointmentList = DAOappointments.getDataAppointment();
            ObservableList<String> type = FXCollections.observableArrayList();
            ObservableList<String> month = FXCollections.observableArrayList();

            for (Appointment appointment: appointmentList) {
                if(appointmentTypeHash.containsKey(appointment.getType())) {
                    continue;
                }
                else {
                    appointmentTypeHash.put(appointment.getType(), 1);
                    type.add(appointment.getType());
                }

            }
            appointmentType.setItems(type);

            for (Appointment appointment: appointmentList) {
                if(monthHash.containsKey(appointment.getStart().toLocalDate().getMonth().toString())) {
                    continue;
                }
                else {
                    monthHash.put(appointment.getStart().toLocalDate().getMonth().toString(), 1);
                    month.add(appointment.getStart().toLocalDate().getMonth().toString());
                }

            }
            appointmentType.setItems(type);
            monthCombo.setItems(month);


            // customer demographics

            HashMap<String, Integer> divisionHash = new HashMap<>();
            int countUS = 0;
            int countUK = 0;
            int countCanada = 0;

            String divisionUS = "";
            String divisionUK = "";
            String divisionCanada = "";
            String divisionName = "";

            ObservableList<Customer> customerList = DAOcustomers.getDataCustomer();
            for (Customer customer: customerList) {
                System.out.println(customer.getCountry());
                    if (customer.getCountry().getName().equals("U.S")) {
                        countUS++;
                        int divisionCount = DAOcustomers.getDivisionCount(customer.getDivisionID());

                        divisionName = DAOFirstLevelDivions.getDivisionName(customer.getDivisionID());

                        if (divisionHash.containsKey(divisionName)) {
                            continue;
                        }
                        else {
                            divisionHash.put(divisionName, countUS);
                            divisionUS += String.valueOf(customer.getDivisionID()) + ": " + divisionName + " | "+  divisionCount+ "\n";
                        }

                    }
                    else if (customer.getCountry().getName().equals("UK")) {
                        countUK++;

                        int divisionCount = DAOcustomers.getDivisionCount(customer.getDivisionID());

                        divisionName = DAOFirstLevelDivions.getDivisionName(customer.getDivisionID());

                        if (divisionHash.containsKey(divisionName)) {
                            continue;
                        }
                        else {
                            divisionHash.put(divisionName, countUS);
                            divisionUK += String.valueOf(customer.getDivisionID()) + ": " + divisionName + " | "+ divisionCount+ "\n";
                        }

                    }
                    else if (customer.getCountry().getName().equals("Canada")) {
                        countCanada++;

                        int divisionCount = DAOcustomers.getDivisionCount(customer.getDivisionID());

                        divisionName = DAOFirstLevelDivions.getDivisionName(customer.getDivisionID());

                        if (divisionHash.containsKey(divisionName)) {
                            continue;
                        }
                        else {
                            divisionHash.put(divisionName, countUS);
                            divisionCanada += String.valueOf(customer.getDivisionID()) + ": " + divisionName + " | "+ divisionCount + "\n";
                        }
                }
            }
            demographics += "US total Customers: " + countUS + "\n" + divisionUS + "\n" +
                    "UK total Customers: " + countUK + "\n" + divisionUK + "\n" +
                    "Canada total Customers: " + countCanada + "\n"+ divisionCanada + "\n";
            demographicsLabel.setText(demographics);
            customerReportLabel.setText(totalCustomerAppointmentsTypeByMonth);


        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
