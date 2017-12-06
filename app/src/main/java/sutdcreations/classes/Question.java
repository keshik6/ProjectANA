package sutdcreations.classes;

import java.util.ArrayList;

/**
 * Created by Beng Haun on 2/12/2017.
 */

public class Question {
    Student asker;
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

    public Question(String title, String body, ArrayList<String> tags,Student asker) {
        this.title = title;
        this.body = body;
        this.tags = tags;
        this.asker = asker;
        String courseCode = this.getKey().split(" ")[0];
        this.asker.questionMap.put(courseCode,this.asker.questionMap.get(courseCode)+1);
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
        if (answer.answerer instanceof Student){
            Student student = (Student)answer.answerer;
            String courseCode = this.getKey().split(" ")[0];
            student.answerMap.put(courseCode,student.answerMap.get(courseCode)+1);
        }
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


    public Student getAsker() {
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

    public int getAnswersTotal(){
        return answers.size();
    }
}
