package sutdcreations.projectana;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import Client.*;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button signInButton;
    private EditText getEmail;
    private EditText getPassword;
    private FirebaseAuth firebaseAuth;      //Declaring Firebase authentication object
    private TextView gettxtView1;
    private TextView gettxtView2;
    String email,password;
    ChatClientGUI trueClient = new ChatClientGUI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Instantiate Variables
        firebaseAuth = FirebaseAuth.getInstance();
        getEmail = (EditText) findViewById(R.id.emailTF);
        getPassword = (EditText) findViewById(R.id.passwordTF);
        signInButton = (Button) findViewById(R.id.signInBt);
        gettxtView1 = (TextView)findViewById(R.id.textView1);
        gettxtView2 = (TextView)findViewById(R.id.textView2);

        //Set On click Listener
        signInButton.setOnClickListener(this);


    }

    //back button should do nothing on login page
    @Override
    public void onBackPressed(){

    }

    private void signIn(){
        email = getEmail.getText().toString().trim();
        password = getPassword.getText().toString().trim();

        //Make sure user cannot proceed on without entering email and password
        if(TextUtils.isEmpty(email)){ //email is empty
            Toast.makeText(this,"Please enter email", Toast.LENGTH_SHORT).show();
            signInButton.setText("Sign in");
            return; //stop function from further execution
        }
        if(TextUtils.isEmpty(password)){ //password is empty
            Toast.makeText(this,"Please enter password", Toast.LENGTH_SHORT).show();
            signInButton.setText("Sign in");
            return; //stop function from further execution
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                //Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    //Log.w(TAG, "signInWithEmail:failed", task.getException());
                    Toast.makeText(LoginActivity.this, "Wrong username or password",
                            Toast.LENGTH_SHORT).show();
                    signInButton.setText("Sign in");
                }
                else{
                    Toast.makeText(LoginActivity.this, "Successfully signed in",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                }

                }
            });
    }

    @Override
    public void onClick(View v) {
        signInButton.setText("Signing in...");
        signIn();
    }


}


