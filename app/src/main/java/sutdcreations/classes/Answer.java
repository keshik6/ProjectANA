package sutdcreations.classes;

import java.util.ArrayList;

/**
 * Created by Beng Haun on 2/12/2017.
 */

public class Answer {
    User answerer;
    String body;
    int votes;
    ArrayList<User> voted;

    public Answer(){
        //default constructor for Firebase
    }

    public Answer(String title) {

    }

    public void upVote() {

    }

    public void downVote() {

    }

    public void edit(String string) {

    }

    //public getters for Firebase
    public User getAnswerer() {
        return answerer;
    }

    public String getBody() {
        return body;
    }

    public int getVotes() {
        return votes;
    }

    public ArrayList<User> getVoted() {
        return voted;
    }
}