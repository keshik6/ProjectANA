package sutdcreations.classes;

import java.util.ArrayList;

/* TODO: Implement these classes. */

public abstract class User {

    ArrayList<Subject> subjects = new ArrayList<>();
    String email_add;
    public abstract void addSubject (Subject subject);
    public User(String email_add){
        this.email_add=email_add;
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
}














