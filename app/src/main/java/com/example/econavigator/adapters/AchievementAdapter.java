package com.example.econavigator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.econavigator.R;
import com.example.econavigator.models.FirebaseAchievement;

import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.ViewHolder> {

    private Context context;
    private List<FirebaseAchievement> achievementList;

    public AchievementAdapter(Context context, List<FirebaseAchievement> achievementList) {
        this.context = context;
        this.achievementList = achievementList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_achievement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseAchievement achievement = achievementList.get(position);

        holder.tvTitle.setText(achievement.getTitle());

        // Получаем ресурс иконки
        int iconResource = getIconResource(achievement.getIconName());
        holder.ivIcon.setImageResource(iconResource);

        if (achievement.isUnlocked()) {
            holder.itemView.setAlpha(1.0f);
        } else {
            holder.itemView.setAlpha(0.5f);
        }
    }

    @Override
    public int getItemCount() {
        return achievementList.size();
    }

    // Метод для получения ресурса иконки по имени
    private int getIconResource(String iconName) {
        if (iconName == null) {
            return R.drawable.ic_bottle; // default icon
        }

        switch (iconName) {
            case "ic_bottle":
                return R.drawable.ic_bottle;
            case "ic_paper":
                return R.drawable.ic_paper;
            case "ic_plastic":
                return R.drawable.ic_plastic;
            case "ic_glass":
                return R.drawable.ic_glass;
            default:
                return R.drawable.ic_bottle;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_achievement_icon);
            tvTitle = itemView.findViewById(R.id.tv_achievement_title);
        }
    }
}