package sutdcreations.classes;

import java.util.ArrayList;

/* TODO: Implement these classes. */

public abstract class User {

    ArrayList<Subject> subjects = new ArrayList<>();
    String email_add;
    String uid;
    String user_name;

    public abstract void addSubject (Subject subject);
    public User(String email_add, String uid){
        this.email_add=email_add;
        this.uid = uid;
    }
    public User(){};

    public void setEmail_add(String email){
        email_add = email;
    }

    public void setUser_name(String name){
        user_name = name;
    }

    public String getUser_name(){
        return user_name;
    }

    public void removeSubject(Subject s){subjects.remove(s);}
    //public getters for Firebase
    public ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public String getEmail_add() {
        return email_add;
    }

    public String getUid(){return uid;}
}














