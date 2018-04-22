package com.example.irene.androidcourses;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.UUID;

public class MessagesRepository {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference messagesRef = database.getReference("messages");
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public void loadMessages(@NonNull final MessagesRepository.MessagesLoadListener messagesLoadListener) {
        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                messagesLoadListener.onMessagesReceived(toMessageList(dataSnapshot));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                messagesLoadListener.onError(databaseError.toException());
            }
        });

    }

    public interface MessagesLoadListener{
        void onMessagesReceived(Message message);
        void onError(Throwable error);
    }

    public void addMessage(String text, String userName) {
        String id = UUID.randomUUID().toString();
        Message msg = new Message(id,text, Calendar.getInstance().getTime(),
                new Author(user.getUid(), userName, ""));
        messagesRef.child(id).setValue(msg);
    }

    private Message toMessageList(DataSnapshot dataSnapshot) {
        Message message = dataSnapshot.getValue(Message.class);
        return message;
    }
}
