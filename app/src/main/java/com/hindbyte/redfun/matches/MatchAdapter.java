package com.hindbyte.redfun.matches;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hindbyte.redfun.R;
import com.hindbyte.redfun.activity.RoomsActivity;
import com.hindbyte.redfun.utils.ToastWindow;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class MatchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MatchModel> matchList;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    public static Handler handlerMatch = new Handler(Looper.getMainLooper());

    public MatchAdapter(List<MatchModel> matchList, Context mContext) {
        this.matchList = matchList;
        MatchAdapter.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == 1 || viewType == 2) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_match_item, parent, false);
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
            MatchModel newsModel = matchList.get(position);
            viewHolder.series.setText(newsModel.getSeries());
            String status = "";
            viewHolder.status.setTextColor(Color.parseColor("#4d4d4d"));
            switch (newsModel.getStatus()) {
                case "open":
                    status = "OPEN";
                    viewHolder.status.setTextColor(Color.parseColor("#61FF8D"));
                    viewHolder.item.setOnClickListener(v -> {
                        Intent intent = new Intent(mContext, RoomsActivity.class);
                        intent.putExtra("MATCH", newsModel.getTeamA() + " vs " + newsModel.getTeamB());
                        intent.putExtra("MATCH_ID", newsModel.getId());
                        intent.putExtra("STATUS", "OPEN");
                        mContext.startActivity(intent);
                    });
                    break;
                case "upcoming":
                    status = "UPCOMING";
                    viewHolder.item.setOnClickListener(v -> {
                        ToastWindow.makeText(mContext, "Team entry is not open", 2000);
                    });
                    break;
            }
            viewHolder.status.setText(status);
            viewHolder.title.setText(newsModel.getTeamA() + " vs " + newsModel.getTeamB());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat oldFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    String difference = "";
                    try {
                        Calendar current = Calendar.getInstance(TimeZone.getTimeZone("IST"));
                        String current_date = oldFormat.format(current.getTime());
                        Date scheduledDate = oldFormat.parse(newsModel.getTime());
                        Date currentDate = oldFormat.parse(current_date);
                        long diffInMilli = 0;
                        if (scheduledDate != null && currentDate != null) {
                            diffInMilli = scheduledDate.getTime() - currentDate.getTime();
                        }
                        long diffSeconds = diffInMilli / 1000 % 60;
                        long diffMinutes = diffInMilli / (60 * 1000) % 60;
                        long diffHours = diffInMilli / (60 * 60 * 1000) % 24;
                        long diffDays = diffInMilli / (24 * 60 * 60 * 1000);
                        if (diffDays != 0) {
                            difference = diffDays + " days left";
                        } else if (diffHours != 0) {
                            difference = diffHours + "h "+diffMinutes + "m left";
                        } else if (diffMinutes != 0) {
                            difference = diffMinutes + "m "+diffSeconds + "s left";
                        } else if (diffSeconds != 0) {
                            difference = diffSeconds + " seconds left";
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    viewHolder.date.setText(difference);
                    handlerMatch.postDelayed(this, 1000);
                }
            };
            handlerMatch.post(runnable);
            Picasso.get()
                    .load(newsModel.getFlagA())
                    .into(viewHolder.flagA);
            Picasso.get()
                    .load(newsModel.getFlagB())
                    .into(viewHolder.flagB);
        } else if (holder.getItemViewType() == 2) {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            MatchModel newsModel = matchList.get(position);
            viewHolder.series.setText(newsModel.getSeries());
            viewHolder.title.setText(newsModel.getTeamA() + " vs " + newsModel.getTeamB());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat displayFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat newFormat = new SimpleDateFormat("MMM dd, yyyy");
            String date = "";
            try {
                date = newFormat.format(Objects.requireNonNull(displayFormat.parse(newsModel.getTime())));
                date = date.toUpperCase();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String status = "";
            viewHolder.status.setTextColor(Color.parseColor("#4d4d4d"));
            switch (newsModel.getStatus()) {
                case "completed":
                    status = "RESULT DECLARED";
                    viewHolder.status.setTextColor(Color.parseColor("#61FF8D"));
                    break;
                case "open":
                    status = "OPEN";
                    break;
                case "counting":
                    status = "LIVE";
                    viewHolder.status.setTextColor(Color.parseColor("#61FF8D"));
                    break;
                case "closed":
                    status = "LIVE";
                    viewHolder.status.setTextColor(Color.parseColor("#61FF8D"));
                    break;
                case "cancelled":
                    status = "CANCELLED";
                    viewHolder.status.setTextColor(Color.parseColor("#DE392D"));
                    break;
                case "refunded":
                    status = "REFUNDED";
                    viewHolder.status.setTextColor(Color.parseColor("#DE392D"));
                    break;
            }
            viewHolder.status.setText(status);
            viewHolder.date.setText(date);
            viewHolder.item.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, RoomsActivity.class);
                intent.putExtra("MATCH", newsModel.getTeamA() + " vs " + newsModel.getTeamB());
                intent.putExtra("MATCH_ID", newsModel.getId());
                intent.putExtra("STATUS", "CLOSED");
                mContext.startActivity(intent);
            });
            Picasso.get()
                    .load(newsModel.getFlagA())
                    .into(viewHolder.flagA);
            Picasso.get()
                    .load(newsModel.getFlagB())
                    .into(viewHolder.flagB);
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
        TextView series;
        TextView status;
        TextView title;
        TextView date;
        ImageView flagA;
        ImageView flagB;

        MyViewHolder(View view) {
            super(view);
            item = view.findViewById(R.id.item);
            series = view.findViewById(R.id.series);
            status = view.findViewById(R.id.status);
            title = view.findViewById(R.id.title);
            date = view.findViewById(R.id.date);
            flagA = view.findViewById(R.id.flagA);
            flagB = view.findViewById(R.id.flagB);
            date.setTextColor(Color.parseColor("#61FF8D"));
        }
    }
}