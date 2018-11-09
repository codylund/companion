package com.codylund.companion;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.location.Location;
import android.support.annotation.NonNull;

@Entity(tableName = "checkpoints")
public class CheckpointItem {

    @PrimaryKey (autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int mId;

    @NonNull
    @ColumnInfo(name = "time")
    private String mTime;

    @NonNull
    @ColumnInfo(name = "lat")
    private double mLat;

    @NonNull
    @ColumnInfo(name = "lng")
    private double mLng;

    @ColumnInfo(name = "text")
    private String mText;

    @ColumnInfo(name = "pic")
    private String mImagePath;

    @ColumnInfo(name = "shared")
    private boolean mShared;

    public CheckpointItem(final String time, final double lat, final double lng, final String text, final String imagePath) {
        this.mTime = time;
        this.mLat = lat;
        this.mLng = lng;
        this.mText = text;
        this.mImagePath = imagePath;
        this.mShared = false;
    }

    public int getId() {
        return this.mId;
    }

    public String getTime() {
        return this.mTime;
    }

    public double getLat() {
        return this.mLat;
    }

    public double getLng() {
        return this.mLng;
    }

    public String getText() {
        return this.mText;
    }

    public String getImagePath() {
        return this.mImagePath;
    }

    public boolean getShared() {return this.mShared; }

    public void setId(final int id) {
        this.mId = id;
    }

    public void setTime(final String time) {
        this.mTime = time;
    }

    public void setLat(final double lat) {
        this.mLat = lat;
    }

    public void setLng(final double lng) {
        this.mLng = lng;
    }

    public void setText(final String text) {
        this.mText = text;
    }

    public void setImagePath(final String imagePath) {
        this.mImagePath = imagePath;
    }

    public void setShared(final boolean shared) {
        this.mShared = shared;
    }
}
