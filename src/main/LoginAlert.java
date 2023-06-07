package main;

import java.util.Locale;
import java.util.ResourceBundle;
/** this is the lamba expression interface that will allow program to display the alert when user successfully logs in*/
public interface LoginAlert {
    /** this is the method that will take in the the language string and display a popup. This can
     * @param lang this is the language*/
    public void showAlert(String lang);
}
