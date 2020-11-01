package com.example.fpy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Notice extends AppCompatActivity {

    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private User user = User.getInstance();
    private RecyclerView recyclerView;
    private MyAdapter recycleAdapter = new MyAdapter();
    private ArrayList<AnnouncementList> announcementList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        Intent dataIntent = getIntent();
        if (dataIntent != null) {
            announcementList = dataIntent.getParcelableArrayListExtra("announcement");
        }

        ImageView button = findViewById(R.id.back_button);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setAdapter(recycleAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        public class ViewHolder extends RecyclerView.ViewHolder {
            public final TextView title;
            public final TextView detail;
            public final TextView date;
            public final ImageView image;

            public ViewHolder(View v) {
                super(v);
                title = (TextView) v.findViewById(R.id.title);
                detail = (TextView) v.findViewById(R.id.detail);
                date = (TextView) v.findViewById(R.id.date);
                image = (ImageView) v.findViewById(R.id.announcementIcon);
            }
        }

        @Override
        public int getItemCount() {
            return announcementList.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM");
            GlideApp.with(Notice.this)
                    .load(mStorageRef.child("announcement/" + announcementList.get(position).getImageurl()))
                    .into(holder.image);
            holder.title.setText(announcementList.get(position).getTitle());
            holder.detail.setText(announcementList.get(position).getDescription());
            if (position == 0) {
                holder.date.setText(simpleDateFormat.format(announcementList.get(position).getDate()));
            } else {
                Calendar date1 = Calendar.getInstance();
                Calendar date2 = Calendar.getInstance();
                date1.setTime(announcementList.get(position).getDate());
                date2.setTime(announcementList.get(position - 1).getDate());
                if (date1.get(Calendar.DAY_OF_YEAR) != date2.get(Calendar.DAY_OF_YEAR)) {
                    holder.date.setText(simpleDateFormat.format(announcementList.get(position).getDate()));
                }
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Notice.this, Announcement.class);
                    intent.putExtra("imageurl", announcementList.get(position).getImageurl());
                    intent.putExtra("title", announcementList.get(position).getTitle());
                    intent.putExtra("detail", announcementList.get(position).getDescription());
                    intent.putExtra("time", announcementList.get(position).getDate().getTime());
                    startActivity(intent);
                }
            });
        }
    }
}