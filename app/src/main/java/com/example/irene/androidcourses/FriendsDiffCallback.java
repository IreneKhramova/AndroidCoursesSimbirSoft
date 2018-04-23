package com.example.irene.androidcourses;

import android.support.v7.util.DiffUtil;

import java.util.List;

public class FriendsDiffCallback extends DiffUtil.Callback {
    private final List<Friend> oldList;
    private final List<Friend> newList;

    public FriendsDiffCallback(List<Friend> oldList, List<Friend> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Friend oldFriend = oldList.get(oldItemPosition);
        Friend newFriend = newList.get(newItemPosition);
        return oldFriend.getId().equals(newFriend.getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Friend oldFriend = oldList.get(oldItemPosition);
        Friend newFriend = newList.get(newItemPosition);
        return oldFriend.equals(newFriend);
    }
}
