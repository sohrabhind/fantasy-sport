package com.hindbyte.redfun.utils;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Request {

    private static RequestQueue mRequestQueue;

    public static <T> void addToRequestQueue(Context context, com.android.volley.Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? "TAG" : tag);
        getRequestQueue(context).add(req);
    }

    private static RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }

}
