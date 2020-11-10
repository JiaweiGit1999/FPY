package com.example.fpy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.Reference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG;

public class UnitsOwned extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView unitlist;
    private FirestoreRecyclerAdapter adapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_own);

        ImageView imageView = findViewById(R.id.back_button);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        firebaseFirestore = FirebaseFirestore.getInstance();
        unitlist = findViewById(R.id.unit_owned);


        Query query = firebaseFirestore.collection("landlord").whereEqualTo("landlords",user.getUid());

        FirestoreRecyclerOptions<unit_model> options = new FirestoreRecyclerOptions.Builder<unit_model>()
                .setQuery(query,unit_model.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<unit_model, unitViewHolder>(options) {
            @NonNull
            @Override
            public unitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.unit_list,parent,false);
                return new unitViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final unitViewHolder unitViewHolder, int i, @NonNull final unit_model unit_model) {

                    unitViewHolder.name1.setText(unit_model.getName());

                    unitViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(UnitsOwned.this,Tenant.class);
                            intent.putExtra("contact",unit_model.getContact());
                            intent.putExtra("email",unit_model.getEmail());
                            intent.putExtra("ic",unit_model.getIc());
                            intent.putExtra("name",unit_model.getName());
                            startActivity(intent);

                        }
                    });

            }
        };
        unitlist.setHasFixedSize(true);
        unitlist.setLayoutManager(new LinearLayoutManager(this));
        unitlist.setAdapter(adapter);
    }


    private class unitViewHolder extends RecyclerView.ViewHolder {

        private TextView name1;

        public unitViewHolder(@NonNull View itemView) {
            super(itemView);

            name1 = itemView.findViewById(R.id.name);


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
