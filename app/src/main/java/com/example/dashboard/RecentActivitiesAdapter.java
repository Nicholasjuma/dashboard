package com.example.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecentActivitiesAdapter extends RecyclerView.Adapter<RecentActivitiesAdapter.ViewHolder> {

    private List<RecentActivity> recentActivities;

    public RecentActivitiesAdapter(List<RecentActivity> recentActivities) {
        this.recentActivities = recentActivities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recent_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecentActivity activity = recentActivities.get(position);
        holder.tvDescription.setText(activity.getDescription());
        holder.tvTimeAgo.setText(activity.getTimeAgo());
    }

    @Override
    public int getItemCount() {
        return recentActivities.size();
    }

    public void addActivity(RecentActivity activity) {
        recentActivities.add(0, activity);
        notifyItemInserted(0);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription;
        TextView tvTimeAgo;

        ViewHolder(View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTimeAgo = itemView.findViewById(R.id.tvTimeAgo);
        }
    }
}
