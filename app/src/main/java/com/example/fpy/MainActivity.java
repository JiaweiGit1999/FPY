package com.example.fpy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static int screen=5000;
    Animation logo_Animation;
    ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        logo_Animation= AnimationUtils.loadAnimation(this,R.anim.logo_animation);
        logo=findViewById(R.id.imageView);
        logo.setAnimation(logo_Animation);

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        //if user exist,
        if (currentUser != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
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
            },screen);

        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this,login.class);
                    startActivity(intent);
                    finish();
                }
            },screen);
        }
    }
}