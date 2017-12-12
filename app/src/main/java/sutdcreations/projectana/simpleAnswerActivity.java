package sutdcreations.projectana;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.renderscript.Sampler;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import sutdcreations.classes.Answer;
import sutdcreations.classes.Feedback;
import sutdcreations.classes.Question;
import sutdcreations.classes.Student;
import sutdcreations.classes.Subject;
import sutdcreations.classes.Teacher;
import sutdcreations.classes.Topic;
import sutdcreations.classes.User;

public class simpleAnswerActivity extends AppCompatActivity {
    FirebaseDatabase database;
    Question question;
    TextView questionTopic;
    TextView questionDetail;
    Button requestFeedback;
    User user;
    boolean inForeground = true;
    boolean waitingForFeedback;
    Button newAnswerBut;
    MyAdapter2 adapter2;
    ArrayList<Answer> answers = new ArrayList<Answer>();
    RecyclerView r1;
    ValueEventListener listener; //keep track of if a listener is added to questionRef, make sure only one is added
    DatabaseReference questionRef;

    @Override
    protected void onPause(){
        super.onPause();
        Log.i("debugAlert","answer list no longer in foreground");
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
        Log.i("debugAlert","onCreate called in answer list");
        setContentView(R.layout.activity_simple_answer);
        database = FirebaseDatabase.getInstance();
        user = ((GlobalData) getApplication()).getUser();
        requestFeedback=(Button)findViewById(R.id.requestFeedback);
        newAnswerBut = findViewById(R.id.newAnswerBut);

        if (listener == null) { //be sure to only add one listener
            Log.i("debugAlert","adding listener");
            listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("size of array is: " + answers.size());
                    answers.clear();
                    question = dataSnapshot.getValue(Question.class);
                    if (questionTopic == null) {
                        questionTopic = (TextView) findViewById(R.id.questionTopic);
                        questionTopic.setText(question.getTitle());
                        questionDetail = (TextView) findViewById(R.id.questionDetail);
                        questionDetail.setText(question.getBody());
                    }
                    for (Answer answer : question.getAnswers()) {
                        answers.add(answer);
                    }
                    if (question.isLive() && user instanceof Student) {
                        Log.i("debugAlert", "calling waitForFeedback in answer");
                        waitForFeedback();
                    }
                    Collections.sort(answers, new AnswerComparator());
                    if (adapter2 == null) {
                        //Set up RecyclerView
                        r1 = (RecyclerView) findViewById(R.id.answerRecyclerView);
                        adapter2 = new MyAdapter2(simpleAnswerActivity.this, answers, question, user);
                        r1.setAdapter(adapter2);
                        r1.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    } else {
                        //Reset RecyclerView
                        adapter2 = new MyAdapter2(simpleAnswerActivity.this, answers, question, user);
                        r1.setAdapter(adapter2);
                        r1.invalidate();
                    }
                    // give feedback
                    User user = ((GlobalData) getApplication()).getUser();

                    if (question.isLive()) { //feedback system only applicable to live questions
                        //Request feedback (teacher) or Give feedback (student)
                        if (user instanceof Teacher) {
                            requestFeedback.setVisibility(View.VISIBLE);
                            requestFeedback.setText("Request for feedback");
                            requestFeedback.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    question.setFeedback(true);
                                    DatabaseAddHelper.updateQuestion(database, question);
                                    Feedback feedback = new Feedback(question);
                                    DatabaseAddHelper.addFeedback(database, feedback);
                                    Intent intent = new Intent(getApplicationContext(), FeedbackActivity.class);
                                    intent.putExtra("feedbackKey", question.getKey());
                                    startActivity(intent);
                                }
                            });
                        }

                        //add button to give feedback for students if teacher has asked for feedback
                        if (user instanceof Student) {
                            Log.i("feedback status: ", "" + question.isFeedback());
                            if (question.isFeedback()) {
                                requestFeedback.setVisibility(View.VISIBLE);
                                requestFeedback.setText("Give feedback");
                                requestFeedback.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getApplicationContext(), FeedbackActivity.class);
                                        intent.putExtra("feedbackKey", question.getKey());
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                requestFeedback.setVisibility(View.GONE);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            //get question object from Firebase
            Log.i("questionKey", getIntent().getStringExtra("questionKey"));
            questionRef = database.getReference().child("Questions").child(getIntent().getStringExtra("questionKey"));
            questionRef.addValueEventListener(listener);

            Button deleteQuestionButton = findViewById(R.id.deleteQuestion);
            if (user instanceof Teacher) { //button to delete questions should only be visible to teahcers
                deleteQuestionButton.setVisibility(View.VISIBLE);
            }

        }
    }

    //ensure to always go back to simpleQuestionActivity when back button pressed
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, simpleQuestionActivity.class);
        intent.putExtra("topicTitle", getIntent().getStringExtra("topicKey"));
        startActivity(intent);
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
        if (!waitingForFeedback) { //only wait for feedback on one instance
            waitingForFeedback = true;
            DatabaseReference feedbackRef = database.getReference().child("Feedback");
            feedbackRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.i("debugAlert", "onChildAdded called in answer list");
                    Feedback feedback = dataSnapshot.getValue(Feedback.class);
                    //do nothing if student has already voted
                    if (containsStudent(feedback.getVoted(), user)) {
                        Log.i("debugAlert", "student has already voted");
                        return;
                    }
                    //show alert if feedback is related to current question
                    if (feedback.getKey().contains(question.getKey()) && inForeground) {
                        Log.i("debugAlert", "showAlertDialog called in answer list");
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

    //onClick method for button that leads to add question activity
    public void onClickGoToAddAnswer(View v){
        Intent intent = new Intent(this, addAnswerActivity.class);
        intent.putExtra("questionKey",getIntent().getStringExtra("questionKey"));
        intent.putExtra("askerUid",question.getAsker().getUid());
        intent.putExtra("topicKey", getIntent().getStringExtra("topicKey"));
        startActivity(intent);
    }


    //delete question, for teacher's use
    public void onClickDeleteQuestion(View v){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Delete question");
        alertBuilder.setMessage("Are you sure you want to delete this question?");
        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //get parent topic of this question from firebase
                DatabaseReference topicReference = FirebaseDatabase.getInstance().getReference().child("Topics").child(getIntent().getStringExtra("topicKey"));
                //go back to list of topics
                Intent intent = new Intent(getApplicationContext(),simpleQuestionActivity.class);
                intent.putExtra("topicTitle",getIntent().getStringExtra("topicKey"));
                startActivity(intent);
                questionRef.removeEventListener(listener);
                topicReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Topic parentTopic = dataSnapshot.getValue(Topic.class);
                        DatabaseAddHelper.deleteQuestion(FirebaseDatabase.getInstance(),question,parentTopic);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                finish();

            }
        });
        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    //delete answers, for teacher's use
    public void onClickDeleteAnswer(View v){

    }

    // Compare answers by vote counts
    class AnswerComparator implements Comparator<Answer> {

        @Override
        public int compare(Answer a1, Answer a2) {
            if (a1.getVotes()>a2.getVotes()) {
                return -1;
            }
            else if (a1.getVotes()<a2.getVotes()) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }
}
class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.myHolder2>{

    ArrayList<Answer> answers;
    Context context;
    User user;
    Question question;

    public MyAdapter2(Context context,ArrayList<Answer> answers,Question question, User user) {
        this.context=context;
        this.answers=answers;
        this.user=user;
        this.question=question;
    }

    @Override
    public MyAdapter2.myHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater myInflator = LayoutInflater.from(context);
        View myView = myInflator.inflate(R.layout.single_answer_layout,parent,false);
        return new myHolder2(myView);
    }

    @Override
    public void onBindViewHolder(MyAdapter2.myHolder2 holder, int position) {
        System.out.println("inside onBindViewHolder");
        final Answer final_answer=answers.get(position);
        final ImageButton upVote=holder.upVote;
        final ImageButton downVote=holder.downVote;
        final myHolder2 final_holder = holder;
        holder.postedBy.setText("Posted by: "+question.getAnimalMap().get(final_answer.getAnswerer().getUid()).toUpperCase());
        holder.answer.setText(final_answer.getBody());
        if (checkUpVoted(user,final_answer)) {
            upVote.setBackgroundResource(R.drawable.up_arrow);
        } else {
            upVote.setBackgroundResource(R.drawable.green_arrow);
        }
        if (checkDownVoted(user,final_answer)) {
            downVote.setBackgroundResource(R.drawable.down_arrow);
        } else {
            downVote.setBackgroundResource(R.drawable.red_arrow);
        }
        final TextView voteCount=holder.voteCount;
        voteCount.setText(""+final_answer.getVotes());
        System.out.println("inside bind view holder");
        holder.upVote.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkDownVoted(user,final_answer)) {
                            final_answer.removeDownVote(user);
                            final_answer.upVote(user);
                            DatabaseAddHelper.updateQuestion(FirebaseDatabase.getInstance(),question);
                            upVote.setBackgroundResource(R.drawable.up_arrow);
                            downVote.setBackgroundResource(R.drawable.red_arrow);
                            voteCount.setText("" + final_answer.getVotes());
                        }
                        else if (checkUpVoted(user,final_answer)) {
                            final_answer.removeUpVote(user);
                            DatabaseAddHelper.updateQuestion(FirebaseDatabase.getInstance(),question);
                            upVote.setBackgroundResource(R.drawable.green_arrow);
                            voteCount.setText("" + final_answer.getVotes());
                        }
                        else {
                            final_answer.upVote((User) user);
                            DatabaseAddHelper.updateQuestion(FirebaseDatabase.getInstance(),question);
                            upVote.setBackgroundResource(R.drawable.up_arrow);
                            voteCount.setText("" + final_answer.getVotes());
                        }
                    }
                }
        );
        holder.downVote.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkUpVoted(user,final_answer)) {
                            final_answer.removeUpVote(user);
                            final_answer.downVote(user);
                            DatabaseAddHelper.updateQuestion(FirebaseDatabase.getInstance(),question);
                            upVote.setBackgroundResource(R.drawable.green_arrow);
                            downVote.setBackgroundResource(R.drawable.down_arrow);
                            voteCount.setText("" + final_answer.getVotes());
                        }
                        else if (checkDownVoted(user,final_answer)) {
                            final_answer.removeDownVote(user);
                            DatabaseAddHelper.updateQuestion(FirebaseDatabase.getInstance(),question);
                            downVote.setBackgroundResource(R.drawable.red_arrow);
                            voteCount.setText("" + final_answer.getVotes());
                        } else {
                            final_answer.downVote(user);
                            downVote.setBackgroundResource(R.drawable.down_arrow);
                            DatabaseAddHelper.updateQuestion(FirebaseDatabase.getInstance(),question);
                            voteCount.setText("" + final_answer.getVotes());
                        }
                    }
                }
        );

        //remove delete answer button from students
        if (user instanceof Student){
            holder.deleteButton.setVisibility(View.GONE);
        }

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String answerText = final_holder.answer.getText().toString();
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                alertBuilder.setTitle("Delete answer");
                alertBuilder.setMessage("Are you sure you want to delete this answer?");
                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //get parent topic of this question from firebase
                        DatabaseReference questionReference = FirebaseDatabase.getInstance().getReference().child("Questions").child(question.getKey());
                        questionReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Question parentQuestion = dataSnapshot.getValue(Question.class);
                                for (Answer answer:question.getAnswers()) {
                                    if (answer.getBody().equals(answerText)) {
                                        DatabaseAddHelper.deleteAnswer(FirebaseDatabase.getInstance(),answer ,question);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
                alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public class myHolder2 extends RecyclerView.ViewHolder{
        TextView answer;
        ImageButton upVote;
        ImageButton downVote;
        TextView voteCount;
        TextView postedBy;
        Button deleteButton;
        public myHolder2(View itemView) {
            super(itemView);
            answer=(TextView) itemView.findViewById(R.id.answer);
            upVote=(ImageButton)itemView.findViewById(R.id.upVote);
            downVote=(ImageButton)itemView.findViewById(R.id.downVote);
            voteCount=(TextView)itemView.findViewById(R.id.voteCount);
            postedBy = (TextView) itemView.findViewById(R.id.postedBy);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    private boolean checkUpVoted(User user, Answer answer) {
        for (String votedUid:answer.getUpVoted()) {
            if (user.getUid().equals(votedUid)){
                return true;
            }
        }
        return false;
    }

    private boolean checkDownVoted(User user, Answer answer) {
        for (String votedUid:answer.getDownVoted()) {
            if (user.getUid().equals(votedUid)){
                return true;
            }
        }
        return false;
    }

}