package com.example.dashboard;

import android.os.Parcel;
import android.os.Parcelable;

public class RecentActivity implements Parcelable {
    private String description;
    private String timeAgo;

    public RecentActivity(String description, String timeAgo) {
        this.description = description != null ? description : "No Description";
        this.timeAgo = timeAgo != null ? timeAgo : "Unknown time";
    }

    public String getDescription() {
        return description;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    @Override
    public String toString() {
        return "RecentActivity{" +
                "description='" + description + '\'' +
                ", timeAgo='" + timeAgo + '\'' +
                '}';
    }

    protected RecentActivity(Parcel in) {
        description = in.readString();
        timeAgo = in.readString();
    }

    public static final Creator<RecentActivity> CREATOR = new Creator<RecentActivity>() {
        @Override
        public RecentActivity createFromParcel(Parcel in) {
            return new RecentActivity(in);
        }

        @Override
        public RecentActivity[] newArray(int size) {
            return new RecentActivity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(description);
        parcel.writeString(timeAgo);
    }
}
