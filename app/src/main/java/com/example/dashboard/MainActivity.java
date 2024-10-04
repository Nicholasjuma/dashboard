package com.example.dashboard;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView workTasksTextView;
    private TextView personalTasksTextView;
    private TextView completionPercentageTextView;
    private ProgressBar progressBar;
    private FloatingActionButton fabAddTask;

    private ArrayList<Task> tasks;
    private RecyclerView recyclerViewRecentActivities;
    private RecentActivitiesAdapter adapter;
    private List<RecentActivity> recentActivities;

    private int totalTasks = 0;
    private int completedTasks = 0;
    private int progressPercentage = 0;

    private boolean isTaskBeingAdded = false; // Flag to prevent multiple clicks

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        tasks = new ArrayList<>();
        recentActivities = new ArrayList<>();

        if (savedInstanceState != null) {
            tasks = (ArrayList<Task>) savedInstanceState.getSerializable("tasks");
            recentActivities = savedInstanceState.getParcelableArrayList("recentActivities");
            progressPercentage = savedInstanceState.getInt("progressPercentage", 0);
        }

        initializeUIElements();
        setInitialTaskCounts();
        updateProgressBar();
        setupButtonListeners();
        setupRecyclerView();
    }

    private void initializeUIElements() {
        workTasksTextView = findViewById(R.id.tvWorkTasks);
        personalTasksTextView = findViewById(R.id.tvPersonalTasks);
        progressBar = findViewById(R.id.progressBar);
        fabAddTask = findViewById(R.id.fabAddTask);
        completionPercentageTextView = findViewById(R.id.tvCompletionPercentage);
        recyclerViewRecentActivities = findViewById(R.id.recyclerViewRecentActivities);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> refreshTasks());
    }

    private void refreshTasks() {
        // Clear recent activities and notify adapter
        recentActivities.clear();
        adapter.notifyDataSetChanged();

        // Clear tasks and reset progress
        tasks.clear();
        totalTasks = 0;
        completedTasks = 0;
        progressPercentage = 0;

        // Update UI elements
        updateTaskCounts();
        updateProgressBar();

        // Stop the refreshing animation
        swipeRefreshLayout.setRefreshing(false);

        Toast.makeText(MainActivity.this, "Tasks and Recent Activities Refreshed", Toast.LENGTH_SHORT).show();
    }

    private void setupRecyclerView() {
        adapter = new RecentActivitiesAdapter(recentActivities);
        recyclerViewRecentActivities.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRecentActivities.setAdapter(adapter);
    }

    private void setInitialTaskCounts() {
        updateTaskCounts();
    }

    private void updateTaskCounts() {
        int workTasksCount = 0;
        int personalTasksCount = 0;
        for (Task task : tasks) {
            if (task.getName().startsWith("New Work Task")) {
                workTasksCount++;
            } else if (task.getName().startsWith("New Personal Task")) {
                personalTasksCount++;
            }
        }
        workTasksTextView.setText(workTasksCount + " tasks");
        personalTasksTextView.setText(personalTasksCount + " tasks");
    }

    private void updateProgressBar() {
        progressPercentage = Math.min(progressPercentage, 100);
        progressBar.setProgress(progressPercentage);
        completionPercentageTextView.setText(progressPercentage + "%");
    }

    private void setupButtonListeners() {
        fabAddTask.setOnClickListener(v -> addNewTask());
    }

    private void addNewTask() {
        if (isTaskBeingAdded) return;
        isTaskBeingAdded = true;

        if (tasks == null) {
            tasks = new ArrayList<>();
        }

        int workTaskCount = 0;
        int personalTaskCount = 0;

        for (Task task : tasks) {
            if (task.getName().startsWith("New Work Task")) {
                workTaskCount++;
            } else if (task.getName().startsWith("New Personal Task")) {
                personalTaskCount++;
            }
        }

        boolean workTaskAdded = false;
        boolean personalTaskAdded = false;

        if (workTaskCount < 2) {
            Task newWorkTask = new Task("New Work Task " + (workTaskCount + 1));
            tasks.add(newWorkTask);
            String timeAgoWork = newWorkTask.getTimeAgo();
            addRecentActivity(newWorkTask.getName() + " added " + timeAgoWork);
            workTaskAdded = true;
            progressPercentage++;
        }

        if (personalTaskCount < 2) {
            Task newPersonalTask = new Task("New Personal Task " + (personalTaskCount + 1));
            tasks.add(newPersonalTask);
            String timeAgoPersonal = newPersonalTask.getTimeAgo();
            addRecentActivity(newPersonalTask.getName() + " added " + timeAgoPersonal);
            personalTaskAdded = true;
            progressPercentage++;
        }

        if (workTaskAdded || personalTaskAdded) {
            String message = "New Tasks Added: ";
            if (workTaskAdded) {
                message += "New Work Task " + (workTaskCount + 1);
            }
            if (personalTaskAdded) {
                message += (workTaskAdded ? " and " : "") + "New Personal Task " + (personalTaskCount + 1);
            }
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

            animateFAB();
        } else {
            Toast.makeText(MainActivity.this, "Maximum tasks reached!", Toast.LENGTH_SHORT).show();
        }

        updateTaskCounts();
        updateProgressBar();

        isTaskBeingAdded = false;
    }

    private void animateFAB() {
        fabAddTask.setEnabled(false);

        fabAddTask.animate()
                .rotation(fabAddTask.getRotation() + 360)
                .setDuration(1000)
                .withEndAction(() -> fabAddTask.setEnabled(true))
                .start();

        int colorFrom = ContextCompat.getColor(this, R.color.colorPrimary);
        int colorTo = ContextCompat.getColor(this, R.color.colorAccent);

        ObjectAnimator colorAnimation = ObjectAnimator.ofArgb(fabAddTask, "backgroundColor", colorFrom, colorTo);
        colorAnimation.setDuration(1000);
        colorAnimation.setEvaluator(new ArgbEvaluator());
        colorAnimation.setRepeatCount(ValueAnimator.REVERSE);
        colorAnimation.start();
    }

    private void completeTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.get(index).completeTask();
            addRecentActivity("Completed task: " + tasks.get(index).getName());

            completedTasks++;
            Toast.makeText(MainActivity.this, "Task Completed", Toast.LENGTH_SHORT).show();

            updateProgressBar();
        } else {
            Toast.makeText(MainActivity.this, "Invalid Task Index", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearTasks() {
        tasks.clear();
        recentActivities.clear();
        progressPercentage = 0;

        updateTaskCounts();
        updateProgressBar();
        adapter.notifyDataSetChanged();
        Toast.makeText(MainActivity.this, "All Tasks and Recent Activities Cleared", Toast.LENGTH_SHORT).show();
    }

    private void addRecentActivity(String description) {
        RecentActivity activity = new RecentActivity(description, "just now");
        recentActivities.add(0, activity);
        adapter.addActivity(activity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            openSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tasks", tasks);
        outState.putParcelableArrayList("recentActivities", new ArrayList<>(recentActivities));
        outState.putInt("progressPercentage", progressPercentage);
    }
}
