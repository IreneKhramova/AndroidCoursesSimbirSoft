package com.example.irene.androidcourses;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

public class MessagesListActivity extends AppCompatActivity {
    private MessagesList messagesList;
    MessageInput inputView;
    private FirebaseUser user;
    private String userName;
    private DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.messagesList = findViewById(R.id.messagesList);

        user = FirebaseAuth.getInstance().getCurrentUser();
        final MessagesListAdapter<Message> adapter = new MessagesListAdapter<>(user.getUid(), null);
        messagesList.setAdapter(adapter);

        final MessagesRepository messagesRepository = new MessagesRepository();
        messagesRepository.loadMessages(new MessagesRepository.MessagesLoadListener() {
            @Override
            public void onMessagesReceived(Message message) {
                adapter.addToStart(message, true);
            }

            @Override
            public void onError(Throwable error) {

            }
        });




        inputView = findViewById(R.id.input);
        inputView.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                //validate and send message
                messagesRepository.addMessage(input.toString(), userName);
                return true;
            }
        });


        usersRef.child(user.getUid()).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read user.", error.toException());
            }
        });
    }
}
