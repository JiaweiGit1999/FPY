package com.example.fpy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.InputStream;

import javax.annotation.Nullable;


public class DashBoard extends AppCompatActivity {
    ViewFlipper slider;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    DocumentReference docRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        //firebase services
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final User user = User.getInstance();

        //firebase links
        mStorageRef = FirebaseStorage.getInstance().getReference();
        docRef = db.collection("landlord").document(user.getUid());
        //views

        final TextView textusername = findViewById(R.id.name);
        CardView cardView2 = findViewById(R.id.facility);
        CardView cardView1 = findViewById(R.id.Payment);

        ImageView imageView = findViewById(R.id.profileIcon);
        if (user.getImageurl() != null)
            GlideApp.with(this /* context */)
                    .load(mStorageRef.child(user.getImageurl()))
                    .into(imageView);

        textusername.setText(user.getUsername());
        final DrawerLayout drawerLayout = findViewById(R.id.drawable);
        NavigationView navigationView=findViewById(R.id.navview);
        navigationView.setBackgroundResource(R.color.menuBackground);

        //on navigator options selected
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.profile11:
                        Intent intent = new Intent(DashBoard.this,Profile.class);
                        startActivity(intent);
                        return true;
                    case R.id.reminder:
                        Toast.makeText(DashBoard.this,"YOU3",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.history:
                        Toast.makeText(DashBoard.this,"1212",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.password:
                        Toast.makeText(DashBoard.this,"12112",Toast.LENGTH_SHORT).show();
                        return true;
                 }
                return false;
            }
        });

        //livechat
        findViewById(R.id.live_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(DashBoard.this,live_chats.class);
                startActivity(intent2);
            }
        });

        //logout (TextView)
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                //send to main activity
                Intent logoutintent = new Intent(DashBoard.this, login.class);
                startActivity(logoutintent);
            }
        });

        //on nav view clicked
        findViewById(R.id.profileIcon).setOnClickListener(new View.OnClickListener() {
                                                             @Override
                                                             public void onClick(View view) {
                                                                 drawerLayout.openDrawer(GravityCompat.END);
                                                                 if(user.getImageurl()!=null){
                                                                     //update profile pic
                                                                     ImageView userpic = findViewById(R.id.userpic);
                                                                     Log.d("profile ",user.getImageurl());
                                                                     GlideApp.with(DashBoard.this /* context */)
                                                                             .load(mStorageRef.child(user.getImageurl()))
                                                                             .into(userpic);
                                                                 }
                                                                 TextView username = findViewById(R.id.nav_username);
                                                                 username.setText(user.getUsername());
                                                             }
                                                         }
        );


        //flipper images
        int images[] = {R.drawable.ann1, R.drawable.ann2};
        slider = findViewById(R.id.slider1);

        for (int image : images) {
            flipperimage(image);

        }
        //card views on click

        CardView cardView3 = findViewById(R.id.code);
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoard.this, QrCode.class);
                startActivity(intent);
            }
        });

        //facility card
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoard.this, Facility.class);
                startActivity(intent);
            }
        });
        //payment card
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoard.this,Payment.class);
                startActivity(intent);
            }
        });

        //on firestore data changed
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed: " + e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    textusername.setText(documentSnapshot.getString("name"));
                } else {
                    System.out.print("Current data: null");
                }
            }
        });
    }

    //on options selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.profile11)
        {
            Toast.makeText(this,"YOU CLICKED PROFILE",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DashBoard.this,Profile.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //flipper image setter
    public void flipperimage(int image)
    {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        slider.addView(imageView);
        slider.setFlipInterval(4000);
        slider.setAutoStart(true);
        slider.setInAnimation(this,android.R.anim.slide_in_left);
        slider.setInAnimation(this,android.R.anim.slide_out_right);
    }}
