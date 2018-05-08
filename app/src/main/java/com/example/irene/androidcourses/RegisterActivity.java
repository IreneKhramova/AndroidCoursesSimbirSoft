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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button registerBtn;
    // статические поля лучше объявлять отдельно от нестатических
    private static final String TAG = "AndroidCourses";
    private EditText emailEditText;
    private EditText passwdEditText;
    private TextInputLayout emailLayout;
    private TextInputLayout passwdLayout;
    private Validator validator;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = database.getReference().child("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        emailEditText = findViewById(R.id.email);
        passwdEditText = findViewById(R.id.password);
        emailLayout = findViewById(R.id.emailInput);
        passwdLayout = findViewById(R.id.passwordInput);

        validator = new Validator();

        passwdEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (validator.isPasswordValid(passwdEditText.getText().toString())) {
                    passwdLayout.setErrorEnabled(false);
                } else {
                    passwdLayout.setError(getString(R.string.invalid_password));
                }
            }
        });

        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (validator.isEmailValid(emailEditText.getText().toString())) {
                    emailLayout.setErrorEnabled(false);
                } else {
                    emailLayout.setError(getString(R.string.invalidEmail));
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
        registerBtn = findViewById(R.id.sign_up_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDataValid()) {
                    emailLayout.setErrorEnabled(false);
                    passwdLayout.setErrorEnabled(false);

                    mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwdEditText.getText().toString())
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        usersRef.child(user.getUid()).child("email").setValue(user.getEmail());
                                        Log.d(TAG, "createUserWithEmail:success");
                                        updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Ошибка аутентификации.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
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

    // логичнее этот метод перенести в класс валидатора
    public boolean isDataValid() {
        if(validator.isEmailValid(emailEditText.getText().toString()) &&
                validator.isPasswordValid(passwdEditText.getText().toString())) {
            return true;
        }
        return false;
    }
}
