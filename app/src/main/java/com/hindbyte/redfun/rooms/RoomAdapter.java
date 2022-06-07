package com.hindbyte.redfun.rooms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.hindbyte.redfun.R;
import com.hindbyte.redfun.activity.RoomActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RoomModel> matchList;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    public RoomAdapter(List<RoomModel> matchList, Context mContext) {
        this.matchList = matchList;
        RoomAdapter.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == 1) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_room_item, parent, false);
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
            RoomModel newsModel = matchList.get(position);
            String prize;
            if (newsModel.getPrize().matches("^[0-9]*$")){
                prize = "Prize\n₹ " + newsModel.getPrize();
            } else {
                prize = "Prize\n" + newsModel.getPrize();
            }
            Spannable word = new SpannableString(prize);
            word.setSpan(new ForegroundColorSpan(Color.parseColor("#61FF8D")), 5, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.prize.setText(word);
            String fees;
            if (newsModel.getFees().equals("0")){
                fees = "Entry\nFree";
            } else {
                fees = "Entry\n₹ " + newsModel.getFees();
            }
            word = new SpannableString(fees);
            word.setSpan(new ForegroundColorSpan(Color.parseColor("#61FF8D")), 5, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.fees.setText(word);
            String winners = "";
            if (newsModel.getStatus().equals("CLOSED")) {
                double spots = Double.valueOf(newsModel.getSpots());
                if (spots == 1) {
                    winners = "1";
                } else {
                    winners = String.valueOf(Math.round(spots * 0.40));
                }
                viewHolder.winners.setText("Total teams: " + newsModel.getSpots() + ", Winners: " + winners);
            }
            String finalWinners = winners;
            viewHolder.item.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, RoomActivity.class);
                intent.putExtra("MATCH", newsModel.getMatch());
                intent.putExtra("MATCH_ID", newsModel.getMatchId());
                intent.putExtra("ROOM_ID", newsModel.getId());
                if (newsModel.getStatus().equals("CLOSED")) {
                    intent.putExtra("SPOTS", newsModel.getSpots());
                    intent.putExtra("WINNERS", finalWinners);
                }
                intent.putExtra("STATUS", newsModel.getStatus());
                mContext.startActivity(intent);
            });
            Picasso.get()
                    .load(newsModel.getImage())
                    .into(viewHolder.imageView);
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
        TextView prize;
        ImageView imageView;
        TextView fees;
        TextView winners;

        MyViewHolder(View view) {
            super(view);
            item = view.findViewById(R.id.item);
            prize = view.findViewById(R.id.prize);
            imageView = view.findViewById(R.id.imageView);
            fees = view.findViewById(R.id.fees);
            winners = view.findViewById(R.id.winners);
            winners.setOnClickListener(view1 -> new AlertDialog.Builder(mContext).setMessage("Around 40% teams will be the winner.\nIf 2 teams participated then 1 team will be the winner.\nIf 10 teams participated then 4 teams will be the winner.").create().show());
        }
    }
}