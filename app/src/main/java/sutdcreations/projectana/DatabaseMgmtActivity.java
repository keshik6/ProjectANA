package sutdcreations.projectana;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import sutdcreations.classes.Answer;
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
    ArrayList<String> animalList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_mgmt);
        database = FirebaseDatabase.getInstance();

//        animalList.addAll(Arrays.asList("alligator", "anteater", "armadillo", "auroch", "axolotl", "badger", "bat", "beaver", "buffalo", "camel", "chameleon", "cheetah", "chipmunk", "chinchilla", "chupacabra", "cormorant", "coyote", "crow", "dingo", "dinosaur", "dog", "dolphin", "duck", "elephant", "ferret", "fox", "frog", "giraffe", "gopher", "grizzly", "hedgehog", "hippo", "hyena", "jackal", "ibex", "ifrit", "iguana", "kangaroo", "koala", "kraken", "lemur", "leopard", "liger", "lion", "llama", "manatee", "mink", "monkey", "moose", "narwhal", "nyan cat", "orangutan", "otter", "panda", "penguin", "platypus", "python", "pumpkin", "quagga", "rabbit", "raccoon", "rhino", "sheep", "shrew", "skunk", "slow loris", "squirrel", "tiger", "turtle", "walrus", "wolf", "wolverine", "wombat"));
//        final Student user = (Student) ((GlobalData) getApplication()).getUser();
//        DatabaseReference questionReference = database.getReference().child("Questions");
//        questionReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
////                Topic topic = dataSnapshot.getValue(Topic.class);
////                String title = topic.getTitle();
////                Log.i("DBtest",topic.getTitle());
//                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
//                    try {
//                        Question question = postSnapshot.getValue(Question.class);
//                        int randNum = ThreadLocalRandom.current().nextInt(0,question.getAnimalList().size());
//                        String randAnimal = question.getAnimalList().get(randNum);
//                        question.getAnimalMap().put(question.getAsker().getUid(),randAnimal);
//                        question.getAnimalList().remove(randAnimal);
//                        DatabaseAddHelper.updateQuestion(database,question);
//
//                    }
//                    catch (Exception e){
//                        Log.i("updateDBExcept", "exception caught at current topic");
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }
}
