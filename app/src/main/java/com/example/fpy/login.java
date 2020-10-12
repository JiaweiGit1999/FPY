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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                                        loginsuccess();

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

    public void loginsuccess(){
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference docRef = db.collection("landlord").document(currentUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        Log.d("Document: ", currentUser.getUid());

                        //set global variables
                        User user= User.getInstance();
                        user.setUid(currentUser.getUid());

                        user.setUsername(document.getString("name"));
                        user.setGender(document.getString("gender"));
                        user.setIc(document.getString("ic"));
                        user.setEmail(document.getString("email"));
                        user.setContact(document.getString("contact"));
                        user.setImageurl(document.getString("imageurl"));
                        
                        //send to main activity
                        Intent intent = new Intent(getApplicationContext(), DashBoard.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.d("Document: ", "No such document");
                    }
                } else {
                    Log.d("Document: ", "get failed with ", task.getException());
                }
            }
        });
    }
}