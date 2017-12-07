package sutdcreations.projectana;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sutdcreations.classes.Student;
import sutdcreations.classes.Subject;
import sutdcreations.classes.User;

public class ProfileActivity extends AppCompatActivity {
    TextView userNameTF;
    TextView coursesTF;
    TextView noOfQuestionsTF;
    TextView noOfRepliesTF;
    TextView questionsLabel;
    TextView repliesLabel;


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

        //Get the user object
        User user = ((GlobalData) getApplication()).getUser();

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

        //Get the no.Of Questions and replies
        if (user instanceof Student){
            Student student = (Student)user;
<<<<<<< HEAD
            //noOfQuestionsTF.setText(String.valueOf());
            //noOfRepliesTF.setText(String.valueOf());
=======
            //noOfQuestionsTF.setText(String.valueOf(student.getQuestions_count()));
            //noOfRepliesTF.setText(String.valueOf(student.getReplies_count()));
>>>>>>> benghaun
        }
        else{
            noOfQuestionsTF.setVisibility(View.INVISIBLE);
            noOfRepliesTF.setVisibility(View.INVISIBLE);
            questionsLabel.setVisibility(View.INVISIBLE);
            repliesLabel.setVisibility(View.INVISIBLE);
        }





    }
}