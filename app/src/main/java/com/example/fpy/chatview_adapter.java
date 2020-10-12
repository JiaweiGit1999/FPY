package com.example.fpy;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stripe.android.model.Source;

import java.util.ArrayList;

//livechat adapter
public class chatview_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Messages> messages = new ArrayList<Messages>();


    public chatview_adapter(ArrayList<Messages> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView;
        if(viewType == 1){
            // Inflate the custom layout
            contactView = inflater.inflate(R.layout.layout_receiver, parent, false);
            ReceiverHolder viewHolder = new ReceiverHolder(contactView);
            return viewHolder;
        }else if(viewType == 2){
            // Inflate the custom layout
            contactView = inflater.inflate(R.layout.layout_sender, parent, false);
            SenderHolder viewHolder = new SenderHolder(contactView);
            return viewHolder;
        }else{
            Log.d("Adapter: ", viewType+"");
            throw new RuntimeException("The type has to be ONE or TWO");
        }
        // Return a new holder instance

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 1:
                initLayoutOne((ReceiverHolder) holder, position);
                break;
            case 2:
                initLayoutTwo((SenderHolder) holder, position);
                break;
            default:
                break;
        }
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }
    // determine which layout to use for the row
    @Override
    public int getItemViewType(int position) {
        Messages item = messages.get(position);
        Log.d("Adapter: ",item.getName());
        if (item.getName().equals("user") ) {
            return 2;
        } else if (item.getName().equals("admin")) {
            return 1;
        } else {
            return -1;
        }
    }
    private void initLayoutOne(ReceiverHolder holder, int pos) {
        TextView nameview = holder.nameTextView;
        TextView messageview = holder.message;

        Messages message = messages.get(pos);

        nameview.setText(message.getName());
        messageview.setText(message.getMessage());
    }

    private void initLayoutTwo(SenderHolder holder, int pos) {
        TextView messageview = holder.sendermessage;

        Messages message = messages.get(pos);

        messageview.setText(message.getMessage());
    }

    public class ReceiverHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView message;
        public ReceiverHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.Rname);
            message = (TextView) itemView.findViewById(R.id.receiver);
        }
    }

    // Static inner class to initialize the views of rows
    static class SenderHolder extends RecyclerView.ViewHolder {
        public TextView sendermessage;
        public SenderHolder(View itemView) {
            super(itemView);
            sendermessage = (TextView) itemView.findViewById(R.id.sender);
        }
    }

    public void updateItems(ArrayList<Messages> newList) {
        messages.clear();
        messages.addAll(newList);
        this.notifyDataSetChanged();
    }
}
