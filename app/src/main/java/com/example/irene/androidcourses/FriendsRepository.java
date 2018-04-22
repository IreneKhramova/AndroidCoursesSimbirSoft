package com.example.irene.androidcourses;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendsRepository {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("friends");

    public void loadFriends(@NonNull final FriendsLoadListener friendsLoadListener) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendsLoadListener.onFriendsLoaded(toFriendList(dataSnapshot));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                friendsLoadListener.onError(databaseError.toException());
            }
        });
    }

    public interface FriendsLoadListener{
        void onFriendsLoaded(List<Friend> friends);
        void onError(Throwable error);
    }

    private List<Friend> toFriendList(DataSnapshot dataSnapshot) {
        List<Friend> friends = new ArrayList<>();

        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
            friends.add(snapshot.getValue(Friend.class));
        }

        return friends;
    }
}
