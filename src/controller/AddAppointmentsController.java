package controller;

import DBDatabase.DAOContacts;
import DBDatabase.DAOUsers;
import DBDatabase.DAOappointments;
import DBDatabase.DAOcustomers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.Contacts;
import model.Customer;
import model.Users;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

import static java.lang.String.*;

/**This class allows the functionality of the 'addAppointment.fxml'.*/
public class AddAppointmentsController implements Initializable {
    private Parent root;
    private Scene scene;
    private Stage stage;
    private final ZoneId localZoneID = ZoneId.systemDefault();

    @FXML private ComboBox<String> startHCombo;
    @FXML private ComboBox<String> startMCombo;
    @FXML private ComboBox<String> endHCombo;
    @FXML private ComboBox<String> endMCombo;


    @FXML private TextField appointmentCol;
    @FXML private TextField descriptionCol;
    @FXML private TextField locationCol;
    @FXML private TextField titleCol;
    @FXML private TextField typeCol;
    @FXML private DatePicker startDatePick;
    @FXML private DatePicker endDatePick;
    @FXML private TextField startTime;
    @FXML private TextField endTime;

    @FXML private ComboBox<Integer> customerCombo;
    @FXML private ComboBox<Integer> userCombo;
    @FXML private ComboBox<Integer> contactCombo;
    @FXML private ComboBox<String> comboType;

    @FXML private ChoiceBox<String> typeChoiceBox;
    private String[] appointmentTypes = {"Coffee Break", "Meeting", "Lunch Break", "Other", "Planning Session",
    "De-Briefing "};

    @FXML private Button addButton;

    @FXML private Button backButton;


    @FXML void startHComboAction (ActionEvent event) {

    }
    @FXML void startMComboAction (ActionEvent event) {

    }
    @FXML void endHComboAction (ActionEvent event) {

    }
    @FXML void endMComboAction (ActionEvent event) {

    }
/** this is the method that will check if the appointment that is being entered has a time conflict with another appointment
 * The condiditons include whether the appointment time matches the proposed time,
 * if the proposed start datetime is in the time window of a preexisting appointment,
 * if the proposed end datetime is in the time window of a preexisting appointment,
 * and if the preexisting appointment is within the time window of the proposed appointment
 * @param endDateTime this is the end time for the proposed appointment
 * @param startDateTime  this is the start time for the proposed appointment
 * @return this will return either true or false. True will be returned if there is a time conflict. If not, it will return false*/
    private boolean conflictAppointmentCustomer(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        try {
            ObservableList<Appointment> appointments = DAOappointments.getAppointmentsByCustomerID(customerCombo.getSelectionModel().getSelectedItem());
            for (Appointment appointment: appointments) {
                if (appointment.getStart().isEqual(startDateTime) || appointment.getEnd().isEqual(endDateTime)) {
                    System.out.println("a");
                    return true;
                }
                else if (appointment.getStart().isBefore(endDateTime) && endDateTime.isBefore(appointment.getEnd())) {
                    System.out.println("b");
                    return true;
                }
                else if (appointment.getStart().isBefore(startDateTime) && startDateTime.isBefore(appointment.getEnd())) {
                    System.out.println("c");
                    return true;
                }
                else if (startDateTime.isBefore(appointment.getStart()) && endDateTime.isAfter(appointment.getEnd())) {
                    System.out.println("d");
                    return true;
                }
                else {
                    System.out.println("f");
                    return false;
                }

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

//    /** this is the method that will check if the appointment that is being entered has a time conflict with a matching contactID
//     * @param contactID this is the contact identification number
//     * @param endDateTime this is the end time for the proposed appointment
//     * @param startDateTime  this is the start time for the proposed appointment
//     * @return this will return either true or false. True will be returned if there is a time conflict. If not, it will return false*/
//    private boolean conflictAppointmentContact(Integer contactID, LocalDateTime startDateTime, LocalDateTime endDateTime) {
//        try {
//            ObservableList<Appointment> contactAppointments = DAOappointments.getAppointmentsByContactID(contactID);
//            for (Appointment appointment: contactAppointments) {
//                if (appointment.getStart().isEqual(startDateTime) || appointment.getEnd().isEqual(endDateTime)) {
//                    System.out.println("a");
//                    return true;
//                }
//                else if (appointment.getStart().isBefore(endDateTime) && endDateTime.isBefore(appointment.getEnd())) {
//                    System.out.println("b");
//                    return true;
//                }
//                else if (appointment.getStart().isBefore(startDateTime) && startDateTime.isBefore(appointment.getEnd())) {
//                    System.out.println("c");
//                    return true;
//                }
//                else {
//                    System.out.println("f");
//                    return false;
//                }
//
//            }
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        return false;
//    }

    /**This is the action that allows user to add an appointment after filling in the fields and clicking 'addButton'
     * the user will enter text into the text fields.
     * if the times entered are outside of business hours, an alert will tell user.
     * an alert will display if the the appointments aren't on the same day.
     * an alert will display if the start datetime is after the end datetime
     * this method will call the 'conflictAppointment' method that will check if there is a conflicting time.
     * if any fields are empty, a display will tell that the user is missing information
     * @param event an object of ActionEvent that will allow 'addButton' to submit or display error messages*/
    @FXML void addAppointment(ActionEvent event) throws SQLException, IOException {
        //int appointmentID = incrementID();
        //System.out.println(appointmentID);


        String descriptionTextfield = descriptionCol.getText();
        String locationTextfield = locationCol.getText();
        String titleTextfield = titleCol.getText();
        String typeChoice = typeChoiceBox.getValue();

        LocalDate sDate = startDatePick.getValue();
        LocalDate eDate = endDatePick.getValue();
        LocalTime sTime = LocalTime.of(
                Integer.valueOf(startHCombo.getSelectionModel().getSelectedItem()),
                Integer.valueOf(startMCombo.getSelectionModel().getSelectedItem()));
        LocalTime eTime = LocalTime.of(
                Integer.valueOf(endHCombo.getSelectionModel().getSelectedItem()),
                Integer.valueOf(endMCombo.getSelectionModel().getSelectedItem()));

        // converts and parses the time that is a String type to a Time data type

        LocalDateTime startDateTime = LocalDateTime.of(sDate, sTime);
        LocalDateTime endDateTime = LocalDateTime.of(eDate, eTime);

        ZonedDateTime startUTC = startDateTime.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endUTC = endDateTime.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC"));

        System.out.println("startUTC: " + startUTC);
        System.out.println("endUTC: " + endUTC);

        //getting localized business hours
        ZonedDateTime startEST = ZonedDateTime.of(sDate, LocalTime.of(8,0), ZoneId.of("America/New_York"));
        LocalDateTime startLocal = startEST.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endLocal = startLocal.plusHours(14);


        if (customerCombo.getSelectionModel().isEmpty() ||
                userCombo.getSelectionModel().isEmpty() ||
                contactCombo.getSelectionModel().isEmpty() ||
                descriptionTextfield == null ||
                locationTextfield == null ||
                titleTextfield == null ||
                typeChoice == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please do not leave any field blank");
            alert.showAndWait();
        }


        // Start > End
        else if (startDateTime.isAfter(endDateTime)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please pick another starting time and starting date that doesn't overlap with end time");
            alert.showAndWait();
        }

        // start date != end date
        else if (!(sDate.equals(eDate))) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please pick date that starts and ends on the same day");
            alert.showAndWait();
        }
        // start < 8am or end > 10pm
        else if (startDateTime.isBefore(startLocal) || endDateTime.isAfter(endLocal)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Your time is outside of business hours");
            alert.showAndWait();
        }
        else if (conflictAppointmentCustomer(startDateTime, endDateTime)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("The customer time conflicts with with their other appointments");
            alert.showAndWait();
        }
//        else if (conflictAppointmentContact(contactCombo.getSelectionModel().getSelectedItem(), startDateTime, endDateTime)) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setContentText("The customer time conflicts with with the contact's other appointments");
//            alert.showAndWait();
//        }

        else {
            DAOappointments.insert(
                    titleTextfield,
                    descriptionTextfield,
                    locationTextfield,
                    typeChoice,
                    startDateTime,
                    endDateTime,
                    customerCombo.getSelectionModel().getSelectedItem(),
                    userCombo.getSelectionModel().getSelectedItem(),
                    contactCombo.getSelectionModel().getSelectedItem());

            root = FXMLLoader.load(getClass().getResource("/view/appointments.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }
    }

//
//    @FXML void selectEndDatePick(ActionEvent event) {
//
//    }
//    @FXML void selectStartDatePick(ActionEvent event) {
//
//    }
//
//    @FXML void userComboClick(ActionEvent event) {
//
//    }
//    @FXML void contactComboClick(ActionEvent event) {
//
//    }
//
//    @FXML void customerComboClick(ActionEvent event) {
//
//    }
//
//    @FXML void startHourSelect(ActionEvent event) {
//
//    }
//    @FXML void endHourSelected(ActionEvent event) {
//
//    }
//    @FXML void endMinSelected(ActionEvent event) {
//
//    }
//    @FXML void startMinSelected(ActionEvent event) {
//
//    }

    /**This method will allow user to go back
     * @param event this allows the button to go back*/
    @FXML void goBack(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/appointments.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
/**This will start up the screen. This will pre-set any text, disable the 'appointmentCol', and call the 'getData' methods from
 * DAOUser, DAOContact, and DAOCustomer and populate the ids into their own combo box
 * @param resourceBundle creates locale-specific data
 * @param url creates a pointer that will point to a resource on the internet*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int i = 0;
        appointmentCol.setDisable(true);

        typeChoiceBox.getItems().addAll(appointmentTypes);

        try {
            // this subsection will populate the contact combo box with contact ids
            ObservableList<Contacts> contactList = DAOContacts.getDataContacts();
            ObservableList<Integer> filterContact = FXCollections.observableArrayList();
            for (Contacts contact: contactList) {
                filterContact.add(contact.getId());
            }
            contactCombo.setItems(filterContact);

            // this subsection will populate the user combo box with user ids
            ObservableList<Users> userList = DAOUsers.getDataUsers();
            ObservableList<Integer> filterUser = FXCollections.observableArrayList();
            for (Users user: userList) {
                filterUser.add(user.getId());
            }
            userCombo.setItems(filterUser);

            // this subsection will populate the customer combo box with customer ids
            ObservableList<Customer> customersList = DAOcustomers.getDataCustomer();
            ObservableList<Integer> filterCustomer = FXCollections.observableArrayList();
            for (Customer customer: customersList) {
                filterCustomer.add(customer.getCustomerID());
            }
            customerCombo.setItems(filterCustomer);

            ObservableList<String> hTime = FXCollections.observableArrayList();
            ObservableList<String> mTime = FXCollections.observableArrayList();

            for (i = 0; i <= 23; i++) {
                if (i < 10) {
                    int newI = i;
                    String formattedNumber = String.format("%02d", newI);
                    hTime.add(String.valueOf(formattedNumber));
                }
                else {
                    hTime.add(String.valueOf(i));
                }
            }

            for (i = 0; i <= 59; i++) {
                if (i < 10) {
                    int newI = i;
                    String formattedNumber = String.format("%02d", newI);
                    mTime.add(String.valueOf(formattedNumber));
                }
                else {
                    mTime.add(String.valueOf(i));
                }
            }

            startMCombo.setItems(mTime);
            startHCombo.setItems(hTime);
            endMCombo.setItems(mTime);
            endHCombo.setItems(hTime);
        }

         catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
