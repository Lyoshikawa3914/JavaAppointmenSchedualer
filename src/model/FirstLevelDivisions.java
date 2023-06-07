package model;

/** this is the first level divsions class. This will make */
public class FirstLevelDivisions {
    int id;
    String division;
    int countryID;
    /** this is the contacts class constructor. This will set initialize the class attributes
     * @param division this is the division name
     * @param id this is the division identification number
     * @param countryID this is the country identification number associated with the division*/
    public FirstLevelDivisions(int id, String division, int countryID) {
        this.id = id;
        this.division = division;
        this.countryID = countryID;
    }

    /**this is the get id method. This will return the division id
     * @return id */
    public int getId() {
        return id;
    }
    /**this method will set the id variable to the contact class variable
     * @param id this will set the divison id*/
    public void setId(int id) {
        this.id = id;
    }
    /**this is the get division method. This will return the FirstLevelDivision division name
     * @return division */
    public String getDivision() {
        return division;
    }

    /**this method will set the division variable to the FirstLevelDivision class variable
     * @param division this is the name of the division*/
    public void setDivision(String division) {
        this.division = division;
    }

    /**this is the get country id method. This will return the country id
     * @return id */
    public int getCountryID() {
        return countryID;
    }
    /**this method will set the country id variable to the FirstLevelDivision class variable
     * @param countryID this is the country id number*/
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

/** this is the toString method. this will translate the data from the division table into a string.
 * @return division. This is the division name*/
    public String toString() {

        return division;
    }
}


