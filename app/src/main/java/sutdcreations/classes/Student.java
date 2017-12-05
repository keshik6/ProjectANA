package sutdcreations.classes;

/**
 * Created by Beng Haun on 2/12/2017.
 */

public class Student extends User {
    double participation;

    public Student(){
        super();
        //public constructor for Firebase
    }
    public Student(String email_add, String uid) {
        super(email_add, uid);
    }

    public void addSubject(Subject subject){
        subjects.add(subject);
    }

    public void increaseParticipation(double d) {
        participation+=d;
    }

    //public getter for Firebase
    public double getParticipation(){return participation;}

    

}