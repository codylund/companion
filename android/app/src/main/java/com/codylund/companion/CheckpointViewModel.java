package com.codylund.companion;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

public class CheckpointViewModel extends AndroidViewModel {

    private static final String TAG = CheckpointViewModel.class.getName();

    private CheckpointRepository mRepository;
    private LiveData<List<CheckpointItem>> mAllCheckpoints;

    public CheckpointViewModel(@NonNull Application application) {
        super(application);
        mRepository = new CheckpointRepository(application);
        mAllCheckpoints = mRepository.getAllCheckpoints();
    }

    public LiveData<List<CheckpointItem>> getAllCheckpoints() {
        return mAllCheckpoints;
    }

    public LiveData<List<CheckpointItem>> getPendingCheckpoints() {
        return mRepository.getCheckpointsWithSharedStatus(false);
    }

    public void insert(final CheckpointItem checkpointItem) {
        Log.i(TAG, "Saving checkpoint from " + checkpointItem.getTime() + " locally to share later.");
        mRepository.insert(checkpointItem);
    }

    public void delete(final CheckpointItem checkpointItem) {
        Log.i(TAG, "Removing locally saved checkpoint from " + checkpointItem.getTime() + ".");
        mRepository.delete(checkpointItem);
    }

    public void update(final CheckpointItem checkpointItem) {
        Log.i(TAG, "Updating locally save checkpoint from " + checkpointItem.getTime() + ".");
        mRepository.update(checkpointItem);
    }

    public void share(final CheckpointItem checkpointItem, CheckpointRepository.OnShareCheckpointResult listener) {
        Log.i(TAG, "Sharing checkpoint from " + checkpointItem.getTime() + ".");
        mRepository.share(checkpointItem, listener);
    }
}
