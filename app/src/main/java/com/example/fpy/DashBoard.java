package com.example.fpy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class DashBoard extends AppCompatActivity {
    ViewFlipper slider;
    private String username,email,contact,ic;
    boolean doubleBackToExitPressedOnce = false;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        mAuth = FirebaseAuth.getInstance();

        final DrawerLayout drawerLayout = findViewById(R.id.drawable);
        NavigationView navigationView=findViewById(R.id.navview);
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

        findViewById(R.id.imageView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);
            }

        }
        );
        int images[]={R.drawable.hello,R.drawable.wel};
         slider = findViewById(R.id.slider1);

        for (int image:images)
        {
            flipperimage(image);

        }

        CardView cardView2 = findViewById(R.id.facility);
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoard.this,Facility.class);
                startActivity(intent);
            }
        });

        CardView cardView1 = findViewById(R.id.Payment);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoard.this,Payment.class);
                startActivity(intent);
            }
        });
    }

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

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }






}
