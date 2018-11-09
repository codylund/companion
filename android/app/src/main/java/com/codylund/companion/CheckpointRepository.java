package com.codylund.companion;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.location.Location;
import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.File;
import java.util.List;

public class CheckpointRepository {

    private CheckpointItemDao mCheckpointItemDao;
    private DaoTaskFactory mDaoTaskFactory;
    private LiveData<List<CheckpointItem>> mCheckpointItems;
    private RequestQueue mRequestQueue;

    public CheckpointRepository(Application application) {
        RequestQueueSingleton.instantiate(application);
        CheckpointDatabase db = CheckpointDatabase.getDatabase(application);
        mCheckpointItemDao = db.checkpointItemDao();
        mCheckpointItems = mCheckpointItemDao.getAll();
        mDaoTaskFactory = new DaoTaskFactory(mCheckpointItemDao);
    }

    public LiveData<List<CheckpointItem>> getAllCheckpoints() {
        return mCheckpointItems;
    }

    public LiveData<List<CheckpointItem>> getCheckpointsWithSharedStatus(boolean status) {
        return mCheckpointItemDao.getAllWithSharedStatus(status);
    }

    public void insert(final CheckpointItem checkpointItem) {
        mDaoTaskFactory.getTask(DaoTaskFactory.INSERT).execute(checkpointItem);
    }

    public void delete(final CheckpointItem checkpointItem) {
        mDaoTaskFactory.getTask(DaoTaskFactory.DELETE).execute(checkpointItem);
    }

    public void update(final CheckpointItem checkpointItem) {
        mDaoTaskFactory.getTask(DaoTaskFactory.UPDATE).execute(checkpointItem);
    }

    public void share(final CheckpointItem checkpointItem, final OnShareCheckpointResult listener) {
        // Prepare the request for the clicked object
        CheckpointRequest checkpointRequest = new CheckpointRequest(checkpointItem, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Something went wrong; we can't delete this local checkpoint yet.
                listener.onError();
            }
        }, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                listener.onSuccess();
                checkpointItem.setShared(true);
                CheckpointRepository.this.update(checkpointItem);
            }
        });

        checkpointRequest.setTag("MultiRequest");

        // Send the request
        RequestQueueSingleton.sendCheckpointRequest(checkpointRequest);
    }

    public interface OnShareCheckpointResult {
        void onError();
        void onSuccess();
    }

    private class DaoTaskFactory {

        public static final int INSERT = 0;
        public static final int DELETE = 1;
        public static final int UPDATE = 2;

        private final CheckpointItemDao mCheckpointItemDao;

        DaoTaskFactory(CheckpointItemDao dao) {
            mCheckpointItemDao = dao;
        }

        public DaoAsyncTask getTask(final int type) {
            DaoAsyncTask task = null;
            switch (type) {
                case INSERT:
                    task = new InsertAsyncTask(this.mCheckpointItemDao);
                    break;
                case DELETE:
                    task = new DeleteAsyncTask(this.mCheckpointItemDao);
                    break;

                case UPDATE:
                    task = new UpdateAsyncTask(this.mCheckpointItemDao);
                    break;
            }
            return task;
        }
    }

    private static abstract class DaoAsyncTask extends AsyncTask<CheckpointItem, Void, Void> {
        public CheckpointItemDao mAsyncTaskDao;
        DaoAsyncTask(CheckpointItemDao dao) { mAsyncTaskDao = dao; }

        @Override
        protected Void doInBackground(CheckpointItem ... checkpointItems) {
            this.command(checkpointItems[0]);
            return null;
        }

        public abstract void command(CheckpointItem checkpointItem);
    }

    private static class InsertAsyncTask extends DaoAsyncTask {

        InsertAsyncTask(CheckpointItemDao dao) {
            super(dao);
        }

        @Override
        public void command(CheckpointItem checkpointItem) {
            mAsyncTaskDao.insert(checkpointItem);
        }

    }

    private static class DeleteAsyncTask extends DaoAsyncTask {

        DeleteAsyncTask(CheckpointItemDao dao) {
            super(dao);
        }

        @Override
        public void command(CheckpointItem checkpointItem) {
            mAsyncTaskDao.delete(checkpointItem);
        }

    }

    private static class UpdateAsyncTask extends DaoAsyncTask {

        UpdateAsyncTask(CheckpointItemDao dao) {
            super(dao);
        }

        @Override
        public void command(CheckpointItem checkpointItem) {
            mAsyncTaskDao.update(checkpointItem);
        }
    }
}
