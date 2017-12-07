package sutdcreations.projectana;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sutdcreations.classes.Question;
import sutdcreations.classes.Student;
import sutdcreations.classes.Teacher;
import sutdcreations.classes.Topic;
import sutdcreations.classes.User;

public class AskQuestionActivity extends AppCompatActivity {
    EditText questionTitle;
    EditText questionBody;
    EditText tag;
    ArrayList<String> tags = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        questionTitle = findViewById(R.id.questionTitle);
        questionBody = findViewById(R.id.questionBody);
        tag = findViewById(R.id.tag);

    }

    public void onClickAddTag(View v){
        String tagString = tag.getText().toString();

        //show toast if no tag was entered
        if (tagString.equals("")){
            Toast.makeText(this,"Please enter a tag",Toast.LENGTH_SHORT).show();
            return;
        }

        //add tag to ArrayList of tags, display tag on screen
        tags.add(tagString);
        LinearLayout tagLayout = findViewById(R.id.tagLayout);
        TextView tagView = new TextView(this);
        tagView.setText(tagString);
        tagView.setPadding(0,0,50,0);
        tagLayout.addView(tagView);
        tag.setText("");
    }


    public void onClickPostQuestion(View v){
        //show toast if question title or body is empty
        if (questionTitle.getText().toString().equals("")){
            Toast.makeText(this,"Please enter question title",Toast.LENGTH_SHORT).show();
            return;
        }
        if (questionBody.getText().toString().equals("")){
            Toast.makeText(this,"Please enter question body",Toast.LENGTH_SHORT).show();
            return;
        }

        Student asker = (Student) ((GlobalData) getApplication()).getUser();

        //create question object and post it to Firebase
        final Question question = new Question(questionTitle.getText().toString(),questionBody.getText().toString(),tags,asker);
        String topicKey = getIntent().getStringExtra("topicKey");
        DatabaseReference topicRef = FirebaseDatabase.getInstance().getReference().child("Topics").child(topicKey);
        topicRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Topic topic = dataSnapshot.getValue(Topic.class);
                DatabaseAddHelper.addQuestion(FirebaseDatabase.getInstance(),question,topic);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //go back to list of questions
        Intent intent = new Intent(this, simpleQuestionActivity.class);
        intent.putExtra("topicTitle",topicKey);
        startActivity(intent);

    }
}
