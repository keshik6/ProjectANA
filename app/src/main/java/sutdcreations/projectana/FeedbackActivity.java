package sutdcreations.projectana;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sutdcreations.classes.Feedback;
import sutdcreations.classes.Question;
import sutdcreations.classes.Student;
import sutdcreations.classes.Teacher;
import sutdcreations.classes.User;
import sutdcreations.projectana.R;

public class FeedbackActivity extends AppCompatActivity {
    String feedbackKey;
    User user;
    String topicKey;
    Feedback feedback;
    boolean inForeground = true;

    @Override
    protected void onPause(){
        super.onPause();
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
        setContentView(R.layout.activity_feedback);
        user = ((GlobalData) getApplication()).getUser();
        feedbackKey = getIntent().getStringExtra("feedbackKey");
        topicKey = getIntent().getStringExtra("topicKey");

        //get feedback object from Firebase, update layout with necessary elements
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Feedback").child(feedbackKey);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                feedback = dataSnapshot.getValue(Feedback.class);
                if (feedback == null){
                    //Feedback session was ended by teacher, display toast and return to answer activity
                    if (inForeground) {
                        Toast.makeText(getApplicationContext(), "Feedback session has ended", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), simpleAnswerActivity.class);
                        intent.putExtra("questionKey", feedbackKey);
                        intent.putExtra("topicKey",topicKey);
                        startActivity(intent);
                        finish();
                    }
                }
                else {
                    //constantly update layout real-time with latest numbers from Firebase
                    updateLayout();
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

    //update layout based on data from feedback object
    public void updateLayout(){
        final LinearLayout layout = findViewById(R.id.secondaryFeedbackLayout);
        layout.removeAllViews();
        //update the numbers of students that understand/dont understand
        TextView understand = findViewById(R.id.understand);
        TextView dontUnderstand = findViewById(R.id.dontUnderstand);
        understand.setText(String.valueOf(feedback.getUnderstand()));
        dontUnderstand.setText(String.valueOf(feedback.getDont_understand()));

        //add voting buttons for students if they have not yet voted
        if (user instanceof Student && !containsStudent(feedback.getVoted(),user)){
            Button understandButton = new Button(this);
            understandButton.setText("I understand");
            understandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //layout.removeView(view);
                    understand();
                }
            });

            Button dontUnderstandButton = new Button(this);
            dontUnderstandButton.setText("I don't understand");
            dontUnderstandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //layout.removeView(view);
                    dontUnderstand();
                }
            });

            layout.addView(understandButton);
            layout.addView(dontUnderstandButton);
        }

        //add button to cancel feedback for teachers
        if (user instanceof Teacher){
            Button endFeedback = new Button(this);
            endFeedback.setText("End feedback");
            endFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //delete feedback object in Firebase
                    DatabaseReference feedbackRef = FirebaseDatabase.getInstance().getReference().child("Feedback").child(feedbackKey);
                    feedbackRef.removeValue();

                    //set feedback status of question to false
                    DatabaseReference questionRef = FirebaseDatabase.getInstance().getReference().child("Questions").child(feedbackKey);
                    questionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Question question = dataSnapshot.getValue(Question.class);
                            question.setFeedback(false);
                            Intent intent = new Intent(getApplicationContext(), simpleAnswerActivity.class);
                            intent.putExtra("questionKey",feedbackKey);
                            intent.putExtra("topicKey",topicKey);
                            startActivity(intent);
                            DatabaseAddHelper.updateQuestion(FirebaseDatabase.getInstance(),question);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            });
            layout.addView(endFeedback);
        }
    }

    //increment the understand attribute in the existing Feedback object in Firebase
    public void understand(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Feedback").child(feedbackKey);
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
    public void dontUnderstand(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Feedback").child(feedbackKey);
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
