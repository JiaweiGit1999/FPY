package com.example.fpy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class chatview_adapter extends RecyclerView.Adapter<chatview_adapter.Holder> {

    private ArrayList<Messages> messages = new ArrayList<Messages>();

    public chatview_adapter(ArrayList<Messages> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public chatview_adapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.layout_receiver, parent, false);

        // Return a new holder instance
        Holder viewHolder = new Holder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull chatview_adapter.Holder holder, int position) {
        TextView nameview = holder.nameTextView;
        TextView messageview = holder.message;

        Messages message = messages.get(position);

        nameview.setText(message.getName());
        messageview.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView message;
        public Holder(@NonNull View itemView) {
            super(itemView);


            nameTextView = (TextView) itemView.findViewById(R.id.Rname);
            message = (TextView) itemView.findViewById(R.id.receiver);
        }
    }
}
