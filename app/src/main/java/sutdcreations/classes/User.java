package sutdcreations.classes;

import java.util.ArrayList;

import sutdcreations.projectana.GlobalData;

/* TODO: Implement these classes. */

public abstract class User {

    ArrayList<Subject> subjects = new ArrayList<>();
    String email_add;
    String uid;
    public abstract void addSubject (Subject subject);
    public User(String email_add, String uid){
        this.email_add=email_add;
        this.uid = uid;
    }
    public User(){};
    public void removeSubject(Subject s){subjects.remove(s);}
    //public getters for Firebase
    public ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public String getEmail_add() {
        return email_add;
    }

    public String getUid(){return uid;}

    public String getUserName(){
        return null;
    }

}














