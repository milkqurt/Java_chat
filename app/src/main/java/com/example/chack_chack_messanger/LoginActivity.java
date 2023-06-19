package com.example.chack_chack_messanger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chack_chack_messanger.viewmodel.LoginViewModel;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button loginButton;
    private TextView textViewRegister;
    private TextView textViewForgotPassword;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        observeViewModel();
        setupClickListeners();
    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                loginViewModel.login(email, password);
            }
        });
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ResetPasswordActivity.resetPasswordIntent(
                        LoginActivity.this,editTextEmail.getText().toString().trim());
                startActivity(intent);
            }
        });
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = RegistrationActivity.registrationIntent(LoginActivity.this);
                startActivity(intent);
            }
        });
    }

    private void observeViewModel() {
        loginViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }
        });
        loginViewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Intent intent = UsersActivity.usersIntent(LoginActivity.this,
                            firebaseUser.getUid());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
    }

    public static Intent loginIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }
}