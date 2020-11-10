package com.example.fpy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class reminder extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView reminderlist;
    private FirestoreRecyclerAdapter adapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preminderlist);
        ImageView imageView = findViewById(R.id.back_button);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();
        reminderlist=findViewById(R.id.paymentreminder);

        Query query = firebaseFirestore.collection("billing").whereEqualTo("user_id", user.getUid());

        FirestoreRecyclerOptions<paymentlist> options = new FirestoreRecyclerOptions.Builder<paymentlist>()
                .setQuery(query,paymentlist.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<paymentlist, RpaymentViewHolder>(options) {
            @NonNull
            @Override
            public RpaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem,parent,false);
                return new RpaymentViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull RpaymentViewHolder holder, int position, @NonNull paymentlist model) {

                holder.description.setText(model.getDescription());
                holder.amount.setText(String.format(Locale.ENGLISH, "RM %.2f", model.getAmount() ));

            }
        };
        reminderlist.setHasFixedSize(true);
        reminderlist.setLayoutManager(new LinearLayoutManager(this));
        reminderlist.setAdapter(adapter);
    }


    private class RpaymentViewHolder extends RecyclerView.ViewHolder {

        private final TextView amount;
        private TextView description;
        private ImageView img;


        public RpaymentViewHolder(@NonNull View itemView) {
            super(itemView);

            amount = itemView.findViewById(R.id.Title);
            description=itemView.findViewById(R.id.description);
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

