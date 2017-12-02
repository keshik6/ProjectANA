package sutdcreations.projectana;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;

import sutdcreations.classes.Student;
import sutdcreations.classes.Subject;
import sutdcreations.classes.User;
import sutdcreations.projectana.R;
/* This activity is meant for programmatically adding objects to the Firebase database.
For example, it can be used to add more users into the database.
  */
public class DatabaseMgmtActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_mgmt);
        String uid = "qCMd1yt5mMcJb9n3fHJrNWRBbJb2";
        User user = new Student("examplestudent@gmail.com");
        user.addSubject(new Subject("50.001","Introduction to Infosys"));
        user.addSubject(new Subject("50.002","Computation Structures"));
        user.addSubject(new Subject("50.004", "Introduction to Algorithms"));
        database.getReference().child("UserInfo").child(uid).setValue(user);

    }
}
