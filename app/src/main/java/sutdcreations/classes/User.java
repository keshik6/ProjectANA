package sutdcreations.classes;

import java.util.ArrayList;

/* TODO: Implement these classes. */

public class User {

    ArrayList<Subject> subjects;
    String email_add;

    public User(String email_add) {
        public void addSubject (Subject subject) {
    }
}

public class Teacher extends User{

    public Teacher(String email_add) {

    }
    public Subject createSubject(int subjectCode, String subjectTitle) {

    }
    public Topic createTopic(String title) {

    }
    public double getParticipation(Student student) {

    }
    public void markasCompleted(Question question){

    }
}

public class Student extends User{
    double participation;

    public Student(String email_add) {

    }
    public void increaseParticipation(double d) {

    }
}

public class Subject{

    int subjectCode;
    String subjectTitle;
    ArrayList<Topic> topics;
    boolean isLive;

    public Subject(int subjectCode, String subjectTitle) {

    }
    public void addTopic(Topic topic) {

    }
    public void toggleLive() {

    }
}

public class Topic{

    String title;
    ArrayList<Question> questions;

    public Topic(String title) {

    }
    public void addQuestion(Question question) {

    }
    public void removeQuestion(Question question) {

    }
}

public class Question{

    User asker;
    String title;
    String body;
    int votes;
    ArrayList<User> voted;
    ArrayList<Answer> answers;
    ArrayList<String> tags;
    boolean isClosed;

    public Question(String title, String body,ArrayList<String> tags) {}
    public void upVote() {}
    public void downVote() {}
    public void addAnswer(Answer answer) {}
    public void close() {}
    public void removeAnswer(Answer answer) {}
}

public class Answer{
    User answerer;
    String body;
    int votes;
    ArrayList<User> voted;

    public Answer(String title) {

    }
    public void upVote() {

    }
    public void downVote() {

    }
    public void edit(String string) {

    }
}

public class Feedback{

    int understand;
    int dont_understand;
    ArrayList<User> voted;

}