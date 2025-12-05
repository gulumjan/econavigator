package com.example.econavigator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.econavigator.R;
import com.example.econavigator.models.FirebaseStudent;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private Context context;
    private List<FirebaseStudent> studentList;

    public LeaderboardAdapter(Context context, List<FirebaseStudent> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseStudent student = studentList.get(position);

        holder.tvPosition.setText(String.valueOf(position + 1));
        holder.tvName.setText(student.getName());
        holder.tvClass.setText(student.getClassName());
        holder.tvPoints.setText(student.getPoints() + " баллов");
        holder.tvLevel.setText(student.getLevelTitle());
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPosition, tvName, tvClass, tvPoints, tvLevel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPosition = itemView.findViewById(R.id.tv_position);
            tvName = itemView.findViewById(R.id.tv_name);
            tvClass = itemView.findViewById(R.id.tv_class);
            tvPoints = itemView.findViewById(R.id.tv_points);
            tvLevel = itemView.findViewById(R.id.tv_level);
        }
    }
}