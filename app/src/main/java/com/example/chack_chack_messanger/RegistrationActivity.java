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

import com.example.chack_chack_messanger.viewmodel.RegistrationViewModel;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextAge;
    private Button buttonRegister;
    private RegistrationViewModel registrationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initViews();
        registrationViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        observeViewModel();
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = getTrimmedValue(editTextEmail);
                String password = getTrimmedValue(editTextPassword);
                String name = getTrimmedValue(editTextName);
                String surname = getTrimmedValue(editTextSurname);
                int age = Integer.parseInt(getTrimmedValue(editTextAge));
                registrationViewModel.signUp(email, password, name, surname, age);
            }
        });
    }

    private void observeViewModel() {
        registrationViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    Toast.makeText(RegistrationActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }
        });
        registrationViewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Intent intent = UsersActivity.usersIntent(RegistrationActivity.this,
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
        editTextName = findViewById(R.id.editTextName);
        editTextSurname = findViewById(R.id.editTextSurname);
        editTextAge = findViewById(R.id.editTextAge);
        buttonRegister = findViewById(R.id.buttonRegister);
    }

    private String getTrimmedValue(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static Intent registrationIntent(Context context) {
        return new Intent(context, RegistrationActivity.class);
    }
}