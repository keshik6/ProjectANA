package sutdcreations.projectana;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sutdcreations.classes.Student;
import sutdcreations.classes.Subject;
import sutdcreations.classes.Topic;
import sutdcreations.classes.User;

public class ProfileActivity extends AppCompatActivity {
    TextView userNameTF;
    TextView coursesTF;
    TextView noOfQuestionsTF;
    TextView noOfRepliesTF;
    TextView questionsLabel;
    TextView repliesLabel;
    TextView gradesLabel;
    TextView gradesTF;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userNameTF = (TextView)findViewById(R.id.NameTF);
        coursesTF = (TextView)findViewById(R.id.CoursesTF);
        noOfQuestionsTF = (TextView)findViewById(R.id.NoOfQuestionsTF);
        noOfRepliesTF = (TextView)findViewById(R.id.NoOfRepliesTF);

        //Get reference to labels
        questionsLabel = (TextView)findViewById(R.id.QuestionsLabel);
        repliesLabel = (TextView)findViewById(R.id.ReplyLabel);

        //Get the reference to grades label and text box
        gradesLabel = (TextView)findViewById(R.id.GradesLabel);
        gradesTF = (TextView)findViewById(R.id.GradesTF);


        //Get the user object
        user = ((GlobalData) getApplication()).getUser();

        //Get user Name
        String user_name = user.getUser_name();
        userNameTF.setText(user_name);

        //Get course codes for all the subjects enrolled
        ArrayList<Subject> subjectsList = user.getSubjects();
        String courseCodes = "";
        for (Subject s: subjectsList){
            courseCodes += s.getSubjectCode();
            if (subjectsList.get(subjectsList.size()-1) != s){
                courseCodes += "\n";
            }
        }
        coursesTF.setText(courseCodes);

        //Get the objects from the database
        DatabaseReference allTopicsRef = FirebaseDatabase.getInstance().getReference().child("Topics");
        allTopicsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Topic> topics = new ArrayList<>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    Topic topic = childSnapshot.getValue(Topic.class);
                    topics.add(topic);
                }
                updateScore(topics);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    //Update the scores subsequently
    private void updateScore(ArrayList<Topic> topics){
        //Get the no.Of Questions and replies
        if (user instanceof Student){
            Student student = (Student)user;
            noOfQuestionsTF.setText(String.valueOf(student.getTotalQuestionsAsked()));
            noOfRepliesTF.setText(String.valueOf(student.getTotalReplies()));
            gradesTF.setText(student.calculateScore(topics,"Student")[0]);
        }
        else{
            //Set these fields to invisible for the teacher's version
            noOfQuestionsTF.setVisibility(View.INVISIBLE);
            noOfRepliesTF.setVisibility(View.INVISIBLE);
            questionsLabel.setVisibility(View.INVISIBLE);
            repliesLabel.setVisibility(View.INVISIBLE);
            gradesLabel.setVisibility(View.INVISIBLE);
            gradesTF.setVisibility(View.INVISIBLE);
        }
    }
}