package com.example.chack_chack_messanger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.chack_chack_messanger.adapter.UserAdapter;
import com.example.chack_chack_messanger.entity.User;
import com.example.chack_chack_messanger.viewmodel.UsersViewModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private static final String EXTRA_CURRENT_USER_ID = "current_id";
    private UsersViewModel usersViewModel;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        initViews();
        currentUserId = getIntent().getStringExtra(EXTRA_CURRENT_USER_ID);
        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        observeViewModel();
        userAdapter.setOnUserClickLister(new UserAdapter.OnUserClickLister() {
            @Override
            public void onUserClick(User user) {
                Intent intent = ChatActivity.chatIntent(UsersActivity.this, currentUserId, user.getId());
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewUsers);
        userAdapter = new UserAdapter();
        recyclerView.setAdapter(userAdapter);
    }

    public void observeViewModel() {
        usersViewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser == null) {
                    Intent intent = LoginActivity.loginIntent(UsersActivity.this);
                    startActivity(intent);
                    finish();
                }
            }
        });
        usersViewModel.getUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                userAdapter.setUsers(users);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_logout) {
            usersViewModel.logout();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        usersViewModel.setUserOnline(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        usersViewModel.setUserOnline(false);
    }

    public static Intent usersIntent(Context context, String currentUserId) {
        Intent intent = new Intent(context, UsersActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId);
        return intent;
    }
}