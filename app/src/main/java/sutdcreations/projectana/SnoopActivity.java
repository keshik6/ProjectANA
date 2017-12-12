package sutdcreations.projectana;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import sutdcreations.classes.Student;

public class SnoopActivity extends AppCompatActivity {

    EditText StudentIDEditText;
    Button SearchBtn;
    TextView StudentNameTF;
    TextView NoOfQns;
    TextView NoOfAns;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snoop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        StudentIDEditText = (EditText)findViewById(R.id.studentSearch);
        SearchBtn = (Button)findViewById(R.id.searchBtn);
        StudentNameTF = (TextView)findViewById(R.id.studentNameTF);
        NoOfQns = (TextView)findViewById(R.id.noOfQtnTF);
        NoOfAns = (TextView)findViewById(R.id.noOfAnsTF);

        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int student_id = Integer.parseInt(StudentIDEditText.getText().toString());
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("UserInfo");
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Student> students = new ArrayList<>();
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                            Student student = childSnapshot.getValue(Student.class);
                            students.add(student);
                        }
                        //do whatever u want with the ArrayList of all students here
                        for (Student s: students){
                            if (String.valueOf(s.getStudent_id()).equals(null)){
                                continue;
                            }
                            else if (String.valueOf(s.getStudent_id()).equals(student_id)){
                                StudentNameTF.setText(s.getUser_name());

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }




}
