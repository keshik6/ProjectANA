package sutdcreations.projectana;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import sutdcreations.classes.Subject;
import sutdcreations.classes.Teacher;

public class AddSubjectActivity extends AppCompatActivity {

    private EditText subjectCode;
    private EditText subjectTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        subjectCode = findViewById(R.id.subjectCode);
        subjectTitle = findViewById(R.id.subjectTitle);
    }

    //create new Subject object and add it to Firebase
    public void onClickAddSubject(View v){
        String subjectCodeString = subjectCode.getText().toString();
        String subjectTitleString = subjectTitle.getText().toString();

        //display toast if text box for subject code is empty
        if (subjectCodeString.equals("")){
            Toast.makeText(this,"Please enter the subject code",Toast.LENGTH_SHORT).show();
            return;
        }

        //display toast if text box for subject title is empty
        if (subjectCodeString.equals("")){
            Toast.makeText(this,"Please enter the subject title",Toast.LENGTH_SHORT).show();
            return;
        }


        //create Subject object, add to Firebase
        Subject subject = new Subject(subjectCodeString,subjectTitleString);
        DatabaseAddHelper.addSubject(FirebaseDatabase.getInstance(),subject);

        //add subject to this teacher's list of subjects, update Firebase
        Teacher teacher = (Teacher) ((GlobalData) getApplication()).getUser();
        teacher.addSubject(subject);
        DatabaseAddHelper.updateUser(FirebaseDatabase.getInstance(),teacher);

        //go back to MainActivity
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
