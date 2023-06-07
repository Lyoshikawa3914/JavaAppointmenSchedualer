package main;

import java.io.IOException;

/** this is the method that will increase the count for the amount of attempts the user took to login*/
public interface AttemptsCounter {
    /** the method that will count the attempts
     * @return this will return null*/
    public Integer count();
}
