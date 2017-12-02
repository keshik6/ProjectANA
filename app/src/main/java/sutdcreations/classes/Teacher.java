package sutdcreations.classes;

/**
 * Created by Beng Haun on 2/12/2017.
 */

public class Teacher extends User {

    public Teacher(){
        super();
        //default constructor for Firebase
    }

    public Teacher(String email_add) {
        super(email_add);
    }

    public void addSubject(Subject subject){
        subjects.add(subject);
    }

    public void createSubject(int subjectCode, String subjectTitle) {
    }

    public void createTopic(String title, Subject subject) {

    }

    public double getParticipation(Student student) {
        return student.participation;
    }

    public void markasCompleted(Question question) {

    }
}
