package model;

import java.time.LocalDateTime;

/** this is the appointment model class. This will create appointment objects from the appointments
 * extracted from the appointment table from DAOappointments*/
public class Appointment {
    private int appointments;
    private String title;
    private String location;
    private String description;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerID;
    private int userID;
    private int contactID;

/**this is the method that will set up the class attributes
 * @param appointments this is the appointment identification number
 * @param title this is the title of the appointment
 *@param description this is a short descritption of the appointment
 * @param location this is the location of the appointment
 * @param type this is the type of appointment
 * @param start this is the start time and date of the appointment
 * @param end this is the end time and date of the appointment
 * @param userID this is the user identification number that will set up the appointments
 * @param contactID this is the contact number of the person setting up the appointment
 * @param customerID this is the customer identification number who will attend the meeting*/
    public Appointment(int appointments,String title, String description, String location, String type,
                        LocalDateTime start, LocalDateTime end,
                       int customerID, int userID, int contactID) {
        this.appointments = appointments;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }
    /**this method will get the appointment id and return the number
     * @return appointments this will return the appointments id*/
    public int getAppointments() {
        return appointments;
    }

    /**this method will set the appointments id to the appointment class id
     @param appointments this will set the appointment id to the appointment object*/
    public void setAppointments(int appointments) {
        this.appointments = appointments;
    }

    /**this method will get the appointment title and return the title
     * @return this will return the title*/
    public String getTitle() {
        return title;
    }

    /**this method will set the appointments type to the appointment class variable
     * @param title this will set the title to the appointment object*/
    public void setTitle(String title) {
        this.title = title;
    }

    /**this method will get the appointment location and return the location
     * @return this will return the location*/
    public String getLocation() {
        return location;
    }

    /**this method will set the appointments variable to the appointment class variable
     * @param location this will set location to appointment object*/
    public void setLocation(String location) {
        this.location = location;
    }

    /**this method will get the appointment description and return the description
     * @return this will return the desription*/
    public String getDescription() {
        return description;
    }

    /**this method will set the appointments variable to the appointment class variable
     * @param description this will set description to the appointment object*/
    public void setDescription(String description) {
        this.description = description;
    }

    /**this method will get the appointment type and return the type
     * @return this will return the type*/
    public String getType() {

        return type;
    }
/** this method will set the type to the appointment object
 * @param type this is the string type that will be set into the appointment object*/
    public void setType(String type) {

        this.type = type;
    }

    /**this method will get the appointment start datetime
     * @return this will return the start*/
    public LocalDateTime getStart() {

        return start;
    }

    /**this method will set the appointments variable to the appointment class variable
     * @param start this will set start to appointment object*/
    public void setStart(LocalDateTime start) {

        this.start = start;
    }

    /**this method will get the appointment end datetime
     * @return this will return the end*/
    public LocalDateTime getEnd() {
        return end;
    }

    /**this method will set the appointments variable to the appointment class variable
     * @param end this will set end to the appointment object*/
    public void setEnd(LocalDateTime end) {

        this.end = end;
    }
    /**this method will get the customer id from the appointment
     * @return this will return the customer id*/
    public int getCustomerID() {

        return customerID;
    }

    /**this method will set the appointments variable to the appointment class variable
     * @param customerID this will set the customer id number to the customer object*/
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**this method will get the user id from the appointment
     * @return this will return the user id*/
    public int getUserID() {
        return userID;
    }

    /**this method will set the appointments variable to the appointment class variable
     * @param userID this will set the user id number to the appointment object*/
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**this method will get the contact id from the appointment
     * @return this will return the contact id*/
    public int getContactID() {
        return contactID;
    }

    /**this method will set the appointments variable to the appointment class variable
     * @param contactID this will set the contact id number to the appointment object*/
    public void setContactID(int contactID) {

        this.contactID = contactID;
    }


}
