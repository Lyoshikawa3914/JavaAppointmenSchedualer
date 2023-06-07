package model;

/**this will create the class for Users. This will be use to create User objects*/
public class Users {
    int id;
    String username;
    String password;

    /** this is the Users class constructor. This will set initialize the class attributes
     * @param password this is used to get access to the app
     * @param id this is the user identification number
     * @param username this is the name of the user to login in to app*/
    public Users(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
    /**this is the get id method. This will return the user id
     * @return id */
    public int getId() {
        return id;
    }

    /**this method will set the id variable to the user class variable
     * @param id this is the users id that is set into user object*/
    public void setId(int id) {
        this.id = id;
    }
    /**this is the get username method. This will return the username
     * @return username */
    public String getUsername() {
        return username;
    }

    /**this method will set the username variable to the user class variable
     * @param username this is the username for the user*/
    public void setUsername(String username) {
        this.username = username;
    }

    /**this is the get password method. This will return the password
     * @return password */
    public String getPassword() {
        return password;
    }
    /**this method will set the password variable to the user class variable
     * @param password this is the user password*/

    public void setPassword(String password) {
        this.password = password;
    }
    /**this is the to string method. This method will transform data from observable list into a string
     * @return username */
    public String toString() {
        return username;
    }
}
