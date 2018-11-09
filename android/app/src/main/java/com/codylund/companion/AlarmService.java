package com.codylund.companion;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Date;

public class AlarmService extends Service {

    private static final String TAG = AlarmService.class.getName();

    @Override
    public void onCreate() {
        RequestQueueSingleton.instantiate(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Context context = getApplicationContext();
        Log.d(TAG, "Sending ping to server...");
        LatLngHelper.getLatestLatLng(context, new LatLngHelper.IGetLatestLatLngCallback() {
            @Override
            public void returnLatestLatLng(final Location location) {
                if (location != null) {

                    // Instantiate a checkpoint w/ no text or images
                    CheckpointItem item = new CheckpointItem(
                            new Date().toString(),
                            location.getLatitude(),
                            location.getLongitude(),
                            null,
                            null
                    );

                    // Prepare the HTTP request
                    CheckpointRequest ping = new CheckpointRequest(item, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "Ping failed with status code " + error.toString());
                        }
                    }, new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            Log.d(TAG, "Successful ping from " + location.toString());
                        }
                    });

                    // Send the checkpoint request
                    RequestQueueSingleton.sendCheckpointRequest(ping);
                }
            }
        });
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
