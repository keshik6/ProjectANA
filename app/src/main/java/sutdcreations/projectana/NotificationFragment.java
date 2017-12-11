package sutdcreations.projectana;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL;

import sutdcreations.classes.Student;
import sutdcreations.classes.Teacher;
import sutdcreations.classes.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {
    FirebaseDatabase database;
    Student userFromFirebase;
    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        //get Student object from Firebase
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();

        DatabaseReference userRef = database.getReference().child("UserInfo").child(uid);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    userFromFirebase = dataSnapshot.getValue(Student.class);
                    loadNotifications(userFromFirebase.getNotifications());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadNotifications(final ArrayList<String> notifications){
        Log.i("debugNotif",""+notifications.size());
        final LinearLayout notifFragmentLayout = getView().findViewById(R.id.notifFragmentLayout);
        notifFragmentLayout.removeAllViews();
        for (final String questionKey:notifications){
            //create new LinearLayout for each notification, containing some text, a button to navigate to the answered question, and a button to delete the notification
            final LinearLayout notifLayout = new LinearLayout(getActivity().getApplicationContext());
            notifLayout.setOrientation(LinearLayout.HORIZONTAL);

            //text widget
            TextView notifText = new TextView(getActivity().getApplicationContext());
            notifText.setText("Someone answered your question!");
            notifLayout.addView(notifText);

            //button to navigate to answered question
            Button goToQuestion = new Button(getActivity().getApplicationContext());
            goToQuestion.setText("Go");
            goToQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity().getApplicationContext(),simpleAnswerActivity.class);
                    intent.putExtra("questionKey",questionKey);
                    startActivity(intent);
                    userFromFirebase.removeNotification(questionKey);
                    DatabaseAddHelper.updateStudent(FirebaseDatabase.getInstance(),userFromFirebase);
                    notifFragmentLayout.removeView(notifLayout);
                }
            });
            notifLayout.addView(goToQuestion);

            //button to delete notification
            Button deleteNotif = new Button(getActivity().getApplicationContext());
            deleteNotif.setText("Delete");
            deleteNotif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userFromFirebase.removeNotification(questionKey);
                    DatabaseAddHelper.updateStudent(FirebaseDatabase.getInstance(),userFromFirebase);
                    notifFragmentLayout.removeView(notifLayout);
                }
            });
            notifLayout.addView(deleteNotif);

            //add to overall layout
            notifFragmentLayout.addView(notifLayout);
        }
    }

}
