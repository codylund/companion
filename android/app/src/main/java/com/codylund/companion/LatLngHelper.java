package com.codylund.companion;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class LatLngHelper {

    private static final String TAG = LatLngHelper.class.getName();

    public static boolean checkGPSPermission(final Context context) {
        return context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static void getLatestLatLng(final Context context, final IGetLatestLatLngCallback callback) {

        // check that the user enabled GPS permissions
        if (checkGPSPermission(context)) {

            // see if we can get the last known location
            final FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Log.d(TAG, "Successfully retrieved last known location.");
                            callback.returnLatestLatLng(location);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Failed to retrieve last known location.");
                            callback.returnLatestLatLng(null);
                        }
                    });

        } else {
            Log.e(TAG, "Location permissions not enabled.");
            callback.returnLatestLatLng(null);
        }
    }

    public interface IGetLatestLatLngCallback {
        void returnLatestLatLng(Location location);
    }

}
