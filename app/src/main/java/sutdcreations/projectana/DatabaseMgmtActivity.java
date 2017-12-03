package sutdcreations.projectana;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import sutdcreations.classes.Question;
import sutdcreations.classes.Student;
import sutdcreations.classes.Subject;
import sutdcreations.classes.Teacher;
import sutdcreations.classes.Topic;
import sutdcreations.classes.User;

/*
This activity is meant for programmatically adding objects to the Firebase database.
For example, it can be used to add more users into the database.
It is purely for convenience during development and will not be used in the final product.
*/

public class DatabaseMgmtActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_mgmt);
        database = FirebaseDatabase.getInstance();
//        String topicName0 = "50001 Recursion";
//        ArrayList<String> tag01 = new ArrayList<>();
//        tag01.add("Strings");
//        Question question01 = new Question("How to do recursion with Strings?",
//                "I don't really understand how to solve recursive problems with Strings, can someone explain it to me?",tag01);
//
//        ArrayList<String> tag02 = new ArrayList<>();
//        tag02.add("Towers of Hanoi");
//        tag02.add("Cohort question");
//        Question question02 = new Question("Towers of Hanoi?",
//                "I know we went through this in class, but I really don't understand it, can someone explain it to me in detail?", tag02);
//        Topic topic0 = new Topic("Recursion");
//        topic0.addQuestion(question01);
//        topic0.addQuestion(question02);
//        database.getReference().child("Topics").child(topicName0).setValue(topic0);
//        database.getReference().child("Questions").child(topicName0+" "+"How to do recursion with Strings?").setValue(question01);
//        database.getReference().child("Questions").child(topicName0+" "+"Towers of Hanoi?").setValue(question02);
//
//        String topicName1 = "50001 Exceptions";
//        ArrayList<String> tag11 = new ArrayList<>();
//        Question question11 = new Question("Checked and unchecked exceptions?",
//                "Hi, I don't really get the difference between the two types of exceptions. Help please!",tag11);
//
//        ArrayList<String> tag12 = new ArrayList<>();
//        tag12.add("catch");
//        Question question12 = new Question("Catching exceptions",
//                "Hi, can someone tell me what to do after I catch an exception? What's the point?", tag12);
//        Topic topic1 = new Topic("Exceptions");
//        topic1.addQuestion(question11);
//        topic1.addQuestion(question12);
//        database.getReference().child("Topics").child(topicName1).setValue(topic1);
//        database.getReference().child("Questions").child(topicName1+" "+"Checked and unchecked exceptions?").setValue(question11);
//        database.getReference().child("Questions").child(topicName1+" "+"Catching exceptions").setValue(question12);
//
//        String topicName2 = "50001 Android programming";
//        ArrayList<String> tag21 = new ArrayList<>();
//        tag21.add("RecyclerView");
//        tag21.add("Adapters");
//        Question question21 = new Question("RecyclerView Adapter",
//                "What's the purpose of the Adapter when we do RecyclerView?",tag21);
//
//        ArrayList<String> tag22 = new ArrayList<>();
//        tag22.add("SQLite");
//        Question question22 = new Question("So many classes, help!!",
//                "For our SQLite cohort exercise, there were so many classes including the contract, the helper, and the database, what is the use of all those classes?", tag22);
//        Topic topic2 = new Topic("Android programming");
//        topic2.addQuestion(question21);
//        topic2.addQuestion(question22);
//        database.getReference().child("Topics").child(topicName2).setValue(topic2);
//        database.getReference().child("Questions").child(topicName2+" "+"RecyclerView Adapter").setValue(question21);
//        database.getReference().child("Questions").child(topicName2+" "+"So many classes, help!!").setValue(question22);
//
//        String topicName3 = "50002 Finite State Machines";
//        ArrayList<String> tag31 = new ArrayList<>();
//        tag31.add("Moore and Mealy");
//        Question question31 = new Question("Difference between Moore and Mealy",
//                "Whats the difference between Moore and Mealy machines? When do we use Moore machines and when do we use Mealy machines?",tag31);
//
//        Topic topic3 = new Topic("Finite State Machines");
//        topic3.addQuestion(question31);
//
//        database.getReference().child("Topics").child(topicName3).setValue(topic3);
//        database.getReference().child("Questions").child(topicName3+" "+"Difference between Moore and Mealy").setValue(question31);
//
//        String topicName4 = "50002 The Assembly Language";
//        ArrayList<String> tag41 = new ArrayList<>();
//        tag41.add("BR and JMP");
//        Question question41 = new Question("Difference between BR and JMP?",
//                "Whats the difference between BR and JMP? Don't they both jump to a different part of the code?",tag41);
//
//        ArrayList<String> tag42 = new ArrayList<>();
//        tag42.add("for loop");
//        Question question42 = new Question("For loops in assembly",
//                "I understand the way that for loops are written in class, but I came up with another way that checks the condition first, before branching into the for loop rather than branching at the end. Is there a problem with this?",
//                tag42);
//        Topic topic4 = new Topic("The Assembly Language");
//        topic4.addQuestion(question41);
//        topic4.addQuestion(question42);
//        database.getReference().child("Topics").child(topicName4).setValue(topic4);
//        database.getReference().child("Questions").child(topicName4+" "+"Difference between BR and JMP?").setValue(question41);
//        database.getReference().child("Questions").child(topicName4+" "+"For loops in assembly").setValue(question42);
//
//        String topicName5 = "50002 Beta Architecture";
//        ArrayList<String> tag51 = new ArrayList<>();
//        tag51.add("Data and code memory");
//        Question question51 = new Question("Separation of code and data memory",
//                "I saw the diagram of the Beta and noticed that the code and data memory are separated, are they not the same memory?",tag51);
//
//        ArrayList<String> tag52 = new ArrayList<>();
//        tag52.add("z logic");
//        tag52.add("BNE/BEQ");
//        Question question52 = new Question("Z in Beta",
//                "What does the Z value represent in Beta? I know it's used for BEQ and BNE, but what does it mean?", tag52);
//        Topic topic5 = new Topic("Beta Architecture");
//        topic5.addQuestion(question51);
//        topic5.addQuestion(question52);
//        database.getReference().child("Topics").child(topicName5).setValue(topic5);
//        database.getReference().child("Questions").child(topicName5+" "+"Separation of code and data memory").setValue(question51);
//        database.getReference().child("Questions").child(topicName5+" "+"Z in Beta").setValue(question52);
//
//        String topicName6 = "50004 Hash Tables";
//        ArrayList<String> tag61 = new ArrayList<>();
//        tag61.add("Size of hash table");
//        Question question61 = new Question("Hash table size increase?",
//                "Do hash tables have a capacity? Will the size keep increasing if we keep adding more items to it?",tag61);
//
//        Topic topic6 = new Topic("Hash Tables");
//        topic6.addQuestion(question61);
//        database.getReference().child("Topics").child(topicName6).setValue(topic6);
//        database.getReference().child("Questions").child(topicName6+" "+"Hash table size increase?").setValue(question61);
//
//        String topicName7 = "50004 Single-source Shortest Paths";
//        ArrayList<String> tag71 = new ArrayList<>();
//        tag71.add("Djikstra");
//        Question question71 = new Question("Why does Djikstra's work?",
//                "Why does Djikstra's algorithm definitely get the shortest path? It seems to be too good to be true that going for the nearest node will result in the shortest path",tag71);
//
//        ArrayList<String> tag72 = new ArrayList<>();
//        tag72.add("Djikstra");
//        tag72.add("negative weight edge");
//        Question question72 = new Question("Djikstra with negative weights?",
//                "Why does Djikstra's algorithm not work with negative edge weights?", tag72);
//        Topic topic7 = new Topic("Single-source Shortest Paths");
//        topic7.addQuestion(question71);
//        topic7.addQuestion(question72);
//        database.getReference().child("Topics").child(topicName7).setValue(topic7);
//        database.getReference().child("Questions").child(topicName7+" "+"Why does Djikstra's work?").setValue(question71);
//        database.getReference().child("Questions").child(topicName7+" "+"Djikstra with negative weights?").setValue(question72);
//
//        String topicName8 = "50004 Dynamic Programming";
//        ArrayList<String> tag81 = new ArrayList<>();
//        Question question81 = new Question("Top-down vs bottom-up?",
//                "Are the top-down and bottom-up approaches essentially the same in terms of computations performed?",tag81);
//        Topic topic8 = new Topic("Dynamic Programming");
//        topic8.addQuestion(question81);
//        database.getReference().child("Topics").child(topicName8).setValue(topic8);
//        database.getReference().child("Questions").child(topicName8+" "+"Top-down vs bottom-up?").setValue(question81);
    }
}
