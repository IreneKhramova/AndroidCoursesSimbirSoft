package com.example.irene.androidcourses;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    private Button saveBtn;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText nameEditText;
    private TextInputLayout emailLayout;
    private TextInputLayout phoneLayout;
    private TextInputLayout nameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if ( isDataValid() ) {
                    //
                }*/
            }
        });

        emailEditText = findViewById(R.id.email);
        phoneEditText = findViewById(R.id.phone);
        nameEditText = findViewById(R.id.name);
        emailLayout = findViewById(R.id.emailTextInputLayout);
        phoneLayout = findViewById(R.id.phoneTextInputLayout);
        nameLayout = findViewById(R.id.nameTextInputLayout);


        nameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                isNameValid();
            }
        });

        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                isEmailValid();
            }
        });

        phoneEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                isPhoneValid();
            }
        });
    }

    private boolean isNameValid() {
        boolean result = true;

        String name = nameEditText.getText().toString();
        if (name == null || name.length() < 4) {
            nameLayout.setError(getString(R.string.invalidName));
            result = false;
        } else {
            nameLayout.setErrorEnabled(false);
        }
        return result;
    }

    private boolean isPhoneValid() {
        boolean result = true;

        String phone = phoneEditText.getText().toString();
        if (!(android.util.Patterns.PHONE.matcher(phone).matches())) {
            phoneLayout.setError(getString(R.string.invalidPhone));
            result = false;
        } else {
            phoneLayout.setErrorEnabled(false);
        }
        return result;
    }

    private boolean isEmailValid() {
        boolean result = true;

        String email = emailEditText.getText().toString();
        if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            emailLayout.setError(getString(R.string.invalidEmail));
            result = false;
        } else {
            emailLayout.setErrorEnabled(false);
        }

        return result;
    }
}
