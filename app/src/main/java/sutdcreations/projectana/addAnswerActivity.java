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
import sutdcreations.classes.Student;
import sutdcreations.classes.User;

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
        final String questionKey = getIntent().getStringExtra("questionKey");
        final User answerer = ((GlobalData) getApplication()).getUser();

        //display toast if text box is empty
        if (answerString.equals("")){
            Toast.makeText(this,"Please enter your answer",Toast.LENGTH_SHORT).show();
            return;
        }

        //add 1 to number of answers by student for this subject
        if (answerer instanceof Student){
            Student student = (Student) answerer;
            String courseCode = questionKey.split(" ")[0];
            if (student.getAnswersMap().containsKey(courseCode)) {
                student.getAnswersMap().put(courseCode, student.getAnswersMap().get(courseCode) + 1);
            }
            else{
                student.getAnswersMap().put(courseCode, 1);
            }
            DatabaseAddHelper.updateStudent(FirebaseDatabase.getInstance(),student);
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

        //create notification for asker, add to Firebase
        String askerUid = getIntent().getStringExtra("askerUid");
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(askerUid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Student asker = dataSnapshot.getValue(Student.class);
                asker.addNotification(questionKey);
                DatabaseAddHelper.updateStudent(FirebaseDatabase.getInstance(),asker);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //go back to question
        Intent intent = new Intent(this, simpleAnswerActivity.class);
        intent.putExtra("questionKey",questionKey);
        intent.putExtra("topicKey", getIntent().getStringExtra("topicKey"));
        startActivity(intent);
        finish();
    }
}
