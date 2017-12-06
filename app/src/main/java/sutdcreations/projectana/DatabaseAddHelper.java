package sutdcreations.projectana;



import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sutdcreations.classes.Answer;
import sutdcreations.classes.Feedback;
import sutdcreations.classes.Question;
import sutdcreations.classes.Subject;
import sutdcreations.classes.Topic;
import sutdcreations.classes.User;

/**
 * Created by Beng Haun on 3/12/2017.
 */

public class DatabaseAddHelper {

    //adds a new subject to Firebase
    public static void addSubject(FirebaseDatabase database, Subject subject){
        //get Firebase reference for all subjects in Firebase
        DatabaseReference subjRef = database.getReference().child("Subjects");
        //create new child under Subjects node in Firebase, and set the value to be the Subject object
        subjRef.child(subject.getKey()).setValue(subject);
    }

    //adds a Topic to an existing Subject, updates both the Topic and Subject in Firebase
    public static void addTopic(FirebaseDatabase database, Subject subject, Topic topic){
        //get Firebase reference for particular subject that we are adding the Topic to
        final DatabaseReference subjRef = database.getReference().child("Subjects").child(subject.getKey());

        //add topic to the subject, update the Firebase for the subject
        subject.addTopic(topic);
        subjRef.setValue(subject);

        //create new child under Topics node in Firebase and set the value
        DatabaseReference topicRef = database.getReference().child("Topics");
        topicRef.child(topic.getKey()).setValue(topic);
    }

    //adds a question to an existing Topic, updates the Topic in Firebase and adds a new Question to Firebase
    public static void addQuestion(FirebaseDatabase database, Question question, Topic topic){
        //get Firebase reference for particular topic that we are adding question to
        DatabaseReference topicRef = database.getReference().child("Topics").child(topic.getKey());

        //add question to the topic, update the Firebase for the topic
        topic.addQuestion(question);
        topicRef.setValue(topic);

        //create new child under Questions node in Firebase and set the value
        DatabaseReference questionRef = database.getReference().child("Questions").child(question.getKey());
        questionRef.setValue(question);
    }

    //adds an answer to a question
    public static void addAnswer(FirebaseDatabase database, Answer answer, Question question){
        DatabaseReference questionRef = database.getReference().child("Questions").child(question.getKey());
        question.addAnswer(answer);
        questionRef.setValue(question);
    }

    //creates a new feedback instance for a question
    public static void addFeedback(FirebaseDatabase database, Feedback feedback){
        DatabaseReference feedbackRef = database.getReference().child("Feedback").child(feedback.getKey());
        feedbackRef.setValue(feedback);
    }

    //update existing subject in Firebase
    public static void updateSubject(FirebaseDatabase database, Subject subject){
        addSubject(database,subject);
    }

    //update existing topic in Firebase
    public static void updateTopic(FirebaseDatabase database, Topic topic){
        DatabaseReference databaseReference = database.getReference().child("Topics").child(topic.getKey());
        databaseReference.setValue(topic);
    }

    //update existing question in Firebase
    public static void updateQuestion(FirebaseDatabase database, Question question){
        DatabaseReference databaseReference = database.getReference().child("Questions").child(question.getKey());
        databaseReference.setValue(question);
    }

    //update an existing answer
    public static void updateAnswer(FirebaseDatabase database, Question question, Answer answer){
        addAnswer(database,answer,question);
    }

    //update existing feedback
    public static void updateFeedback(FirebaseDatabase database, Feedback feedback){
        addFeedback(database,feedback);
    }

    public static void updateUser(FirebaseDatabase database, User user){
        DatabaseReference userRef = database.getReference().child("UserInfo").child(user.getUid());
        userRef.setValue(user);
    }


}
