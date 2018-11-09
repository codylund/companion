package com.codylund.companion;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = {CheckpointItem.class}, version = 1)
public abstract class CheckpointDatabase extends RoomDatabase {
    public abstract CheckpointItemDao checkpointItemDao();

    private static CheckpointDatabase INSTANCE;

    static CheckpointDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CheckpointDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            CheckpointDatabase.class,
                            "checkpoints").build();
                }
            }
        }
        return INSTANCE;
    }
}
