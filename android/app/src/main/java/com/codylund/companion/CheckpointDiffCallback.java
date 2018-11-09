package com.codylund.companion;

import android.support.v7.util.DiffUtil;

import java.util.List;
import java.util.Objects;

public class CheckpointDiffCallback extends DiffUtil.Callback {

    private final List<CheckpointItem> mOldList;
    private final List<CheckpointItem> mNewList;

    public CheckpointDiffCallback(List<CheckpointItem> oldList, List<CheckpointItem> newList) {
        this.mOldList = oldList;
        this.mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        if (this.mOldList != null)
            return this.mOldList.size();
        else
            return 0;
    }

    @Override
    public int getNewListSize() {
        if (this.mNewList != null)
            return this.mNewList.size();
        else
            return 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        CheckpointItem oldItem = mOldList.get(oldItemPosition);
        CheckpointItem newItem = mNewList.get(newItemPosition);

        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        CheckpointItem oldItem = mOldList.get(oldItemPosition);
        CheckpointItem newItem = mNewList.get(newItemPosition);

        boolean equal = true;
        equal &= (oldItem.getTime().equals(newItem.getTime()));
        equal &= (oldItem.getLat() == (newItem.getLat()));
        equal &= (oldItem.getLng() == (newItem.getLng()));
        equal &= (Objects.equals(oldItem.getImagePath(), newItem.getImagePath()));
        equal &= (Objects.equals(oldItem.getText(), newItem.getText()));
        equal &= (oldItem.getShared() == newItem.getShared());

        return equal;
    }
}
