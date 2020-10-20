package com.example.fpy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

        textusername.setText(user.getUsername());
        final DrawerLayout drawerLayout = findViewById(R.id.drawable);
        NavigationView navigationView=findViewById(R.id.navview);

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
                        PasswordDialog();
                        return true;
                    case R.id.live_chat:
                        Intent intent2 = new Intent(DashBoard.this,live_chats.class);
                        startActivity(intent2);
                        return true;
                    case R.id.logout:
                        mAuth.signOut();
                        //send to main activity
                        Intent logoutintent = new Intent(DashBoard.this, login.class);
                        startActivity(logoutintent);
                        return true;

                }
                return false;
            }
        });

        //on nav view clicked
        findViewById(R.id.imageView3).setOnClickListener(new View.OnClickListener() {
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
        int images[] = {R.drawable.hello, R.drawable.wel};
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
    }

    //Alert Dialog
    public void PasswordDialog()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(DashBoard.this);
        View popup = getLayoutInflater().inflate(R.layout.change_password,null);
        final EditText current_password=popup.findViewById(R.id.current_password);
        final EditText new_password=popup.findViewById(R.id.new_password);
        final EditText confirm_password=popup.findViewById(R.id.confirm_password);
        Button save= popup.findViewById(R.id.confirm);
        Button cancel=popup.findViewById(R.id.cancel);
        builder.setView(popup);

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Test CURRENT PASS: ",current_password.getText().toString());
                Log.d("Test NEW PASS: ",new_password.getText().toString());
                Log.d("Test: CONFIRM PASS",confirm_password.getText().toString());

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
