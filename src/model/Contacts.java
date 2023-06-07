package model;

/**this will create the class for contacts. contacts will be use to create contact objects*/
public class Contacts {
    int id;
    String name;
    String email;

    /** this is the contacts class constructor. This will set initialize the class attributes
     * @param email this is the email of the contact
     * @param id this is the contact identification number
     * @param name this is the name of the contact*/
    public Contacts(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

/**this is the get id method. This will return the contact id
 * @return id */
    public int getId() {
        return id;
    }

    /**this method will set the id variable to the contact class variable
     * @param id this will set the contact id into the contact object*/
    public void setId(int id) {
        this.id = id;
    }

    /**this is the get name method. This will return the name
     * @return name */
    public String getName() {
        return name;
    }
    /**this method will set the name variable to the contact class variable
     * @param name this will set the name string into the contact object*/
    public void setName(String name) {
        this.name = name;
    }

    /**this is the get email method. This will return the email
     * @return email */
    public String getEmail() {
        return email;
    }

    /**this method will set the email variable to the contact class variable
     * @param email this will set the email string to the contact object*/
    public void setEmail(String email) {
        this.email = email;
    }

    /**this is the to string method. This method will transform data from observable list into a string
     * @return email */
    public String toString() {

        return name;
    }
}
