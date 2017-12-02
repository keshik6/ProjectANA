package sutdcreations.classes;

import java.util.ArrayList;

/**
 * Created by Beng Haun on 2/12/2017.
 */

public class Feedback {

    int understand;
    int dont_understand;
    ArrayList<User> voted = new ArrayList<>();
    public Feedback(){
        //default constructor for Firebase
    }

    public void incUnderstand(User user){
        voted.add(user);
        understand+=1;
    }

    public void decUnderstand(User user){
        dont_understand+=1;
        voted.add(user);
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