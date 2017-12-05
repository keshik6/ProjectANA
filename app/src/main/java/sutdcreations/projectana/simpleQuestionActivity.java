package sutdcreations.projectana;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sutdcreations.classes.Question;

public class simpleQuestionActivity extends AppCompatActivity {
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_question);
        final String topicKey = getIntent().getStringExtra("topicTitle");
        database = FirebaseDatabase.getInstance();
        DatabaseReference questionReference = database.getReference().child("Questions");
        questionReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Question> questions = new ArrayList<>();

                //iterate through all questions
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Question question = postSnapshot.getValue(Question.class);

                    //add question to ArrayList if it is of the topic that you are interested in
                    if (question.getKey().contains(topicKey)){
                        questions.add(question);
                    }
                }
                addQuestionsToLayout(questions);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addQuestionsToLayout(final ArrayList<Question> questions){
        LinearLayout layout = findViewById(R.id.simpleQuestionLayout);
        for (Question question : questions){
            final Question final_question = question;
            Button button = new Button(this);
            if (question.isLive()) button.setText(question.getTitle()+ " (Live)");
            else button.setText(question.getTitle());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),simpleAnswerActivity.class);
                    intent.putExtra("questionKey",final_question.getKey());
                    startActivity(intent);
                }
            });
            layout.addView(button);
        }
    }
}
