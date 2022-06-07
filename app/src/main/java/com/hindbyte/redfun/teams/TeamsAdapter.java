package com.hindbyte.redfun.teams;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import com.hindbyte.redfun.R;
import com.hindbyte.redfun.activity.EditMyTeamActivity;
import com.hindbyte.redfun.activity.TeamPointsActivity;

public class TeamsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TeamsModel> passModelList;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static String match;
    private static String match_id;

    public TeamsAdapter(List<TeamsModel> passModelList, Context mContext, String match, String match_id) {
        this.passModelList = passModelList;
        TeamsAdapter.mContext = mContext;
        TeamsAdapter.match = match;
        TeamsAdapter.match_id = match_id;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch(viewType){
            case 1:{
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_team_item, parent, false);
                viewHolder = new MyViewHolder(v);
                break;
            }
            case 2:{
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_my_team_item, parent, false);
                viewHolder = new MyViewHolder2(v);
                break;
            }
        }
        assert viewHolder != null;
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        switch(holder.getItemViewType()){
            case 1:{
                MyViewHolder viewHolder = (MyViewHolder) holder;
                TeamsModel newsModel = passModelList.get(position);
                viewHolder.nameView.setText(newsModel.getName());
                viewHolder.pointsView.setText("#"+newsModel.getPoints());
                viewHolder.rankView.setText("#"+newsModel.getRank());
                viewHolder.winningView.setText(newsModel.getAmount());
                if (newsModel.getAmount().toLowerCase().contains("you")) {
                    viewHolder.item.setBackgroundResource(R.drawable.item_back_my_list);
                    if (newsModel.getAmount().toLowerCase().contains("not") || newsModel.getAmount().toLowerCase().contains("lost")) {
                        viewHolder.winningView.setTextColor(Color.parseColor("#DE392D"));
                    } else {
                        viewHolder.winningView.setTextColor(Color.parseColor("#61FF8D"));
                    }
                } else {
                    viewHolder.item.setBackgroundResource(R.drawable.item_back_list);
                    viewHolder.winningView.setTextColor(Color.parseColor("#FFEB3B"));
                }
                viewHolder.item.setOnClickListener(v -> {
                    Intent intent = new Intent(mContext, TeamPointsActivity.class);
                    intent.putExtra("MATCH", match);
                    intent.putExtra("MATCH_ID", match_id);
                    intent.putExtra("ID", newsModel.getId());
                    mContext.startActivity(intent);
                });
                break;
            }
            case 2:{
                MyViewHolder2 viewHolder = (MyViewHolder2) holder;
                TeamsModel newsModel = passModelList.get(position);
                viewHolder.nameView.setText(newsModel.getName());
                viewHolder.pointsView.setText(newsModel.getPoints());
                viewHolder.rankView.setText("#"+newsModel.getRank());
                viewHolder.winningView.setText(newsModel.getAmount());
                if (newsModel.getAmount().toLowerCase().contains("not") || newsModel.getAmount().toLowerCase().contains("lost")) {
                    viewHolder.winningView.setTextColor(Color.parseColor("#DE392D"));
                } else {
                    viewHolder.winningView.setTextColor(Color.parseColor("#61FF8D"));
                }
                viewHolder.item.setOnClickListener(v -> {
                    Intent intent = new Intent(mContext, TeamPointsActivity.class);
                    intent.putExtra("MATCH", match);
                    intent.putExtra("MATCH_ID", match_id);
                    intent.putExtra("ID", newsModel.getId());
                    mContext.startActivity(intent);
                });
                viewHolder.editMyTeam.setOnClickListener(v -> {
                    Intent intent = new Intent(mContext, EditMyTeamActivity.class);
                    intent.putExtra("MATCH", match);
                    intent.putExtra("MATCH_ID", match_id);
                    intent.putExtra("ID", newsModel.getId());
                    mContext.startActivity(intent);
                });
                viewHolder.viewPoints.setOnClickListener(v -> {
                    Intent intent = new Intent(mContext, TeamPointsActivity.class);
                    intent.putExtra("MATCH", match);
                    intent.putExtra("MATCH_ID", match_id);
                    intent.putExtra("ID", newsModel.getId());
                    mContext.startActivity(intent);
                });
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return passModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return passModelList.get(position).getViewType();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item;
        TextView nameView;
        TextView pointsView;
        TextView rankView;
        TextView winningView;

        MyViewHolder(View view) {
            super(view);
            item = view.findViewById(R.id.item);
            nameView = view.findViewById(R.id.nameView);
            pointsView = view.findViewById(R.id.pointsView);
            rankView = view.findViewById(R.id.rankView);
            winningView = view.findViewById(R.id.winningView);
        }
    }

    public class MyViewHolder2 extends RecyclerView.ViewHolder {
        LinearLayout item;
        TextView nameView;
        TextView editMyTeam;
        TextView pointsView;
        TextView rankView;
        TextView winningView;
        TextView viewPoints;

        MyViewHolder2(View view) {
            super(view);
            item = view.findViewById(R.id.item);
            nameView = view.findViewById(R.id.nameView);
            editMyTeam = view.findViewById(R.id.editMyTeam);
            pointsView = view.findViewById(R.id.pointsView);
            rankView = view.findViewById(R.id.rankView);
            winningView = view.findViewById(R.id.winningView);
            viewPoints = view.findViewById(R.id.viewPoints);
        }
    }
}