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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

//livechat activity
public class live_chats extends AppCompatActivity {
    //firebase services
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //user details
    User user = User.getInstance();

    //variables
   ArrayList<Messages> messages = new ArrayList<Messages>();
    //views
    private RecyclerView recyclerView;
    private ImageButton sendmessage;
    private EditText msgtext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_chats);
        //firestore reference
        final CollectionReference docRef = db.collection("landlord").document(user.getUid()).collection("chatroom");

        //variables
        final chatview_adapter adapter = new chatview_adapter(messages);
        //view assignments
        recyclerView = findViewById(R.id.chatview);
        sendmessage = findViewById(R.id.sendmsg);
        msgtext = findViewById(R.id.msgtext);

        //set the adapter to recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);

//        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for (QueryDocumentSnapshot document : task.getResult()) {
//
//                    String name = document.getString("user");
//                    String msg =  document.getString("message");
//
//                    int time = (int) (System.currentTimeMillis());
//                    Timestamp tsTemp = new Timestamp(time);
//
//                    Messages item = new Messages(name, msg, tsTemp);
//                    messages.add(item);
//                }
//
//            }
//        });

        sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msgview = msgtext.getText().toString();
                if(!msgview.isEmpty()){
                    int time = (int) (System.currentTimeMillis());
                    Timestamp tsTemp = new Timestamp(time);

                    Map<String, Object> update = new HashMap<>();
                    update.put("message", msgview);
                    update.put("user", "user");
                    update.put("time",tsTemp);

                    docRef.add(update);
                }
            }
        });

        docRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed:" + e);
                    return;
                }

                ArrayList<Messages> newmessages = new ArrayList<Messages>();
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    String name = doc.getString("user");
                    String msg =  doc.getString("message");
                    int time = (int) (System.currentTimeMillis());
                    Timestamp tsTemp = new Timestamp(time);

                    Messages msgs = new Messages(name,msg,tsTemp);
                    newmessages.add(msgs);
                    Log.d("livechat Listener: ","" + msgs.getMessage());
                }
                adapter.updateItems(newmessages);
            }
        });
    }
}