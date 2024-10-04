package com.example.dashboard;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class Task implements Serializable {
    private String name;
    private boolean isCompleted;
    private long createdAt; //

    public Task(String name) {
        this.name = name;
        this.isCompleted = false;
        this.createdAt = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void completeTask() {
        isCompleted = true;
    }

    public String getTimeAgo() {
        long currentTime = System.currentTimeMillis();
        long timeDiff = currentTime - createdAt;

        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff);
        long hours = TimeUnit.MILLISECONDS.toHours(timeDiff);
        long days = TimeUnit.MILLISECONDS.toDays(timeDiff);

        if (days > 0) {
            return days + (days == 1 ? " day ago" : " days ago");
        } else if (hours > 0) {
            return hours + (hours == 1 ? " hour ago" : " hours ago");
        } else {
            return minutes + (minutes == 1 ? " minute ago" : " minutes ago");
        }
    }
}
