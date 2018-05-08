package com.example.irene.androidcourses;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FriendsAdapter extends
        RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {
    private List<Friend> friends = new ArrayList<>();

    public void setFriends(List<Friend> friends) {
        // Правильнее написать так
        // this.friends.clear();
        // this.friends.addAll(friends);
        this.friends = friends;

        Collections.sort(friends, new Comparator<Friend>() {
            @Override
            public int compare(Friend lhs, Friend rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
    }

    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_item, parent, false);
        return new FriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position) {
        holder.setFriend(friends.get(position));
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public class FriendsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView message;
        private TextView name;
        private RoundedImageView avatar;
        private TextView date;
        private TextView messages_badge;

        FriendsViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            message = itemView.findViewById(R.id.message);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            messages_badge = itemView.findViewById(R.id.messages_badge);
            itemView.setOnClickListener(this);
        }

        void setFriend(Friend friend) {
            message.setText(friend.getMessage());
            name.setText(friend.getName());
            date.setText(friend.getDate());
            messages_badge.setText(friend.getMessagesBadge().toString());
            Picasso.get().load(friend.getAvatar()).into(avatar);
        }

        @Override
        public void onClick(View v) {
            // Имя переменной желательно должно быть более осмысленным
            final Context a = v.getContext();
            Intent intent = new Intent(a, MessagesListActivity.class);
            a.startActivity(intent);
        }
    }
}
