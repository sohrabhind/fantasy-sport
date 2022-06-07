package com.hindbyte.redfun.winner;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hindbyte.redfun.R;

import java.util.List;

public class WinnerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<WinnerModel> matchList;

    public WinnerAdapter(List<WinnerModel> matchList) {
        this.matchList = matchList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == 1) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_winner_item, parent, false);
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
            WinnerModel newsModel = matchList.get(position);
            viewHolder.name.setText(newsModel.getName()+ "\nWON ₹"+newsModel.getAmount());
            viewHolder.series.setText(newsModel.getSeries());
            viewHolder.points.setText("POINTS : "+newsModel.getPoints());
        }
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return matchList.get(position).getViewType();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item;
        TextView name;
        TextView series;
        TextView points;

        MyViewHolder(View view) {
            super(view);
            item = view.findViewById(R.id.item);
            name = view.findViewById(R.id.name);
            series = view.findViewById(R.id.series);
            points = view.findViewById(R.id.points);
        }
    }
}