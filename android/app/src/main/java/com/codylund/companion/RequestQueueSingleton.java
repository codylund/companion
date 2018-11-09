package com.codylund.companion;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQueueSingleton {

    private static RequestQueue mRequestQueue;

    public static void instantiate(final Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
            mRequestQueue.start();
        }
    }

    public static void sendCheckpointRequest(final CheckpointRequest checkpointRequest) {
        if (mRequestQueue == null) {
            throw new IllegalStateException("RequestQueue singleton has not been instantiated.");
        }
        mRequestQueue.add(checkpointRequest);
    }

}
