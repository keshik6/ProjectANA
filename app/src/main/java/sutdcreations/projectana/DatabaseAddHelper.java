package sutdcreations.projectana;



import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sutdcreations.classes.Answer;
import sutdcreations.classes.Question;
import sutdcreations.classes.Subject;
import sutdcreations.classes.Topic;

/**
 * Created by Beng Haun on 3/12/2017.
 */

public class DatabaseAddHelper {
    //adds a new subject to Firebase
    public static void addSubject(FirebaseDatabase database, Subject subject){
        //get Firebase reference for all subjects in Firebase
        DatabaseReference subjDatabaseReference = database.getReference().child("Subjects");
        //create new child under Subjects node in Firebase, and set the value to be the Subject object
        subjDatabaseReference.child(subject.getKey()).setValue(subject);
    }

    //adds a Topic to an existing Subject, updates both the Topic and Subject in Firebase
    public static void addTopic(FirebaseDatabase database, Subject subject, Topic topic){
        //get Firebase reference for particular subject that we are adding the Topic to
        final DatabaseReference subjDatabaseReference = database.getReference().child("Subjects").child(subject.getKey());

        //add topic to the subject, update the Firebase for the subject
        subject.addTopic(topic);
        subjDatabaseReference.setValue(subject);

        //create new child under Topics node in Firebase and set the value
        DatabaseReference topicDatabaseReference = database.getReference().child("Topics");
        topicDatabaseReference.child(topic.getKey()).setValue(topic);
    }

    //adds a question to an existing Topic, updates the Topic in Firebase and adds a new Question to Firebase
    public static void addQuestion(FirebaseDatabase database, Question question, Topic topic){
        //get Firebase reference for particular topic that we are adding question to
        DatabaseReference topicDatabaseReference = database.getReference().child("Topics").child(topic.getKey());

        //add question to the topic, update the Firebase for the topic
        topic.addQuestion(question);
        topicDatabaseReference.setValue(topic);

        //create new child under Questions node in Firebase and set the value
        DatabaseReference questionDatabaseReference = database.getReference().child("Questions").child(question.getKey());
        questionDatabaseReference.setValue(question);
    }

    //adds an answer to a question
    public static void addAnswer(FirebaseDatabase database, Answer answer, Question question){
        DatabaseReference questionDatabaseReference = database.getReference().child("Questions").child(question.getKey());
        question.addAnswer(answer);
        questionDatabaseReference.setValue(question);
    }
}
