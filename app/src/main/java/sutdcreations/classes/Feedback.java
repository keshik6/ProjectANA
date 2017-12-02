package sutdcreations.classes;

import java.util.ArrayList;

/**
 * Created by Beng Haun on 2/12/2017.
 */

public class Feedback {

    int understand;
    int dont_understand;
    ArrayList<User> voted;
    public Feedback(){
        //default constructor for Firebase
    }

    //public getters for Firebase
    public int getUnderstand() {
        return understand;
    }

    public int getDont_understand() {
        return dont_understand;
    }

    public ArrayList<User> getVoted() {
        return voted;
    }
}