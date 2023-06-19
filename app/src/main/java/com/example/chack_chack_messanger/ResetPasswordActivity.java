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
import android.widget.Toast;

import com.example.chack_chack_messanger.viewmodel.ResetPasswordViewModel;

public class ResetPasswordActivity extends AppCompatActivity {

    private static final String EXTRA_EMAIL = "email";
    private EditText editTextEmail;
    private Button resetPasswordButton;
    private ResetPasswordViewModel resetPasswordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initViews();
        resetPasswordViewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);
        observeViewModel();
        String email = getIntent().getStringExtra(EXTRA_EMAIL);
        editTextEmail.setText(email);
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                resetPasswordViewModel.resetPassword(email);
            }
        });
    }

    private void observeViewModel() {
        resetPasswordViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    Toast.makeText(ResetPasswordActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }
        });
        resetPasswordViewModel.isSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(ResetPasswordActivity.this,
                            "Перейдите в свою электронную почту", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void initViews() {
        editTextEmail = findViewById(R.id.editTextResEmail);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
    }

    public static Intent resetPasswordIntent(Context context, String email) {
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        intent.putExtra(EXTRA_EMAIL, email);
        return intent;
    }
}