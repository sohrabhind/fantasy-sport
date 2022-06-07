package com.hindbyte.redfun.passbook;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

import com.hindbyte.redfun.R;

public class PassbookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PassbookModel> passModelList;

    public PassbookAdapter(List<PassbookModel> passModelList) {
        this.passModelList = passModelList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == 1) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_passbook_item, parent, false);
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
            PassbookModel newsModel = passModelList.get(position);
            String method = newsModel.getMethod();
            if (method.contains("RAZORPAY_")) {
                method = "Added To Wallet\nTransaction ID: " + method.replace("RAZORPAY_", "");
            } else if (method.contains("WITHDRAW_")) {
                method = "Withdraw From Wallet\nWithdraw ID: " + method.replace("WITHDRAW_", "");
            } else if (method.contains("FOR_M_")) {
                method = "Deduced For\nMatch ID: " + method.replace("FOR_M_", "").replace("R_", " Room ID: ").replace("T_", " Team ID: ");
            } else if (method.contains("BY_M_")) {
                method = "Won For\nMatch ID: " + method.replace("BY_M_", "").replace("R_", " Room ID: ").replace("T_", " Team ID: ");
            } else if (method.contains("REFUND_M_")) {
                method = "Refunded For\nMatch ID: " + method.replace("REFUND_M_", "").replace("R_", " Room ID: ").replace("T_", " Team ID: ");
            }
            @SuppressLint("SimpleDateFormat") SimpleDateFormat displayFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat newFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
            String date = null;
            try {
                date = newFormat.format(Objects.requireNonNull(displayFormat.parse(newsModel.getTime())));
                date = date.toUpperCase();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            viewHolder.title.setText(newsModel.getAmount() + " ₹ " + method);
            viewHolder.date.setText(date);
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
        TextView title;
        TextView date;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            date = view.findViewById(R.id.date);
        }
    }
}