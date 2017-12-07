package sutdcreations.projectana;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sutdcreations.classes.Student;
import sutdcreations.classes.Subject;
import sutdcreations.classes.Teacher;
import sutdcreations.classes.User;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private FirebaseDatabase database;
    Button snoopButton;
    User user;
    String uid;
    String user_type;
    ArrayList<Subject> subjects = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Button courseButton = (Button) findViewById(R.id.testCourse);
        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DatabaseMgmtActivity.class);
                startActivity(intent);
            }
        });

        Button profileButton = (Button) findViewById(R.id.testProfile);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        Button feedbackButton = (Button) findViewById(R.id.testFeedback);
        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });

        snoopButton = (Button) findViewById(R.id.testSnoop);
        snoopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SnoopActivity.class);
                startActivity(intent);
            }
        });
        snoopButton.setVisibility(View.INVISIBLE);

        database = FirebaseDatabase.getInstance();
        //check if current user is teacher or student
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userTypeRef = database.getReference().child("UserType").child(uid);
        userTypeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_type = dataSnapshot.getValue(String.class);
                Log.i("userType",user_type);
                //Show snoop button if user is a teacher
                if (user_type.equals("teacher")){
                    snoopButton.setVisibility(View.VISIBLE);
                }

                //create user classes from data stored in Firebase
                DatabaseReference userInfoRef = database.getReference().child("UserInfo").child(uid);
                userInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (user_type!=null) {
                            if (user_type.equals("student")) {
                                user = dataSnapshot.getValue(Student.class);
                            }

                            if (user_type.equals("teacher")) {
                                user = dataSnapshot.getValue(Teacher.class);

                            }
                        }
                        //set user as global data to be accessed from any activity
                        ((GlobalData) getApplication()).setUser(user);
                        //get full subjects from Firebase
                        getSubjects();
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getSubjects(){
        DatabaseReference subjRef = database.getReference().child("Subjects");
        subjRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> subjKeys = new ArrayList<>();
                for (Subject subj : user.getSubjects()){
                    subjKeys.add(subj.getKey());
                }
                ArrayList<Subject> subjects = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (subjKeys.contains(snapshot.getKey())){
                        Subject subject = snapshot.getValue(Subject.class);
                        subjects.add(subject);
                    }
                }
                addSubjectsToLayout(subjects);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        subjRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Subject subj = dataSnapshot.getValue(Subject.class);
//                subjects.add(subj);
//                addSubjectsToLayout(subjects);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    public void addSubjectsToLayout(ArrayList<Subject> subjects){
        LinearLayout layout = findViewById(R.id.HoldMyButtons);
        for (Subject s: subjects){
            Log.i("isLive",""+s.isLive());
            final Subject subj = s;
            Button button = new Button(this);
            if (s.isLive()){
                button.setText(s.getSubjectCode()+" "+s.getSubjectTitle() + " "+ "(Live)");
            }
            else button.setText(s.getSubjectCode()+" "+s.getSubjectTitle());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CourseTopicActivity.class);
                    //send subject code of clicked subject to CourseTopicActivity
                    //this allows CourseTopicActivity to load the topics for that subject from Firebase
                    intent.putExtra("subjectCode",subj.getSubjectCode().replace(".",""));
                    startActivity(intent);
                }
            });
            layout.addView(button);
        }

        //add button to create new subjects for teachers
        if (user instanceof Teacher){
            Button newSubjectButton = new Button(this);
            newSubjectButton.setText("Create new subject");
            newSubjectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), AddSubjectActivity.class);
                    startActivity(intent);
                }
            });
            layout.addView(newSubjectButton);
        }
    }

}
