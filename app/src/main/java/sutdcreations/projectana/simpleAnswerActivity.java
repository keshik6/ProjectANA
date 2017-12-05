package sutdcreations.projectana;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sutdcreations.classes.Answer;
import sutdcreations.classes.Feedback;
import sutdcreations.classes.Question;
import sutdcreations.classes.Student;
import sutdcreations.classes.Teacher;
import sutdcreations.classes.User;

public class simpleAnswerActivity extends AppCompatActivity {
    FirebaseDatabase database;
    Question question;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_answer);
        database = FirebaseDatabase.getInstance();

        //get question object from Firebase
        Log.i("questionKey",getIntent().getStringExtra("questionKey"));
        DatabaseReference questionRef = database.getReference().child("Questions").child(getIntent().getStringExtra("questionKey"));
        questionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                question = dataSnapshot.getValue(Question.class);
                updateLayout();
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
        TextView questionBody = new TextView(this);
        questionBody.setText(question.getBody());
        layout.addView(questionTitle);
        layout.addView(questionBody);
        for (Answer answer : question.getAnswers()){
            TextView answerView = new TextView(this);
            answerView.setText(answer.getBody());
            layout.addView(answerView);
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
    }
}
