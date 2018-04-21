package com.example.irene.androidcourses;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private Button saveBtn;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText nameEditText;
    private TextInputLayout emailLayout;
    private TextInputLayout phoneLayout;
    private TextInputLayout nameLayout;
    private FirebaseUser user;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = database.getReference().child("users");
    private static final String TAG = "AndroidCourses";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        emailEditText = findViewById(R.id.email);
        phoneEditText = findViewById(R.id.phone);
        nameEditText = findViewById(R.id.name);
        emailLayout = findViewById(R.id.emailTextInputLayout);
        phoneLayout = findViewById(R.id.phoneTextInputLayout);
        nameLayout = findViewById(R.id.nameTextInputLayout);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            emailEditText.setText(user.getEmail());

            usersRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    //Log.d(TAG, "User is: " + u.getName());
                    if(dataSnapshot.exists()) {
                        User u = dataSnapshot.getValue(User.class);
                        nameEditText.setText(u.getName());
                        phoneEditText.setText(u.getPhone());
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read user.", error.toException());
                    Toast.makeText(ProfileActivity.this, "Ошибка",
                                  Toast.LENGTH_SHORT).show();
                }
            });

            saveBtn = findViewById(R.id.save_btn);
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isNameValid() && isEmailValid() && isPhoneValid()) {

                        User us = new User(nameEditText.getText().toString(), phoneEditText.getText().toString());
                        usersRef.child(user.getUid()).setValue(us);

                        //TODO: re-authenticate user for changing email
                        /*AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldpass);

                        // Prompt the user to re-provide their sign-in credentials
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d(TAG, "User re-authenticated.");
                                    }
                                });
*/


                    user.updateEmail(emailEditText.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.e(TAG, "User email address updated.");
                                    }
                                }
                            });

                        Toast.makeText(ProfileActivity.this, "Информация обновлена.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

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
