package com.example.irene.androidcourses;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.Manifest;

public class ProfileActivity extends AppCompatActivity {
    private Button saveBtn;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText nameEditText;
    private TextInputLayout emailLayout;
    private TextInputLayout phoneLayout;
    private TextInputLayout nameLayout;
    private ImageView avatarView;
    private FirebaseUser user;
    private Validator validator;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = database.getReference().child("users");
    private static final String TAG = "AndroidCourses";
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

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
        avatarView = findViewById(R.id.avatar);

        avatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(ProfileActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(ProfileActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }
                else {
                    dispatchTakePictureIntent();
                }
            }
        });


        validator = new Validator();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            usersRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    User u = dataSnapshot.getValue(User.class);
                    nameEditText.setText(u.getName());
                    phoneEditText.setText(u.getPhone());
                    emailEditText.setText(u.getEmail());
                    Log.d(TAG, "User is: " + u.getName());
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
                    if (isDataValid()) {
                        nameLayout.setErrorEnabled(false);
                        emailLayout.setErrorEnabled(false);
                        phoneLayout.setErrorEnabled(false);

                        User us = new User(nameEditText.getText().toString(), phoneEditText.getText().toString(), emailEditText.getText().toString());
                        usersRef.child(user.getUid()).setValue(us);

                        Toast.makeText(ProfileActivity.this, "Информация обновлена.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            nameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (validator.isNameValid(nameEditText.getText().toString())) {
                        nameLayout.setErrorEnabled(false);
                    } else {
                        nameLayout.setError(getString(R.string.invalidName));
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

            phoneEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (validator.isPhoneValid(phoneEditText.getText().toString())) {
                        phoneLayout.setErrorEnabled(false);
                    } else {
                        phoneLayout.setError(getString(R.string.invalidPhone));
                    }
                }
            });
        }
    }

    // и этот метод лучше перенести в класс валидатора)
    public boolean isDataValid() {
        if(validator.isNameValid(nameEditText.getText().toString()) &&
                validator.isEmailValid(emailEditText.getText().toString()) &&
                validator.isPhoneValid(phoneEditText.getText().toString())) {
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            avatarView.setImageBitmap(imageBitmap);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (permissions.length == 1 &&
                        permissions[0].equals(Manifest.permission.CAMERA) &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                }
            }
        }
    }
}
