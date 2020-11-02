package com.example.fpy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
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


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import static com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG;


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
        String imgname = "";
        final User user = User.getInstance();

        //firebase links
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference AnnouncementRef = mStorageRef.child("announcement/"+imgname);
        final long ONE_MEGABYTE = 1024 * 1024;
        AnnouncementRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        docRef = db.collection("landlord").document(user.getUid());
        //views

        final TextView textusername = findViewById(R.id.name);
        CardView cardView4 = findViewById(R.id.notice);
        CardView cardView3 = findViewById(R.id.code);
        CardView cardView2 = findViewById(R.id.facility);
        CardView cardView1 = findViewById(R.id.Payment);
        ImageView imageView = findViewById(R.id.profileIcon);

        if (user.getImageurl() != null)
            GlideApp.with(this /* context */)
                    .load(mStorageRef.child(user.getImageurl()))
                    .into(imageView);

        ImageView pIcon = findViewById(R.id.profileIcon);

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
                        Intent intent1 = new Intent(DashBoard.this,payment_history.class);
                        startActivity(intent1);
                        return true;
                    case R.id.password:
                        Toast.makeText(DashBoard.this,"12112",Toast.LENGTH_SHORT).show();
                        PasswordDialog();
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

        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<AnnouncementList> announcementList = new ArrayList<>();
                FirebaseFirestore.getInstance().collection("announcement").orderBy("date", Query.Direction.DESCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        announcementList.add(new AnnouncementList(document.get("title").toString(), document.get("description").toString(), document.get("imageurl").toString(), new Date(document.getDate("date").getTime())));
                                    }
                                    Intent intent = new Intent(DashBoard.this, Notice.class);
                                    intent.putParcelableArrayListExtra("announcement", announcementList);
                                    startActivity(intent);
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

                FirebaseFirestore.getInstance().collection("announcement").document().addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            Log.d(TAG, "Current data: " + snapshot.getData());
                        } else {
                            Log.d(TAG, "Current data: null");
                        }
                    }
                });
            }
        });

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

                //Validate date
                String currentPassword = current_password.getText().toString().trim();
                String newPassword = new_password.getText().toString().trim();
                String confirmPassword = confirm_password.getText().toString().trim();

                if(TextUtils.isEmpty(currentPassword)){
                    Toast.makeText(view.getContext(), "Enter your current password...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newPassword.length() < 6){
                    Toast.makeText(view.getContext(), "Password must be at least 6 characters long...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(confirmPassword == newPassword){
                    Toast.makeText(view.getContext(), "Password updated", Toast.LENGTH_SHORT).show();
                    return;
                }

                updatePassword(currentPassword, newPassword);

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

    private void updatePassword(String currentPassword, final String newPassword) {
        //get current user
        final FirebaseUser user = mAuth.getCurrentUser();

        //before changing password, re-authenticate user
        AuthCredential authCredential  = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //successfully authenticated, begin update
                        user.updatePassword(newPassword)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //password update
                                        Toast.makeText(getBaseContext(), "Password updated...", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //fail to update password
                                        Toast.makeText(getBaseContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //authenticate failed, show reason
                        Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
