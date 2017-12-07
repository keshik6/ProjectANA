package sutdcreations.projectana;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sutdcreations.classes.Answer;
import sutdcreations.classes.Question;

public class addAnswerActivity extends AppCompatActivity {
    private EditText answer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answer);
        answer = findViewById(R.id.answer);
    }

    public void onClickAddAnswer(View v){
        String answerString = answer.getText().toString();
        String questionKey = getIntent().getStringExtra("questionKey");
        //display toast if text box is empty
        if (answerString.equals("")){
            Toast.makeText(this,"Please enter your answer",Toast.LENGTH_SHORT).show();
            return;
        }

        //create answer object, add to Firebase
        final Answer answer = new Answer(answerString, ((GlobalData) getApplication()).getUser());
        DatabaseReference questionRef = FirebaseDatabase.getInstance().getReference().child("Questions").child(questionKey);
        questionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Question question = dataSnapshot.getValue(Question.class);

                DatabaseAddHelper.addAnswer(FirebaseDatabase.getInstance(),answer,question);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //go back to question
        Intent intent = new Intent(this, simpleAnswerActivity.class);
        intent.putExtra("questionKey",questionKey);
        startActivity(intent);
    }
}
