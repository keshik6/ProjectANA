package sutdcreations.projectana;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sutdcreations.classes.Subject;
import sutdcreations.classes.Topic;

public class CourseTopicActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private Subject subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_topic);
        String subjCode = getIntent().getStringExtra("subjectCode");
        database = FirebaseDatabase.getInstance();

        //get subject data from firebase
        DatabaseReference subjectRef = database.getReference().child("Subjects").child(subjCode);
        subjectRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                subject = dataSnapshot.getValue(Subject.class);
                //add topics to layout
                addTopicsToLayout();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void addTopicsToLayout(){
        LinearLayout layout = findViewById(R.id.courseTopicLayout);
        for (Topic topic : subject.getTopics()){
            Button button = new Button(this);
            final String topicTitle = topic.getTitle();
            button.setText(topicTitle);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),QuestionListActivity.class);
                    //put subject code followed by topic title (e.g 50004 Dynamic Programming) to be used to search for the Topic in Firebase
                    intent.putExtra("topicTitle",subject.getSubjectCode().replace(".","") + " " +topicTitle);
                    startActivity(intent);
                }
            });
            layout.addView(button);
        }
    }
}
