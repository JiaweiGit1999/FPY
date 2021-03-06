package com.example.fpy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class payment_history extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView paymentlist;
    private FirestoreRecyclerAdapter adapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);

        ImageView imageView = findViewById(R.id.back_button);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();
        paymentlist=findViewById(R.id.paymentlist);

        Query query = firebaseFirestore.collection("payment").whereEqualTo("user_id", user.getUid());

        FirestoreRecyclerOptions<payment_model> options = new FirestoreRecyclerOptions.Builder<payment_model>()
                .setQuery(query,payment_model.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<payment_model, paymentViewHolder>(options) {
            @NonNull
            @Override
            public paymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_history_list,parent,false);
                return new paymentViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull paymentViewHolder paymentViewHolder, int i, @NonNull payment_model payment_model) {
                paymentViewHolder.Tamount.setText(String.format(Locale.ENGLISH, "RM %.2f", payment_model.getAmount() / 100));
                paymentViewHolder.description.setText(payment_model.getDescription());
                if (payment_model.getStatus().equals("Successful")) {
                    paymentViewHolder.img.setImageResource(R.drawable.success);
                } else if (payment_model.getStatus().equals("Failed")) {
                    paymentViewHolder.img.setImageResource(R.drawable.fail);
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMMM yyyy hh:ss aa");
                paymentViewHolder.time.setText(simpleDateFormat.format(payment_model.getTime()));
            }
        };
        paymentlist.setHasFixedSize(true);
        paymentlist.setLayoutManager(new LinearLayoutManager(this));
        paymentlist.setAdapter(adapter);
    }


    private class paymentViewHolder extends RecyclerView.ViewHolder {

        private final TextView Tamount;
        private TextView description;
        private ImageView img;
        private TextView time;
        private TextView user_id;


        public paymentViewHolder(@NonNull View itemView) {
            super(itemView);

            Tamount = itemView.findViewById(R.id.amount);
            description=itemView.findViewById(R.id.description);
            img = itemView.findViewById(R.id.status);
            time=itemView.findViewById(R.id.date);
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
