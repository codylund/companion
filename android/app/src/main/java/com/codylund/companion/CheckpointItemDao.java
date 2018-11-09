package com.codylund.companion;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CheckpointItemDao {
    @Query("SELECT * FROM checkpoints")
    LiveData<List<CheckpointItem>> getAll();

    @Query("SELECT * FROM checkpoints WHERE shared = :shared")
    LiveData<List<CheckpointItem>> getAllWithSharedStatus(boolean shared);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CheckpointItem checkpointItem);

    @Update
    void update(CheckpointItem checkpointItem);

    @Delete
    void delete(CheckpointItem checkpointItem);
}
