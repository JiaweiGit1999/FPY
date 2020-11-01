package com.example.fpy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Announcement extends AppCompatActivity {

    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        TextView title = findViewById(R.id.title);
        TextView date = findViewById(R.id.date);
        TextView detail = findViewById(R.id.details);
        ImageView imageView = findViewById(R.id.imageView2);
        Button button = findViewById(R.id.button);

        Intent dataintent = getIntent();
        if (dataintent != null) {
            try {
                title.setText(dataintent.getStringExtra("title"));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy hh:ss");
                date.setText(simpleDateFormat.format(new Date(dataintent.getLongExtra("time", 0))));
                GlideApp.with(Announcement.this)
                        .load(mStorageRef.child("announcement/" + dataintent.getStringExtra("imageurl")))
                        .into(imageView);
                detail.setText(dataintent.getStringExtra("detail"));
            } catch (Exception e) {
                Log.d("Error", e.toString());
            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}