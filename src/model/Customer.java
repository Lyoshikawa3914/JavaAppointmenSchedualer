package model;
// the Customer class

import DBDatabase.DAOCountries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**this will create the class for customers. Customers will be use to create customer objects*/
public class Customer {
    private int customerID;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private int divisionID;


    /** this is the customer class constructor. This will set initialize the class attributes
     * @param customerID this is the customer identification number
     * @param customerName this is the customer's name
     * @param address this is the customer's address
     * @param phone this is the customer's phone number
     * @param postalCode this is the customer's postal code
     * @param divisionID this is the customer's division id number, indicating the Specifc area the person lives within
     * the customer's country*/
    public Customer(int customerID, String customerName, String address, String postalCode, String phone, int divisionID) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionID = divisionID;
    }
/** this method will get the customer id from the object.
 * @return customerID */
    public int getCustomerID() {

        return customerID;
    }

    /**this method will set the id variable to the customer class variable
     * @param customerID this is the customer id number*/
    public void setCustomerID(int customerID) {

        this.customerID = customerID;
    }
    /** this method will get the customer name from the object.
     * @return customerName */
    public String getCustomerName() {

        return customerName;
    }
    /**this method will set the id variable to the customer class variable
     * @param customerName this is the customers name*/
    public void setCustomerName(String customerName) {

        this.customerName = customerName;
    }
    /** this method will get the customer address from the object.
     * @return adress */
    public String getAddress() {

        return address;
    }

    /**this method will set the id variable to the customer class variable
     * @param address this is the customer's address*/
    public void setAddress(String address) {

        this.address = address;
    }
    /** this method will get the customer postal code from the object.
     * @return postalCode */
    public String getPostalCode() {

        return postalCode;
    }
    /**this method will set the id variable to the customer class variable
     * @param postalCode this is the postacl code of the customer*/
    public void setPostalCode(String postalCode) {

        this.postalCode = postalCode;
    }
    /** this method will get the customer phone number from the object.
     * @return phone */
    public String getPhone() {

        return phone;
    }
    /**this method will set the id variable to the customer class variable
     * @param phone this is the phone number of the customer*/
    public void setPhone(String phone) {

        this.phone = phone;
    }
    /** this method will get the customer id from the object.
     * @return customerID */
    public int getDivisionID() {

        return divisionID;
    }
    /**this method will set the id variable to the customer class variable
     * @param divisionID this is the division id of the customer*/
    public void setDivisionID(int divisionID) {

        this.divisionID = divisionID;
    }
    /**this is the to string method. This method will transform data from observable list into a string
     * @return customerName */
    public String toString() {
        return customerName;
    }

    /** This method will get the country of the customer by using the customer's division id
 * @return country
     * @throws SQLException this will throw a sql exception*/
    public Country getCountry() throws SQLException {
        Country country = DAOCountries.getCountryByDivision(divisionID);
        return country;
    }
}
