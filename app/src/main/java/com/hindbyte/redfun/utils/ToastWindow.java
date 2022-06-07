package com.hindbyte.redfun.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hindbyte.redfun.R;


public class ToastWindow {

    private final static Handler handlerToast = new Handler(Looper.getMainLooper());

    @SuppressLint("ClickableViewAccessibility")
    public static void makeText(Context context, String text, int duration) {
        try {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            @SuppressLint("InflateParams") View customMenu = inflater.inflate(R.layout.toast_layout, null);
            PopupWindow pw = new PopupWindow(context);
            TextView tv = customMenu.findViewById(R.id.toast_text);
            tv.setText(text);
            pw.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            pw.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            pw.setOutsideTouchable(false);
            pw.setTouchable(false);
            pw.setFocusable(false);
            pw.setContentView(customMenu);
            pw.showAtLocation(customMenu, Gravity.BOTTOM, 0, context.getResources().getDimensionPixelSize(R.dimen.dimen_menu));
            handlerToast.postDelayed(pw::dismiss, duration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}