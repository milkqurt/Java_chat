package com.example.chack_chack_messanger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chack_chack_messanger.adapter.MessagesAdapter;
import com.example.chack_chack_messanger.entity.Message;
import com.example.chack_chack_messanger.entity.User;
import com.example.chack_chack_messanger.viewmodel.ChatViewModel;
import com.example.chack_chack_messanger.viewmodel.ChatViewModelFactory;

import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final String EXTRA_CURRENT_USER_ID = "current_id";
    private static final String EXTRA_OTHER_USER_ID = "other_id";
    private TextView textViewTitle;
    private View onlineStatus;
    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private ImageView imageViewSendMessage;
    private MessagesAdapter messagesAdapter;
    private String currentUserId;
    private String otherUserId;
    private ChatViewModel chatViewModel;
    private ChatViewModelFactory chatViewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initViews();
        currentUserId = getIntent().getStringExtra(EXTRA_CURRENT_USER_ID);
        otherUserId = getIntent().getStringExtra(EXTRA_OTHER_USER_ID);
        chatViewModelFactory = new ChatViewModelFactory(currentUserId, otherUserId);
        chatViewModel = new ViewModelProvider(this, chatViewModelFactory).get(ChatViewModel.class);
        messagesAdapter = new MessagesAdapter();
        recyclerViewMessages.setAdapter(messagesAdapter);
        observeViewModel();
        imageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message(editTextMessage.getText().toString().trim(),
                        currentUserId, otherUserId);
                chatViewModel.sendMessage(message);
            }
        });
    }

    public void observeViewModel() {
        chatViewModel.getMessages().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                messagesAdapter.setMessages(messages);
            }
        });
        chatViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    Toast.makeText(ChatActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }
        });
        chatViewModel.getMessageSent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    editTextMessage.setText("");
                }
            }
        });
        chatViewModel.getOtherUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                String userInfo = String.format("%s %s", user.getName(), user.getSurname());
                textViewTitle.setText(userInfo);
                int bgResId;
                if (user.isOnline()) {
                    bgResId = R.drawable.circle_green;
                } else {
                    bgResId = R.drawable.circle_red;
                }
                Drawable background = ContextCompat.getDrawable(ChatActivity.this, bgResId);
                onlineStatus.setBackground(background);
            }
        });
    }

    private void initViews() {
        textViewTitle = findViewById(R.id.textViewTitle);
        onlineStatus = findViewById(R.id.onlineStatus);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        imageViewSendMessage = findViewById(R.id.imageViewSendMessage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        chatViewModel.setUserOnline(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        chatViewModel.setUserOnline(false);
    }


    public static Intent chatIntent(Context context, String currentUserId, String otherUserId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId);
        intent.putExtra(EXTRA_OTHER_USER_ID, otherUserId);
        return intent;
    }
}