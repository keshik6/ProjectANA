package sutdcreations.projectana;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sutdcreations.classes.Font;
import sutdcreations.classes.Student;
import sutdcreations.classes.Subject;
import sutdcreations.classes.Teacher;
import sutdcreations.classes.User;


/**
 * A simple {@link Fragment} subclass.
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class MainFragment extends Fragment {
    Button snoopButton;
    FirebaseDatabase database;
    String uid;
    String user_type;
    User user;
    Activity parentActivity;
    View parentView;

//    int white = Color.parseColor("#FFFFFF");
//    int red = Color.parseColor("#AB0606");

    int turquoise = Color.parseColor("#60C3CE");
    int black = Color.parseColor("#000000");

//    Typeface opensans_regular = Typeface.createFromAsset(getActivity().getAssets(),"fonts/opensans_regular.ttf");

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        parentView = view;
        Button courseButton = (Button) getView().findViewById(R.id.testCourse);
        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DatabaseMgmtActivity.class);
                startActivity(intent);
            }
        });

        Button profileButton = (Button) getView().findViewById(R.id.testProfile);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        })
        ;

//        profileButton.setTextColor(white);
//        profileButton.setBackgroundColor(red);
//        profileButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        profileButton.setGravity(Gravity.CENTER_HORIZONTAL);



        snoopButton = (Button) getView().findViewById(R.id.testSnoop);
        snoopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SnoopActivity.class);
                startActivity(intent);
            }
        });

        snoopButton.setVisibility(View.INVISIBLE);


    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        database = FirebaseDatabase.getInstance();
        parentActivity = (AppCompatActivity) context;
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
                        ((GlobalData) parentActivity.getApplication()).setUser(user);
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
    }

    public void addSubjectsToLayout(ArrayList<Subject> subjects){
        LinearLayout layout = parentView.findViewById(R.id.HoldMyButtons);
        for (Subject s: subjects){
            Log.i("isLive",""+s.isLive());
            final Subject subj = s;
            Button button = new Button(parentActivity.getApplicationContext());


            // BUTTON APPEARANCE
//            button.setGravity(Gravity.CENTER_HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,10,0,0);
            button.setLayoutParams(params);
            button.setTextColor(black);
            button.setBackgroundColor(turquoise);
            button.setGravity(Gravity.CENTER_HORIZONTAL);
            button.setGravity(View.TEXT_ALIGNMENT_GRAVITY);

//            Font.MOON_BOLD.apply(getContext(),button);
//            button.setTypeface(opensans_regular);

//            button.setTypeface(Typeface.createFromFile("\"C:\\Users\\Trinh Vu Linh Chi\\Documents\\GitHub\\ProjectANA\\app\\src\\main\\res\\font\\moon_bold.otf\""));
            // BUTTON APPEARANCE
            if (s.isLive()){
                button.setText(s.getSubjectCode()+" "+s.getSubjectTitle() + " "+ "(Live)");
            }
            else button.setText(s.getSubjectCode()+" "+s.getSubjectTitle());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(parentActivity.getApplicationContext(), CourseTopicActivity.class);
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
            Button newSubjectButton = new Button(parentActivity.getApplicationContext());
            newSubjectButton.setText("Create new subject");
            newSubjectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(parentActivity.getApplicationContext(), AddSubjectActivity.class);
                    startActivity(intent);
                }
            });
            layout.addView(newSubjectButton);
        }
    }

}


