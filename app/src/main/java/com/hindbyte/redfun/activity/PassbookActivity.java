package com.hindbyte.redfun.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.hindbyte.redfun.R;
import com.hindbyte.redfun.library.SwipyRefreshLayout;
import com.hindbyte.redfun.library.SwipyRefreshLayoutDirection;
import com.hindbyte.redfun.passbook.PassbookAdapter;
import com.hindbyte.redfun.passbook.PassbookModel;
import com.hindbyte.redfun.utils.SQLiteHandler;
import com.hindbyte.redfun.utils.ToastWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PassbookActivity extends AppCompatActivity {

    private PassbookAdapter passAdapter;
    private List<PassbookModel> passModelList = new ArrayList<>();
    private SwipyRefreshLayout swipeRefreshLayout;
    private boolean first;
    PassbookModel passModel;
    SQLiteHandler db;
    String phone;
    String pass;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passbook);

        db = new SQLiteHandler(PassbookActivity.this);
        HashMap<String, String> user = db.getUserDetails();
        phone = user.get("phone");
        pass = user.get("pass");
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        passAdapter = new PassbookAdapter(passModelList);
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
        StringRequest strReq = new StringRequest(Request.Method.POST, "http://hindbyte.com/redfun/account/passbook.php", response -> {
            swipeRefreshLayout.setRefreshing(false);
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                if (!error) {
                    JSONArray jArray = jObj.getJSONArray("item");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject oneObject = jArray.getJSONObject(i);
                        String id = oneObject.getString("id");
                        String amount = oneObject.getString("amount");
                        String time = oneObject.getString("time");
                        String method = oneObject.getString("method");
                        passModel = new PassbookModel();
                        passModel.setId(id);
                        passModel.setAmount(amount);
                        passModel.setTime(time);
                        passModel.setMethod(method);
                        passModel.setViewType(1);
                        if (first) {
                            passModelList.add(passModel);
                        } else {
                            passModelList.add(0, passModel);
                        }
                        passAdapter.notifyDataSetChanged();
                    }
                }  else {
                    String errorMsg = jObj.getString("error_msg");
                    displayToast(errorMsg, 2000);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            swipeRefreshLayout.setRefreshing(false);
            displayToast("Internet/Server Problem: Please try after some time", 1500);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", phone);
                params.put("pass", pass);
                if (passModelList.size() > 0) {
                    first = false;
                    params.put("first_id", passModelList.get(0).getId());
                } else {
                    first = true;
                }
                return params;
            }
        };
        strReq.setShouldCache(false);
        com.hindbyte.redfun.utils.Request.addToRequestQueue(this, strReq, tag_string_req);
    }

    private void getBottomFeed() {
        String tag_string_req = "req_reg";
        StringRequest strReq = new StringRequest(Request.Method.POST, "http://hindbyte.com/redfun/account/passbook.php", response -> {
            swipeRefreshLayout.setRefreshing(false);
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                if (!error) {
                    JSONArray jArray = jObj.getJSONArray("item");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject oneObject = jArray.getJSONObject(i);
                        String id = oneObject.getString("id");
                        String amount = oneObject.getString("amount");
                        String time = oneObject.getString("time");
                        String method = oneObject.getString("method");
                        passModel = new PassbookModel();
                        passModel.setId(id);
                        passModel.setAmount(amount);
                        passModel.setTime(time);
                        passModel.setMethod(method);
                        passModel.setViewType(1);
                        passModelList.add(passModel);
                        passAdapter.notifyDataSetChanged();
                    }
                }  else {
                    String errorMsg = jObj.getString("error_msg");
                    displayToast(errorMsg, 2000);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            swipeRefreshLayout.setRefreshing(false);
            displayToast("Internet/Server Problem: Please try after some time", 1500);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", phone);
                params.put("pass", pass);
                params.put("last_id", passModelList.get(passModelList.size()-1).getId());
                return params;
            }
        };
        strReq.setShouldCache(false);
        com.hindbyte.redfun.utils.Request.addToRequestQueue(this, strReq, tag_string_req);
    }



    private void displayToast(String str, int duration) {
        ToastWindow.makeText(this, str, duration);
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