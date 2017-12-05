package sutdcreations.projectana;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sutdcreations.classes.Feedback;
import sutdcreations.classes.Student;
import sutdcreations.classes.Subject;
import sutdcreations.classes.Topic;
import sutdcreations.classes.User;

public class CourseTopicActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private Subject subject;
    private User user;
    boolean inForeground = true;


    @Override
    protected void onPause(){
        super.onPause();
        Log.i("debugAlert", "topic list no longer in foreground");
        inForeground = false;
    }

    @Override
    protected void onResume(){
        super.onResume();
        inForeground = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("debugAlert","onCreate called");
        setContentView(R.layout.activity_course_topic);
        final String subjCode = getIntent().getStringExtra("subjectCode");
        database = FirebaseDatabase.getInstance();
        user = ((GlobalData) getApplication()).getUser();

        //get topic data from Firebase
        DatabaseReference topicRef = database.getReference().child("Topics");
        topicRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Topic> topics = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (snapshot.getKey().contains(subjCode)){
                        Topic topic = snapshot.getValue(Topic.class);
                        topics.add(topic);
                    }
                }
                //add topics to layout
                addTopicsToLayout(topics);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //get subject data from Firebase
        DatabaseReference subjRef = database.getReference().child("Subjects").child(subjCode);
        subjRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                subject = dataSnapshot.getValue(Subject.class);

                //listen for new feedback event if subject is live, and if user is a student
                if (subject.isLive() && user instanceof Student){
                    Log.i("debugAlert","calling waitForFeedback in topic list");
                    waitForFeedback();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*
    check if a list of users contains a certain user. The standard ArrayList.contains method will not work here
    as the references to the student object inside the list and outside the list will be different even though they are referring
     to the same student, due to them being retrieved from Firebase at different times.
    */
    public boolean containsStudent(ArrayList<Student> users, User user){
        for (User u : users){
            if (u.getUid().equals(user.getUid())){
                return true;
            }
        }
        return false;
    }

    //check for new Feedback objects being added to the database. If Feedback is related to current live subject, show AlertDialog to prompt user to give feedback
    public void waitForFeedback(){
        DatabaseReference feedbackRef = database.getReference().child("Feedback");
        feedbackRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("debugAlert", "onChildAdded called in topic list");
                Feedback feedback = dataSnapshot.getValue(Feedback.class);
                //do nothing if student has already voted
                if (containsStudent(feedback.getVoted(),user)){
                    return;
                }
                //show alert if feedback is related to current subject
                if (feedback.getKey().contains(subject.getKey()) && inForeground){
                    Log.i("debugAlert", "showAlertDialog called in topic list");
                    showFeedbackAlertDialog(feedback);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showFeedbackAlertDialog(Feedback feedback){
        final Feedback final_fb = feedback;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Feedback requested");
        alertBuilder.setMessage("Your teacher is asking for feedback. Did you understand the explanation?");
        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                understand(final_fb);
            }
        });
        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dontUnderstand(final_fb);
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        if (!this.isFinishing() && !containsStudent(feedback.getVoted(),user) && inForeground) {
            alertDialog.show();
        }
    }

    private void addTopicsToLayout(ArrayList<Topic> topics){
        LinearLayout layout = findViewById(R.id.courseTopicLayout);
        for (Topic topic : topics){
            Button button = new Button(this);
            final Topic final_topic = topic;
            final String topicTitle = topic.getTitle();
            if (topic.isLive()) button.setText(topicTitle + " " + "(Live)");
            else button.setText(topicTitle);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),simpleQuestionActivity.class);
                    //put subject code followed by topic title (e.g 50004 Dynamic Programming) to be used to search for the Topic in Firebase
                    intent.putExtra("topicTitle",final_topic.getKey());
                    startActivity(intent);
                }
            });
            layout.addView(button);
        }
    }

    //increment the understand attribute in the existing Feedback object in Firebase
    public void understand(Feedback feedback){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Feedback").child(feedback.getKey());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Feedback feedback = dataSnapshot.getValue(Feedback.class);
                feedback.incUnderstand((Student) user);
                DatabaseAddHelper.updateFeedback(FirebaseDatabase.getInstance(),feedback);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //increment the dont_understand attribute in the existing Feedback object in Firebase
    public void dontUnderstand(Feedback feedback){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Feedback").child(feedback.getKey());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Feedback feedback = dataSnapshot.getValue(Feedback.class);
                feedback.decUnderstand((Student) user);
                DatabaseAddHelper.updateFeedback(FirebaseDatabase.getInstance(), feedback);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
