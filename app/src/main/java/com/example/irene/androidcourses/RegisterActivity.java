package com.example.irene.androidcourses;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button registerBtn;
    private static final String TAG = "AndroidCourses";
    private EditText emailEditText;
    private EditText passwdEditText;
    private TextInputLayout emailLayout;
    private TextInputLayout passwdLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        emailEditText = findViewById(R.id.email);
        passwdEditText = findViewById(R.id.password);
        emailLayout = findViewById(R.id.emailInput);
        passwdLayout = findViewById(R.id.passwordInput);

        passwdEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                isPasswordValid();
            }
        });

        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                isEmailValid();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        registerBtn = findViewById(R.id.sign_up_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email = findViewById(R.id.email);
                EditText password = findViewById(R.id.password);
                
                if(isEmailValid() && isPasswordValid()) {
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Ошибка аутентификации.",
                                                Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }

                                    // ...
                                }
                            });
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void updateUI(FirebaseUser user)
    {
        if(user != null) {
            Context context = RegisterActivity.this;
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        }
    }

    private boolean isPasswordValid() {
        boolean result = true;

        int passwdSize = passwdEditText.getText().length();
        if (passwdSize < 6) {
            passwdLayout.setError(getString(R.string.short_password));
            result = false;
        } else {
            passwdLayout.setErrorEnabled(false);
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
