package com.example.econavigator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.econavigator.R;
import com.example.econavigator.models.Mission;

import java.util.List;

public class MissionAdapter extends RecyclerView.Adapter<MissionAdapter.ViewHolder> {

    private Context context;
    private List<Mission> missionList;

    public MissionAdapter(Context context, List<Mission> missionList) {
        this.context = context;
        this.missionList = missionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mission, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mission mission = missionList.get(position);

        holder.tvTitle.setText(mission.getTitle());
        holder.tvDescription.setText(mission.getDescription());
        holder.tvReward.setText("+" + mission.getRewardPoints() + " баллов");
        holder.tvProgress.setText(mission.getCurrentCount() + "/" + mission.getTargetCount());
        holder.progressBar.setProgress(mission.getProgressPercentage());

        if (mission.isCompleted()) {
            holder.tvStatus.setText("✅ Выполнено");
            holder.tvStatus.setVisibility(View.VISIBLE);
        } else {
            holder.tvStatus.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return missionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvReward, tvProgress, tvStatus;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_mission_title);
            tvDescription = itemView.findViewById(R.id.tv_mission_description);
            tvReward = itemView.findViewById(R.id.tv_mission_reward);
            tvProgress = itemView.findViewById(R.id.tv_mission_progress);
            tvStatus = itemView.findViewById(R.id.tv_mission_status);
            progressBar = itemView.findViewById(R.id.progress_mission);
        }
    }
}