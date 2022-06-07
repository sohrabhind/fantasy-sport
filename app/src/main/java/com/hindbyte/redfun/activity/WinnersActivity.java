package com.hindbyte.redfun.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.hindbyte.redfun.R;
import com.hindbyte.redfun.library.SwipyRefreshLayout;
import com.hindbyte.redfun.library.SwipyRefreshLayoutDirection;
import com.hindbyte.redfun.utils.ToastWindow;
import com.hindbyte.redfun.winner.WinnerAdapter;
import com.hindbyte.redfun.winner.WinnerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WinnersActivity extends AppCompatActivity {

    private WinnerAdapter passAdapter;
    private List<WinnerModel> passModelList = new ArrayList<>();
    private SwipyRefreshLayout swipeRefreshLayout;
    private boolean first;
    WinnerModel passModel;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winners);

        Button toolbar = findViewById(R.id.toolbar);
        toolbar.setText("Recent Winners");
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        passAdapter = new WinnerAdapter(passModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));
        recyclerView.setAdapter(passAdapter);
        recyclerView.setNestedScrollingEnabled(true);
        swipeRefreshLayout.setRefreshing(true);
        getTopFeed();
        swipeRefreshLayout.setOnRefreshListener(direction -> {
            if (direction == SwipyRefreshLayoutDirection.TOP) {
                getTopFeed();
            } else if (direction == SwipyRefreshLayoutDirection.BOTTOM && !passModelList.isEmpty()) {
                getBottomFeed();
            } else {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private void getTopFeed() {
        String tag_string_req = "req_reg";
        StringRequest strReq = new StringRequest(Request.Method.POST, "http://hindbyte.com/redfun/winners/feed.php", response -> {
            swipeRefreshLayout.setRefreshing(false);
            try {
                JSONObject jObj = new JSONObject(response);
                JSONArray jArray = jObj.getJSONArray("item");
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject oneObject = jArray.getJSONObject(i);
                    String id = oneObject.getString("id");
                    String name = oneObject.getString("name");
                    String series = oneObject.getString("series");
                    String amount = oneObject.getString("amount");
                    String points = oneObject.getString("points");
                    passModel = new WinnerModel();
                    passModel.setId(id);
                    passModel.setName(name);
                    passModel.setSeries(series);
                    passModel.setAmount(amount);
                    passModel.setPoints(points);
                    passModel.setViewType(1);
                    if (first) {
                        passModelList.add(passModel);
                    } else {
                        passModelList.add(0, passModel);
                    }
                    passAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            swipeRefreshLayout.setRefreshing(false);
            ToastWindow.makeText(this, "Internet/Server Problem: Please try after some time", 1500);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                first = passModelList.size() <= 0;
                return params;
            }
        };
        strReq.setShouldCache(false);
        com.hindbyte.redfun.utils.Request.addToRequestQueue(this, strReq, tag_string_req);
    }

    private void getBottomFeed() {
        String tag_string_req = "req_reg";
        StringRequest strReq = new StringRequest(Request.Method.POST, "http://hindbyte.com/redfun/winners/feed.php", response -> {
            swipeRefreshLayout.setRefreshing(false);
            try {
                JSONObject jObj = new JSONObject(response);
                JSONArray jArray = jObj.getJSONArray("item");
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject oneObject = jArray.getJSONObject(i);
                    String id = oneObject.getString("id");
                    String name = oneObject.getString("name");
                    String series = oneObject.getString("series");
                    String amount = oneObject.getString("amount");
                    String points = oneObject.getString("points");
                    passModel = new WinnerModel();
                    passModel.setId(id);
                    passModel.setName(name);
                    passModel.setSeries(series);
                    passModel.setAmount(amount);
                    passModel.setPoints(points);
                    passModel.setViewType(1);
                    passModelList.add(passModel);
                    passAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            swipeRefreshLayout.setRefreshing(false);
            ToastWindow.makeText(this, "Internet/Server Problem: Please try after some time", 1500);
        }) {
            @Override
            protected Map<String, String> getParams() {
                return new HashMap<>();
            }
        };
        strReq.setShouldCache(false);
        com.hindbyte.redfun.utils.Request.addToRequestQueue(this, strReq, tag_string_req);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK && onKeyCodeBack();
    }

    private boolean onKeyCodeBack() {
        finish();
        return true;
    }
}