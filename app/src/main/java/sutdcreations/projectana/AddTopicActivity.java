package sutdcreations.projectana;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sutdcreations.classes.Subject;
import sutdcreations.classes.Topic;

public class AddTopicActivity extends AppCompatActivity {
    private EditText topicTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);
        topicTitle = findViewById(R.id.topicTitle);
    }

    //create new Topic, add it to Firebase
    public void onClickAddTopic(View v){
        final String topicTitleString = topicTitle.getText().toString();

        //show toast if topic title text box is blank
        if (topicTitleString.equals("")){
            Toast.makeText(this,"Please enter the topic title",Toast.LENGTH_SHORT).show();
        }

        //get the Subject from Firebase, add topic to it
        String subjectKey = getIntent().getStringExtra("subjectKey");
        DatabaseReference subjRef = FirebaseDatabase.getInstance().getReference().child("Subjects").child(subjectKey);
        subjRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Subject subject = dataSnapshot.getValue(Subject.class);
                Topic topic = new Topic(topicTitleString);
                DatabaseAddHelper.addTopic(FirebaseDatabase.getInstance(),subject,topic);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //go back to CourseTopicActivity
        Intent intent = new Intent(this,CourseTopicActivity.class);
        intent.putExtra("subjectCode",subjectKey);
        startActivity(intent);
        finish();
    }
}
