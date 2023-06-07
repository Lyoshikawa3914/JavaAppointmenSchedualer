package model;

/**this is the country class. This will create country objects from country date*/
public class Country {
    int id;
    String name;

    /** this is the country constructor. This will initialize the country objects with class atributes
     * @param name this is the name of the country
     * @param id this is the id number of the country*/
    public Country(int id, String name) {
        this.id = id;
        this.name = name;
    }
    /**this is the get id method. This will return the country id
     * @return id */
    public int getId() {
        return id;
    }
    /**this method will set the id variable to the contact class variable
     * @param id this is the country id*/
    public void setId(int id) {
        this.id = id;
    }
    /**this is the get name method. This will return the country name
     * @return name */
    public String getName() {
        return name;
    }
    /**this method will set the name variable to the contact class variable
     * @param name this is the name of the country*/
    public void setName(String name) {
        this.name = name;
    }
    /**this is the to string method. This method will transform data from observable list into a string
     * @return name */
    @Override
    public String toString() {
        return name;
    }
}
