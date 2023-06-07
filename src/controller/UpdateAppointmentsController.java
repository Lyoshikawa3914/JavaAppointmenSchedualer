package controller;

import DBDatabase.DAOContacts;
import DBDatabase.DAOUsers;
import DBDatabase.DAOappointments;
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
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.Contacts;
import model.Customer;
import model.Users;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.ResourceBundle;

/**this is the updateAppointment class.*/
public class UpdateAppointmentsController implements Initializable {
    private Parent root;
    private Scene scene;
    private Stage stage;
    private final ZoneId localZoneID = ZoneId.systemDefault();

    @FXML private ComboBox<String> startHCombo;
    @FXML private ComboBox<String> startMCombo;
    @FXML private ComboBox<String> endHCombo;
    @FXML private ComboBox<String> endMCombo;

    @FXML private TextField appointmentCol;
    @FXML private TextField titleCol;
    @FXML private TextField descriptionCol;
    @FXML private TextField locationCol;
    @FXML private TextField typeCol;

    @FXML private DatePicker startDatePick;
    @FXML private DatePicker endDatePick;
    @FXML private TextField startTime;
    @FXML private TextField endTime;



    @FXML private ComboBox<Integer> contactCombo;
    @FXML private ComboBox<Integer> customerCombo;
    @FXML private ComboBox<Integer> userCombo;
    @FXML private ComboBox<String> typeComboBox;




    @FXML private Button updateButton;
    @FXML private Button backButton;

    @FXML void startHComboAction (ActionEvent event) {

    }
    @FXML void startMComboAction (ActionEvent event) {

    }
    @FXML void endHComboAction (ActionEvent event) {

    }
    @FXML void endMComboAction (ActionEvent event) {

    }

    /** method allows user to go back to the appointments.fxml
     * @param event this will allow method to do the action it needs to do*/
    @FXML void goBack(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/appointments.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    /**This is the method that will receive the data when a user presses the update button from teh 'appointments.fxml'
     * data from DAOContacts, DAOCustomers, and DAOUsers is called and saved into their own resprective Observable Lists
     *
     *
     * all data from the the appointmentsController are populated into the textfields
     *
     * @param object is the appointment object that is being sent to this method
     * @throws SQLException this is the sql exception
     * @throws InterruptedException this is the interupted exception*/
    public void sendAppointmentsInfo(Appointment object) throws SQLException, InterruptedException{
        /* Putting all of the tranferred contents from the appointment object into
        * each respectful field*/

        ObservableList<Contacts> contactList = DAOContacts.getDataContacts();
        ObservableList<Integer> filterContact = FXCollections.observableArrayList();

        ObservableList<Customer> customerList = DAOcustomers.getDataCustomer();
        ObservableList<Integer> filterCustomer = FXCollections.observableArrayList();

        ObservableList<Users> userList = DAOUsers.getDataUsers();
        ObservableList<Integer> filterUser = FXCollections.observableArrayList();

        appointmentCol.setText(String.valueOf(object.getAppointments()));
        titleCol.setText(object.getTitle());
        descriptionCol.setText(object.getDescription());
        locationCol.setText(object.getLocation());

        ObservableList<String> typesOfAppointments = FXCollections.observableArrayList("Coffee Break",
                "Meeting", "Lunch Break", "Other", "De-Briefing", "Planning Session");

        typeComboBox.setItems(typesOfAppointments);
        for (int i = 0; i < typesOfAppointments.size(); i++) {
            if (String.valueOf(typesOfAppointments.get(i)).equals(object.getType())) {
                typeComboBox.getSelectionModel().select(i);
            }
        }



        /* will go through each list, compare the id of each item
        * if there is a match, that item id will be displayed to the combo*/
        for (Contacts contact: contactList) {
            filterContact.add(contact.getId());
        }
        contactCombo.setItems(filterContact);
        for (int i = 0; i < filterContact.size(); i++) {
            if (filterContact.get(i) == object.getContactID()) {
                contactCombo.getSelectionModel().select(i);
            }
        }

        for (Customer customer: customerList) {
            filterCustomer.add(customer.getCustomerID());
        }
        customerCombo.setItems(filterCustomer);
        for (int i = 0; i < filterCustomer.size(); i++) {
            if (filterCustomer.get(i) == object.getCustomerID()) {
                customerCombo.getSelectionModel().select(i);
            }
        }

        for (Users user: userList) {
            filterUser.add(user.getId());
        }
        userCombo.setItems(filterUser);
        for (int i = 0; i < filterUser.size(); i++) {
            if (filterUser.get(i) == object.getUserID()) {
                userCombo.getSelectionModel().select(i);
            }
        }


        // will extract the starting date and time from datetime of the appointment object
        //
        LocalDateTime startDateTimeAppointment = object.getStart();
        LocalDate startDateAppointments = startDateTimeAppointment.toLocalDate();
        startDatePick.setValue(startDateAppointments);
        LocalTime startTimeAppointments = startDateTimeAppointment.toLocalTime();
        startHCombo.getSelectionModel().select(startTimeAppointments.getHour());
        startMCombo.getSelectionModel().select(startTimeAppointments.getMinute());


        // will extract the ending date and time from datetime of the appointment object
        //
        LocalDateTime endDateTimeAppointment = object.getEnd();
        LocalDate endDateAppointments = endDateTimeAppointment.toLocalDate();
        endDatePick.setValue(endDateAppointments);
        LocalTime endTimeAppointments = endDateTimeAppointment.toLocalTime();
        endHCombo.getSelectionModel().select(endTimeAppointments.getHour());
        endMCombo.getSelectionModel().select(endTimeAppointments.getMinute());

    }

    /** this is the method that will check if the appointment that is being entered has a time conflict with another appointment
     *  * The condiditons include whether the appointment time matches the proposed time,
     *  * if the proposed start datetime is in the time window of a preexisting appointment,
     *  * if the proposed end datetime is in the time window of a preexisting appointment,
     *  * and if the preexisting appointment is within the time window of the proposed appointment
     * @param appointmentID this is the appointment identification number
     * @param endDateTime this is the end time for the proposed appointment
     * @param startDateTime  this is the start time for the proposed appointment
     * @return this will return either true or false. True will be returned if there is a time conflict. If not, it will return false*/
    private boolean conflictAppointmentCustomer(Integer appointmentID, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        try {
            ObservableList<Appointment> appointments = DAOappointments.getAppointmentsByCustomerID(customerCombo.getSelectionModel().getSelectedItem());
            for (Appointment appointment: appointments) {
                if (appointment.getAppointments() == appointmentID) {
                    continue;
                }
                else if (appointment.getStart().isEqual(startDateTime) || appointment.getEnd().isEqual(endDateTime)) {
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
        System.out.println("L");
        return false;
    }

//    /** this is the method that will check if the appointment that is being entered has a time conflict with a matching contactID
//      * @param appointmentID this is the appointment identification number
//     * @param contactID this is the contact identification number
//     * @param endDateTime this is the end time for the proposed appointment
//     * @param startDateTime  this is the start time for the proposed appointment
//     * @return this will return either true or false. True will be returned if there is a time conflict. If not, it will return false*/
//    private boolean conflictAppointmentContact(Integer appointmentID, Integer contactID, LocalDateTime startDateTime, LocalDateTime endDateTime) {
//        try {
//            ObservableList<Appointment> contactAppointments = DAOappointments.getAppointmentsByContactID(contactID);
//            for (Appointment appointment: contactAppointments) {
//                if (appointment.getAppointments() == appointmentID) {
//                    continue;
//                }
//
//                else if (appointment.getStart().isEqual(startDateTime) || appointment.getEnd().isEqual(endDateTime)) {
//                    System.out.println("a");
//                    return true;
//                }
//                else if (appointment.getStart().isBefore(endDateTime) && endDateTime.isBefore(appointment.getEnd())) {
//                    System.out.println("b");
//                    return true;
//                } else if (appointment.getStart().isBefore(startDateTime) && startDateTime.isBefore(appointment.getEnd())) {
//                    System.out.println("c");
//                    return true;
//                }
//
//            }
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        return false;
//    }


/**This method take the text/data from the textfields and combobox and insert them in to the appointment table in DAO
 * if any field is left empty, an alert will appear and tell the user they left some fields empty
 * * the user will enter text into the text fields.
 *      * if the times entered are outside of business hours, an alert will tell user.
 *      * an alert will display if the the appointments aren't on the same day.
 *      * an alert will display if the start datetime is after the end datetime
 *      * this method will call the 'conflictAppointment' method that will check if there is a conflicting time.
 * after entering in the fields, the screen will return to the appointmentController
 *  @param event this will allow method to do the action it needs to do*/
    @FXML void updateAppointment(ActionEvent event) throws SQLException, IOException {
        Integer appointmentIDTextfield = Integer.valueOf(appointmentCol.getText());
        String descriptionTextfield = descriptionCol.getText();
        String locationTextfield = locationCol.getText();
        String titleTextfield = titleCol.getText();
        LocalDate sDate = startDatePick.getValue();
        LocalDate eDate = endDatePick.getValue();


        // converts and parses the time that is a String type to a Time data type
        LocalTime sTime = LocalTime.of(
                Integer.valueOf(startHCombo.getSelectionModel().getSelectedItem()),
                Integer.valueOf(startMCombo.getSelectionModel().getSelectedItem()));
        LocalTime eTime = LocalTime.of(
                Integer.valueOf(endHCombo.getSelectionModel().getSelectedItem()),
                Integer.valueOf(endMCombo.getSelectionModel().getSelectedItem()));

        LocalDateTime startDateTime = LocalDateTime.of(sDate, sTime);
        LocalDateTime endDateTime = LocalDateTime.of(eDate, eTime);


        ZonedDateTime startUTC = startDateTime.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endUTC = endDateTime.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC"));

        //getting localized business hours
        ZonedDateTime startEST = ZonedDateTime.of(sDate, LocalTime.of(8, 0), ZoneId.of("America/New_York"));
        LocalDateTime startLocal = startEST.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endLocal = startLocal.plusHours(14);



        if (customerCombo.getSelectionModel().getSelectedItem() == null ||
                userCombo.getSelectionModel().getSelectedItem() == null ||
                contactCombo.getSelectionModel().getSelectedItem() == null ||
                descriptionCol.getText() == null ||
                locationCol.getText() == null ||
                titleCol.getText() == null ||
                typeComboBox.getSelectionModel().isEmpty()) {
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
        else if (conflictAppointmentCustomer(appointmentIDTextfield, startDateTime, endDateTime) == true) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("The customer time conflicts with with their other appointments or with contact appointments");
            alert.showAndWait();
        }
//        else if (conflictAppointmentContact(appointmentIDTextfield, contactCombo.getSelectionModel().getSelectedItem(), startDateTime, endDateTime)) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setContentText("The customer time conflicts with with the contact's other appointments");
//            alert.showAndWait();
//        }

        else {
            DAOappointments.update(
                    titleTextfield,
                    descriptionTextfield,
                    locationTextfield,
                    typeComboBox.getSelectionModel().getSelectedItem().toString(),
                    startDateTime,
                    endDateTime,
                    customerCombo.getSelectionModel().getSelectedItem(),
                    userCombo.getSelectionModel().getSelectedItem(),
                    contactCombo.getSelectionModel().getSelectedItem(),
                    appointmentIDTextfield);

            root = FXMLLoader.load(getClass().getResource("/view/appointments.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }


//    @FXML void selectTypeCombo (ActionEvent event) {
//
//    }
//    @FXML
//    void selectEndDatePick(ActionEvent event) {
//
//    }
//
//    @FXML
//    void selectStartDatePick(ActionEvent event) {
//
//    }
//
//    @FXML
//    void userComboClick(ActionEvent event) {
//
//    }
//    @FXML
//    void contactComboClick(ActionEvent event) {
//
//    }
//
//    @FXML
//    void customerComboClick(ActionEvent event) {
//
//    }

/**this will initilaize this class and disable the appointmentCol so users can't interact with the appointment ID
 * this will also set the types of appointments to the appointment type combo box
 * @param resourceBundle creates locale-specific data
 *  @param url creates a pointer that will point to a resource on the internet*/
    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        //This list will be used to populate 'typeComboBox'

        appointmentCol.setDisable(true);
        int i = 0;
        ObservableList<String> hTime = FXCollections.observableArrayList();
        ObservableList<String> mTime = FXCollections.observableArrayList();

        // populating the time comboboxes with 00-23 for hours and 00-59 for minutes
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
}
