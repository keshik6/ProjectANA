package sutdcreations.classes;

import java.util.ArrayList;

/**
 * Created by Beng Haun on 2/12/2017.
 */

public class Feedback {

    int understand = 0;
    int dont_understand = 0;
    ArrayList<Student> voted = new ArrayList<>();
    String key;
    public Feedback(){
        //default constructor for Firebase
    }

    public Feedback(Question question){
        key = question.getKey();
    }

    public void incUnderstand(Student user){
        voted.add(user);
        understand+=1;
    }

    public void decUnderstand(Student user){
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

    public ArrayList<Student> getVoted() {
        return voted;
    }

    public String getKey(){return key;}
}