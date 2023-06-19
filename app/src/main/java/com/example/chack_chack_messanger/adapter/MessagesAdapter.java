package com.example.chack_chack_messanger.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chack_chack_messanger.R;
import com.example.chack_chack_messanger.entity.Message;

import java.util.ArrayList;
import java.util.List;


public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private static final int VIEW_MY_MESSAGE = 1;
    private static final int VIEW_OTHER_MESSAGE = 2;
    private String currentUserId;
    private List<Message> messages = new ArrayList<>();

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int resLayoutId;
        if (viewType == VIEW_MY_MESSAGE) {
            resLayoutId = R.layout.other_message_item;
        } else {
            resLayoutId = R.layout.my_message_item;
        }
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(resLayoutId, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getSenderId().equals(currentUserId)) {
            return VIEW_MY_MESSAGE;
        } else {
            return VIEW_OTHER_MESSAGE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.textViewMessage.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewMessage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
        }
    }
}
