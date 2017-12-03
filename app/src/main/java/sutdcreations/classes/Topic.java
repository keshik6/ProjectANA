package sutdcreations.classes;

import java.util.ArrayList;

/**
 * Created by Beng Haun on 2/12/2017.
 */

public class Topic {

    String title;
    ArrayList<Question> questions = new ArrayList<>();
    String key;

    public Topic(){
        //default constructor for Firebase
    }



    public Topic(String title) {
        this.title = title;
    }

    public void setKey(String k){
        key = k;
    }

    public void addQuestion(Question question) {
        question.setKey(getKey() + question.getTitle());
        questions.add(question);
    }

    public void removeQuestion(Question question) {
        questions.remove(question);
    }

    //public getters for Firebase
    public String getTitle() {
        return title;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }
    public String getKey() {
        return key;
    }
}
