package sutdcreations.projectana;

import android.content.DialogInterface;
import android.content.Intent;
import android.renderscript.Sampler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sutdcreations.classes.Answer;
import sutdcreations.classes.Feedback;
import sutdcreations.classes.Question;
import sutdcreations.classes.Student;
import sutdcreations.classes.Teacher;
import sutdcreations.classes.User;

public class simpleAnswerActivity extends AppCompatActivity {
    FirebaseDatabase database;
    Question question;
    User user;
    boolean inForeground = true;
    boolean waitingForFeedback;
    Button newAnswerBut;
    ValueEventListener listener; //keep track of if a listener is added to questionRef, make sure only one is added

    @Override
    protected void onPause(){
        super.onPause();
        Log.i("debugAlert","answer list no longer in foreground");
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
        Log.i("debugAlert","onCreate called in answer list");
        setContentView(R.layout.activity_simple_answer);
        database = FirebaseDatabase.getInstance();
        user = ((GlobalData) getApplication()).getUser();
        newAnswerBut = findViewById(R.id.newAnswerBut);
        if (listener == null) { //be sure to only add one listener
            Log.i("debugAlert","adding listener");
            listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    question = dataSnapshot.getValue(Question.class);
                    updateLayout();
                    if (question.isLive() && user instanceof Student){
                        Log.i("debugAlert","calling waitForFeedback in answer");
                        waitForFeedback();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            //get question object from Firebase
            Log.i("questionKey", getIntent().getStringExtra("questionKey"));
            DatabaseReference questionRef = database.getReference().child("Questions").child(getIntent().getStringExtra("questionKey"));
            questionRef.addValueEventListener(listener);
        }
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
        if (!waitingForFeedback) { //only wait for feedback on one instance
            waitingForFeedback = true;
            DatabaseReference feedbackRef = database.getReference().child("Feedback");
            feedbackRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.i("debugAlert", "onChildAdded called in answer list");
                    Feedback feedback = dataSnapshot.getValue(Feedback.class);
                    //do nothing if student has already voted
                    if (containsStudent(feedback.getVoted(), user)) {
                        Log.i("debugAlert", "student has already voted");
                        return;
                    }
                    //show alert if feedback is related to current question
                    if (feedback.getKey().contains(question.getKey()) && inForeground) {
                        Log.i("debugAlert", "showAlertDialog called in answer list");
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

    public void updateLayout(){
        LinearLayout layout = findViewById(R.id.simpleAnswerLayout);
        layout.removeAllViews();

        //add questions and answers as TextView
        TextView questionTitle = new TextView(this);
        questionTitle.setText(question.getTitle());
        questionTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        questionTitle.setTextSize(20);
        TextView questionBody = new TextView(this);
        questionBody.setText(question.getBody());
        questionBody.setTextSize(15);
        questionBody.setPadding(0,0,0,50);
        layout.addView(questionTitle);
        layout.addView(questionBody);
        for (Answer answer : question.getAnswers()){
            TextView answerView = new TextView(this);
            answerView.setText(answer.getBody());
            TextView postedBy = new TextView(this);
            postedBy.setText("Posted by: "+question.getAnimalMap().get(answer.getAnswerer().getUid()));
            postedBy.setPadding(0,0,0,50);
            layout.addView(answerView);
            layout.addView(postedBy);
        }

        User user = ((GlobalData) getApplication()).getUser();

        //add feedback request button for teachers
        if (user instanceof Teacher){
            Button feedbackRequest = new Button(this);
            feedbackRequest.setText("Request for feedback");
            feedbackRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    question.setFeedback(true);
                    DatabaseAddHelper.updateQuestion(database,question);
                    Feedback feedback = new Feedback(question);
                    DatabaseAddHelper.addFeedback(database,feedback);
                    Intent intent = new Intent(getApplicationContext(), FeedbackActivity.class);
                    intent.putExtra("feedbackKey",question.getKey());
                    startActivity(intent);
                }
            });
            layout.addView(feedbackRequest);
        }

        //add button to give feedback for students if teacher has asked for feedback
        if (user instanceof Student){
            Log.i("feedback status: ",""+question.isFeedback());
            if (question.isFeedback()){
                Button giveFeedback = new Button(this);
                giveFeedback.setText("Give feedback");
                giveFeedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(),FeedbackActivity.class);
                        intent.putExtra("feedbackKey",question.getKey());
                        startActivity(intent);
                    }
                });
                layout.addView(giveFeedback);
            }
        }

        layout.addView(newAnswerBut);
    }

    //onClick method for button that leads to add question activity
    public void onClickGoToAddAnswer(View v){
        Intent intent = new Intent(this, addAnswerActivity.class);
        intent.putExtra("questionKey",getIntent().getStringExtra("questionKey"));
        startActivity(intent);
    }
}
