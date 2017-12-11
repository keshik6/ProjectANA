package sutdcreations.classes;

import java.util.ArrayList;

/**
 * Created by Beng Haun on 2/12/2017.
 */

public class Answer {
    Student answerer_stu;
    Teacher answerer_tch;
    String body;
    ArrayList<String> upVoted = new ArrayList<>();
    ArrayList<String> downVoted = new ArrayList<>();
    int votes;

    public Answer(){
        //default constructor for Firebase
    }

    public Answer(String body, User answerer) {
        if (answerer instanceof Student){
            this.answerer_stu = (Student) answerer;
        }

        if (answerer instanceof Teacher){
            this.answerer_tch = (Teacher) answerer;
        }
        this.body = body;

    }

    public void upVote(User user) {
        votes+=1;
        upVoted.add(user.getUid());
    }

    public void downVote(User user) {
        votes -= 1;
        downVoted.add(user.getUid());
    }

    public void removeUpVote(User user) {
        votes-=1;
        upVoted.remove(user.getUid());
    }

    public void removeDownVote(User user) {
        votes+=1;
        downVoted.remove(user.getUid());
    }

    public ArrayList<String> getUpVoted() {
        return upVoted;
    }

    public ArrayList<String> getDownVoted() {
        return downVoted;
    }

    public void setAnswerer_stu(Student answerer_stu) {
        this.answerer_stu = answerer_stu;
    }

    public User getAnswerer(){
        if (answerer_stu != null) return answerer_stu;
        return answerer_tch;
    }

    public Teacher getAnswerer_tch() {
        return answerer_tch;
    }

    public void setAnswerer_tch(Teacher answerer_tch) {
        this.answerer_tch = answerer_tch;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public void edit(String string) {
        this.body = string;
    }

    //public getters for Firebase
    public User getAnswerer_stu() {
        return answerer_stu;
    }

    public String getBody() {
        return body;
    }

    public int getVotes() {
        return votes;
    }
}