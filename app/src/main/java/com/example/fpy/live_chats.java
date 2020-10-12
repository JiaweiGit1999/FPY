package com.example.fpy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;

public class live_chats extends AppCompatActivity {
    //firebase services
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //user details
    User user = User.getInstance();

    //variables
    private ArrayList<Messages> messages = new ArrayList<Messages>();
    //views
    private RecyclerView recyclerView;
    private ImageButton sendmessage;
    private EditText msgtext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_chats);
        //firestore reference
        CollectionReference docRef = db.collection("landlord").document(user.getUid()).collection("chatroom");

        //view assignments
        recyclerView = findViewById(R.id.chatview);
        sendmessage = findViewById(R.id.sendmsg);
        msgtext = findViewById(R.id.msgtext);

        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {

                    String name = document.getString("user");
                    String msg =  document.getString("message");

                    int time = (int) (System.currentTimeMillis());
                    Timestamp tsTemp = new Timestamp(time);

                    Messages item = new Messages(name, msg, tsTemp);
                    messages.add(item);
                }
                //set the adapter to recycler view
                chatview_adapter adapter = new chatview_adapter(messages);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }
        });



        sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}