package com.example.fpy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Locale;

public class booking_reminder extends AppCompatActivity
{
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView bookinglist;
    private FirestoreRecyclerAdapter adapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookingreminderlist);
        ImageView imageView = findViewById(R.id.back_button);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();
        bookinglist=findViewById(R.id.bookingreminder);

        Query query = firebaseFirestore.collection("booking").whereEqualTo("user_id", user.getUid());

        FirestoreRecyclerOptions<bookinglist> options = new FirestoreRecyclerOptions.Builder<bookinglist>()
                .setQuery(query,bookinglist.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<bookinglist, RbookingViewholder>(options) {
            @NonNull
            @Override
            public RbookingViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookingcarditem,parent,false);
                return new RbookingViewholder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull RbookingViewholder holder, int position, @NonNull bookinglist model) {

                if(model.getFacility().equals("Ping-Pong Table"))
                {
                    holder.img.setImageResource(R.drawable.ball);

                }else if (model.getFacility().equals("AV Room"))
                {
                    holder.img.setImageResource(R.drawable.cinema);
                }else if (model.getFacility().equals("BBQ Pit"))
                {
                    holder.img.setImageResource(R.drawable.bbq);
                }else if (model.getFacility().equals("Sky Lounge"))
                {
                    holder.img.setImageResource(R.drawable.lounge);
                }
                holder.date.setText(model.getDate());
                holder.duration.setText(model.getDuration());
                holder.facility.setText(model.getFacility());
                holder.status.setText(model.getStatus());
                holder.time.setText(model.getTime());

            }
        };
        bookinglist.setHasFixedSize(true);
        bookinglist.setLayoutManager(new LinearLayoutManager(this));
        bookinglist.setAdapter(adapter);
    }


    private class RbookingViewholder extends RecyclerView.ViewHolder {


        private TextView date,duration,facility,status,time;
        private ImageView img;


        public RbookingViewholder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            duration = itemView.findViewById(R.id.duration);
            facility = itemView.findViewById(R.id.facility);
            status = itemView.findViewById(R.id.status);
            time = itemView.findViewById(R.id.time);
            img = itemView.findViewById(R.id.image);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }

}

