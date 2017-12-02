package sutdcreations.classes;

import java.util.ArrayList;

/**
 * Created by Beng Haun on 2/12/2017.
 */

public class Topic {

    String title;
    ArrayList<Question> questions;

    public Topic(){
        //default constructor for Firebase
    }

    public Topic(String title) {

    }

    public void addQuestion(Question question) {

    }

    public void removeQuestion(Question question) {

    }

    //public getters for Firebase
    public String getTitle() {
        return title;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }
}
