package sutdcreations.classes;

import java.util.ArrayList;

/**
 * Created by Beng Haun on 2/12/2017.
 */

public class Question {

    User asker;
    String title;
    String body;
    int votes;
    ArrayList<User> voted;
    ArrayList<Answer> answers;
    ArrayList<String> tags;
    boolean isClosed;

    public Question(){//default constructor for Firebase
    }

    public Question(String title, String body, ArrayList<String> tags) {
    }

    public void upVote() {
    }

    public void downVote() {
    }

    public void addAnswer(Answer answer) {
    }

    public void close() {
    }

    public void removeAnswer(Answer answer) {
    }

    //public getters for Firebase

    public Question(User asker, String title, String body, int votes, ArrayList<User> voted, ArrayList<Answer> answers, ArrayList<String> tags, boolean isClosed) {
        this.asker = asker;
        this.title = title;
        this.body = body;
        this.votes = votes;
        this.voted = voted;
        this.answers = answers;
        this.tags = tags;
        this.isClosed = isClosed;
    }
}
