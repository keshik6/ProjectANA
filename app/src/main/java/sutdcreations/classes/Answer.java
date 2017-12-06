package sutdcreations.classes;

import java.util.ArrayList;

/**
 * Created by Beng Haun on 2/12/2017.
 */

public class Answer {
    User answerer;
    String body;
    int votes;
    ArrayList<User> voted = new ArrayList<>();

    public Answer(){
        //default constructor for Firebase
    }

    public Answer(String body,User answerer) {
        this.body = body;
        this.answerer = answerer;
    }

    public void upVote(User user) {
        votes+=1;
        voted.add(user);
    }

    public void downVote(User user) {
        votes-=1;
        voted.add(user);
    }

    public void edit(String string) {
        this.body = string;
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