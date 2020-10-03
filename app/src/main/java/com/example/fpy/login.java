package com.example.fpy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText emailview,passwordview;
    TextView message,forgotpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //ui
        emailview = findViewById(R.id.userid);
        passwordview = findViewById(R.id.Password);
        Button login = findViewById(R.id.login_button);
        forgotpassword = findViewById(R.id.forgotPassword);

        mAuth = FirebaseAuth.getInstance();

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ResetPassword ResetPasswordfragment = new ResetPassword();
                fragmentTransaction.add(R.id.PasswordResetLayout, ResetPasswordfragment);
                fragmentTransaction.commit();
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailview.getText().toString().trim();
                String password = passwordview.getText().toString().trim();
                //Check if email/password is empty
                if(email.matches("") || password.matches("")){
                    Toast.makeText(getApplicationContext(), "Please Enter your email and password",
                            Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("Sign in: ", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        Intent intent = new Intent(login.this,DashBoard.class);
                                        //put uid in intent
                                        intent.putExtra("UID", user.getUid());
                                        //send to main activity
                                        startActivity(intent);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.d("Sign in Failure: ", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(getApplicationContext(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //send to main activity
            Intent intent = new Intent(login.this, DashBoard.class);
            //put uid in intent
            intent.putExtra("UID", currentUser.getUid());
            //send to main activity
            startActivity(intent);
        }
    }
}