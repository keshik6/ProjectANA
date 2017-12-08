package sutdcreations.projectana;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import sutdcreations.classes.Feedback;
import sutdcreations.classes.Question;
import sutdcreations.classes.Student;
import sutdcreations.classes.Teacher;
import sutdcreations.classes.Topic;
import sutdcreations.classes.User;

public class simpleQuestionActivity extends AppCompatActivity {
    FirebaseDatabase database;
    User user;
    Topic topic;
    MyAdapter adapter;
    RecyclerView r1;
    ArrayList<Question> questions = new ArrayList<Question>();
    boolean inForeground = true;

    @Override
    protected void onPause(){
        super.onPause();
        Log.i("debugAlert", "question list no longer in foreground");
        inForeground = false;
    }

    @Override
    protected void onResume(){
        super.onResume();
        inForeground = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_question);
        final String topicKey = getIntent().getStringExtra("topicTitle");
        database = FirebaseDatabase.getInstance();
        System.out.println("start");
        user = ((GlobalData) getApplication()).getUser();


        //Set up RecyclerView
        r1 = (RecyclerView) findViewById(R.id.questionRecyclerView);
        adapter=new MyAdapter(this,questions,user);
        r1.setAdapter(adapter);
        r1.setLayoutManager(new LinearLayoutManager(this));

        //make ask question button invisible for teachers as they are not supposed to be asking questions
        Button askQuestion = findViewById(R.id.askQuestion);
        if (user instanceof Teacher){
            askQuestion.setVisibility(View.INVISIBLE);
        }

        //get Topic object from Firebase
        DatabaseReference topicRef = database.getReference().child("Topics").child(topicKey);
        topicRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                topic = dataSnapshot.getValue(Topic.class);
                TextView topicTitle = findViewById(R.id.topicTitleQuestionList);
                topicTitle.setText(topic.getTitle());
                //listen for new feedback request if topic is live, and if user is a student
                if (topic.isLive() && user instanceof Student){
                    Log.i("debugAlert","calling waitForFeedback in question list");
                    waitForFeedback();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //get list of questions from Firebase
        DatabaseReference questionReference = database.getReference().child("Questions");
        questionReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //iterate through all questions
                try {
                    questions.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Question question = postSnapshot.getValue(Question.class);

                        //add question to ArrayList if it is of the topic that you are interested in
                        if (question.getKey().contains(topicKey)) {
                            questions.add(question);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                //addQuestionsToLayout(questions);
                Collections.sort(questions, new QuestionComparator());
                //addQuestionsToLayout();
                adapter.notifyDataSetChanged();
                adapter.notifyItemMoved(0,questions.size()-1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*
  check if a list of users contains a certain user. The standard ArrayList.contains method will not work here
  as the references to the student object inside the list and outside the list will be different even though they are referring
   to the same student, due to them being retrieved from Firebase at different times.
  */
    public boolean containsStudent(ArrayList<Student> users, User user){
        for (User u : users){
            if (u.getUid().equals(user.getUid())){
                return true;
            }
        }
        return false;
    }

    //check for new Feedback objects being added to the database. If Feedback is related to current live subject, show AlertDialog to prompt user to give feedback
    public void waitForFeedback(){
        DatabaseReference feedbackRef = database.getReference().child("Feedback");
        feedbackRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("debugAlert", "onChildAdded called in question list");
                Feedback feedback = dataSnapshot.getValue(Feedback.class);
                //do nothing if student has already voted
                if (containsStudent(feedback.getVoted(),user)){
                    return;
                }
                //show alert if feedback is related to current topic
                if (feedback.getKey().contains(topic.getKey()) && inForeground){
                    Log.i("debugAlert", "showAlertDialog called in question list");
                    showFeedbackAlertDialog(feedback);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showFeedbackAlertDialog(Feedback feedback){

        final Feedback final_fb = feedback;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Feedback requested");
        alertBuilder.setMessage("Your teacher is asking for feedback. Did you understand the explanation?");
        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                understand(final_fb);
            }
        });
        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dontUnderstand(final_fb);
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        if (!this.isFinishing() && !containsStudent(feedback.getVoted(),user) && inForeground) {
            alertDialog.show();
        }
    }

    //increment the understand attribute in the existing Feedback object in Firebase
    public void understand(Feedback feedback){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Feedback").child(feedback.getKey());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Feedback feedback = dataSnapshot.getValue(Feedback.class);
                feedback.incUnderstand((Student) user);
                DatabaseAddHelper.updateFeedback(FirebaseDatabase.getInstance(),feedback);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //increment the dont_understand attribute in the existing Feedback object in Firebase
    public void dontUnderstand(Feedback feedback){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Feedback").child(feedback.getKey());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Feedback feedback = dataSnapshot.getValue(Feedback.class);
                feedback.decUnderstand((Student) user);
                DatabaseAddHelper.updateFeedback(FirebaseDatabase.getInstance(), feedback);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //onClick method for button for students to ask questions
    public void onClickAskQuestion(View v){
        Intent intent = new Intent(this, AskQuestionActivity.class);
        intent.putExtra("topicKey",topic.getKey());
        startActivity(intent);
    }

    // Compare questions by vote counts
    class QuestionComparator implements Comparator<Question> {

        @Override
        public int compare(Question q1, Question q2) {
            if (q1.getVotes()>q2.getVotes()) {
                return -1;
            }
            else if (q1.getVotes()<q2.getVotes()) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }
}

class MyAdapter extends RecyclerView.Adapter<MyAdapter.myHolder>{

    ArrayList<Question> questions;
    Context context;
    User user;

    public MyAdapter(Context context,ArrayList<Question> questions, User user) {
        this.context=context;
        this.questions=questions;
        this.user=user;
    }

    @Override
    public MyAdapter.myHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater myInflator = LayoutInflater.from(context);
        View myView = myInflator.inflate(R.layout.single_question_layout,parent,false);
        return new myHolder(myView);
    }

    @Override
    public void onBindViewHolder(MyAdapter.myHolder holder, int position) {
        final Question final_question=questions.get(position);
        final ImageButton upVote=holder.upVote;
        final ImageButton downVote=holder.downVote;
        holder.postedBy.setText("Posted by: "+final_question.getAnimalMap().get(final_question.getAsker().getUid()));
        if (final_question.isLive()) holder.questionTitle.setText(final_question.getTitle()+ " (Live)");
        else holder.questionTitle.setText(final_question.getTitle());
        if (checkUpVoted(user,final_question)) {
            upVote.setBackgroundResource(R.drawable.upvote);
        } else {
            upVote.setBackgroundResource(R.drawable.upvote_red);
        }
        if (checkDownVoted(user,final_question)) {
            downVote.setBackgroundResource(R.drawable.downvote);
        } else {
            downVote.setBackgroundResource(R.drawable.downvote_blue);
        }
        holder.questionTitle.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context,simpleAnswerActivity.class);
                        intent.putExtra("questionKey",final_question.getKey());
                        context.startActivity(intent);
                    }
                }
        );
        final TextView voteCount=holder.voteCount;
        voteCount.setText(""+final_question.getVotes());
        holder.upVote.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkDownVoted(user,final_question)) {
                            final_question.removeDownVote(user);
                            final_question.upVote(user);
                            DatabaseAddHelper.updateQuestion(FirebaseDatabase.getInstance(),final_question);
                            upVote.setBackgroundResource(R.drawable.upvote);
                            downVote.setBackgroundResource(R.drawable.downvote_blue);
                            voteCount.setText("" + final_question.getVotes());
                        }
                        else if (checkUpVoted(user,final_question)) {
                            final_question.removeUpVote(user);
                            DatabaseAddHelper.updateQuestion(FirebaseDatabase.getInstance(),final_question);
                            upVote.setBackgroundResource(R.drawable.upvote_red);
                            voteCount.setText("" + final_question.getVotes());
                        }
                        else {
                            final_question.upVote((User) user);
                            //TODO: After running with this line it throws an error: Can't instantiate abstract class sutdcreations.classes.User
                            DatabaseAddHelper.updateQuestion(FirebaseDatabase.getInstance(),final_question);
                            upVote.setBackgroundResource(R.drawable.upvote);
                            voteCount.setText("" + final_question.getVotes());
                        }
                    }
                }
        );
        holder.downVote.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkUpVoted(user,final_question)) {
                            final_question.removeUpVote(user);
                            final_question.downVote(user);
                            DatabaseAddHelper.updateQuestion(FirebaseDatabase.getInstance(),final_question);
                            upVote.setBackgroundResource(R.drawable.upvote_red);
                            downVote.setBackgroundResource(R.drawable.downvote);
                            voteCount.setText("" + final_question.getVotes());
                        }
                        else if (checkDownVoted(user,final_question)) {
                            final_question.removeDownVote(user);
                            DatabaseAddHelper.updateQuestion(FirebaseDatabase.getInstance(),final_question);
                            downVote.setBackgroundResource(R.drawable.downvote_blue);
                            voteCount.setText("" + final_question.getVotes());
                        } else {
                            final_question.downVote(user);
                            downVote.setBackgroundResource(R.drawable.downvote);
                            DatabaseAddHelper.updateQuestion(FirebaseDatabase.getInstance(),final_question);
                            voteCount.setText("" + final_question.getVotes());
                        }
                    }
                }
        );

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class myHolder extends RecyclerView.ViewHolder{
        Button questionTitle;
        ImageButton upVote;
        ImageButton downVote;
        TextView voteCount;
        TextView postedBy;
        public myHolder(View itemView) {
            super(itemView);
            questionTitle=(Button)itemView.findViewById(R.id.questionTitle);
            upVote=(ImageButton)itemView.findViewById(R.id.upVote);
            downVote=(ImageButton)itemView.findViewById(R.id.downVote);
            voteCount=(TextView)itemView.findViewById(R.id.voteCount);
            postedBy = (TextView) itemView.findViewById(R.id.postedBy);
        }
    }

    private boolean checkUpVoted(User user, Question question) {
        for (String votedUid:question.getUpVoted()) {
            if (user.getUid().equals(votedUid)){
                return true;
            }
        }
        return false;
    }

    private boolean checkDownVoted(User user, Question question) {
        for (String votedUid:question.getDownVoted()) {
            if (user.getUid().equals(votedUid)){
                return true;
            }
        }
        return false;
    }

}

