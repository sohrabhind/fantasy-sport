package com.hindbyte.redfun.player;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.hindbyte.redfun.R;

public class PlayerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PlayerModel> playerModelList;

    public PlayerAdapter(List<PlayerModel> playerModelList) {
        this.playerModelList = playerModelList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == 1) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_player_item, parent, false);
            viewHolder = new MyViewHolder(v);
        }
        assert viewHolder != null;
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder.getItemViewType() == 1) {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            PlayerModel newsModel = playerModelList.get(position);
            viewHolder.event.setText(newsModel.getEvent());
            viewHolder.actual.setText(newsModel.getActual());
            viewHolder.points.setText(newsModel.getPoints());
        }
    }

    @Override
    public int getItemCount() {
        return playerModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return playerModelList.get(position).getViewType();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item;
        TextView event;
        TextView actual;
        TextView points;

        MyViewHolder(View view) {
            super(view);
            event = view.findViewById(R.id.event);
            actual = view.findViewById(R.id.actual);
            points = view.findViewById(R.id.points);
        }
    }
}