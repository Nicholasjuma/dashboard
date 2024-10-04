package com.example.dashboard;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRecentActivities;
    private RecentActivitiesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        recyclerViewRecentActivities = findViewById(R.id.recyclerViewRecentActivities);
        recyclerViewRecentActivities.setLayoutManager(new LinearLayoutManager(this));

        List<RecentActivity> recentActivities = new ArrayList<>();
        recentActivities.add(new RecentActivity("Completed Task 1", "5 minutes ago"));
        recentActivities.add(new RecentActivity("Added Task 2", "10 minutes ago"));
        recentActivities.add(new RecentActivity("Marked Task 3 as In Progress", "20 minutes ago"));

        adapter = new RecentActivitiesAdapter(recentActivities);
        recyclerViewRecentActivities.setAdapter(adapter);
    }
}