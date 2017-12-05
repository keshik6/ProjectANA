package sutdcreations.classes;

import java.util.ArrayList;

/**
 * Created by Beng Haun on 2/12/2017.
 */

public class Question {

    User asker;
    String title;
    String body;
    String key;
    int votes;
    ArrayList<User> voted = new ArrayList<>();
    ArrayList<Answer> answers = new ArrayList<>();
    ArrayList<String> tags = new ArrayList<>();
    boolean isClosed = false;
    boolean isLive = false;
    boolean feedback = false;

    public Question(){//default constructor for Firebase
    }

    public Question(String title, String body, ArrayList<String> tags) {
        this.title = title;
        this.body = body;
        this.tags = tags;
    }

    public void upVote(User user) {
        votes+=1;
        voted.add(user);
    }

    public void downVote(User user) {
        votes -= 1;
        voted.add(user);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public void close() {
        isClosed = true;
    }

    public void removeAnswer(Answer answer) {
        answers.remove(answer);
    }

    public void setKey(String k){
        this.key = k;
    }

    public void setIsLive(boolean isLive) {
        this.isLive = isLive;
    }

    public void setFeedback(boolean fb){
        feedback = fb;
    }

    //public getters for Firebase


    public User getAsker() {
        return asker;
    }

    public String getTitle() {
        return title;
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

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public String getKey(){return key;}

    public boolean isLive() {
        return isLive;
    }

    public boolean getIsLive(){return isLive;}

    public boolean isFeedback() {
        return feedback;
    }
}
