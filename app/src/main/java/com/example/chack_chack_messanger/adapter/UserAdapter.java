package com.example.chack_chack_messanger.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chack_chack_messanger.R;
import com.example.chack_chack_messanger.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> users = new ArrayList<>();
    private OnUserClickLister onUserClickLister;

    public void setOnUserClickLister(OnUserClickLister onUserClickLister) {
        this.onUserClickLister = onUserClickLister;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        String userInfo = String.format("%s %s, %s",
                user.getName(),
                user.getSurname(),
                user.getAge());
        holder.textViewUserInfo.setText(userInfo);
        int bgResId;
        if (user.isOnline()) {
            bgResId = R.drawable.circle_green;
        } else {
            bgResId = R.drawable.circle_red;
        }
        Drawable background = ContextCompat.getDrawable(holder.itemView.getContext(), bgResId);
        holder.onlineStatus.setBackground(background);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onUserClickLister != null) {
                    onUserClickLister.onUserClick(user);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public interface OnUserClickLister {
        void onUserClick(User user);
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewUserInfo;
        private View onlineStatus;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserInfo = itemView.findViewById(R.id.textViewUserInfo);
            onlineStatus = itemView.findViewById(R.id.onlineStatus);
        }
    }
}
