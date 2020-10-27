package com.example.fpy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

import java.util.List;

public class login extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    EditText emailview,passwordview;
    TextView message,forgotpassword;
    //set global variables


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
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
                View popup = getLayoutInflater().inflate(R.layout.forgot_password,null);
                final EditText email=popup.findViewById(R.id.email);
                Button save =popup.findViewById(R.id.save);
                builder.setView(popup);

                final AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(true);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("Test: ",email.getText().toString());
                    }
                });
                alertDialog.show();


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
        Log.d("Document: ", currentUser.getUid());
        DocumentReference docRef = db.collection("landlord").document(currentUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        User user= User.getInstance();


                        user.reset();
                        //set data of user class
                        user.setUid(currentUser.getUid());
                        user.setUsername(document.getString("name"));
                        user.setGender(document.getString("gender"));
                        user.setIc(document.getString("ic"));
                        user.setEmail(document.getString("email"));
                        user.setContact(document.getString("contact"));
                        user.setImageurl(document.getString("imageurl"));
                        user.setRole(document.getString("role"));
                        user.setUnit( (List<String>) document.get("unit"));
                        Log.d("Document:",user.getUsername());

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