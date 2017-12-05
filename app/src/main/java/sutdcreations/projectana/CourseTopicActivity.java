package sutdcreations.projectana;

import android.content.Intent;
import android.provider.ContactsContract;
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

import java.util.ArrayList;

import sutdcreations.classes.Subject;
import sutdcreations.classes.Topic;

public class CourseTopicActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private Subject subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_topic);
        final String subjCode = getIntent().getStringExtra("subjectCode");
        database = FirebaseDatabase.getInstance();

        //get topic data from firebase
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
}
