package com.hindbyte.redfun.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hindbyte.redfun.R;
import com.hindbyte.redfun.library.SwipyRefreshLayout;
import com.hindbyte.redfun.library.SwipyRefreshLayoutDirection;
import com.hindbyte.redfun.rooms.RoomAdapter;
import com.hindbyte.redfun.rooms.RoomModel;
import com.hindbyte.redfun.utils.SQLiteHandler;
import com.hindbyte.redfun.utils.ToastWindow;

public class RoomsActivity extends AppCompatActivity {

    private RoomAdapter passAdapter;
    private List<RoomModel> passModelList = new ArrayList<>();
    private SwipyRefreshLayout swipeRefreshLayout;
    RoomModel passModel;
    String match;
    String match_id;
    String status;
    String phone;
    String pass;
    SQLiteHandler db;
    Button toolbar;
    String url;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            match = bundle.getString("MATCH");
            match_id = bundle.getString("MATCH_ID");
            status = bundle.getString("STATUS");
        }

        db = new SQLiteHandler(RoomsActivity.this);
        HashMap<String, String> user = db.getUserDetails();
        phone = user.get("phone");
        pass = user.get("pass");
        toolbar = findViewById(R.id.toolbar);
        if (status.equals("CLOSED")) {
            toolbar.setText("Your Contests");
        } else {
            toolbar.setText("Select A Contest");
        }
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        passAdapter = new RoomAdapter(passModelList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));
        recyclerView.setAdapter(passAdapter);
        recyclerView.setNestedScrollingEnabled(true);
        swipeRefreshLayout.setRefreshing(true);
        getTopFeed();
        swipeRefreshLayout.setOnRefreshListener(direction -> {
            if (direction == SwipyRefreshLayoutDirection.TOP && passModelList.isEmpty()) {
                getTopFeed();
            } else {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private void getTopFeed() {
        String tag_string_req = "req_reg";
        if (status.equals("CLOSED")) {
            url = "http://hindbyte.com/redfun/rooms/myrooms.php";
        } else {
            url = "http://hindbyte.com/redfun/rooms/rooms.php";
        }
        StringRequest strReq = new StringRequest(Request.Method.POST, url, response -> {
            swipeRefreshLayout.setRefreshing(false);
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                if (!error) {
                    passModelList.clear();
                    passAdapter.notifyDataSetChanged();
                    JSONArray jArray = jObj.getJSONArray("item");
                    for (int i=0; i < jArray.length(); i++) {
                        JSONObject oneObject = jArray.getJSONObject(i);
                        String id = oneObject.getString("id");
                        String fees = oneObject.getString("fees");
                        String prize = oneObject.getString("prize");
                        String image = oneObject.getString("image");
                        passModel = new RoomModel();
                        passModel.setId(id);
                        passModel.setFees(fees);
                        passModel.setImage(image);
                        passModel.setPrize(prize);
                        passModel.setMatch(match);
                        passModel.setMatchId(match_id);
                        passModel.setStatus(status);
                        if (status.equals("CLOSED")) {
                            String spots = oneObject.getString("spots");
                            passModel.setSpots(spots);
                        }
                        passModel.setViewType(1);
                        passModelList.add(passModel);
                        passAdapter.notifyDataSetChanged();
                    }
                } else {
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
                if (status.equals("CLOSED")) {
                    params.put("phone", phone);
                    params.put("pass", pass);
                    params.put("match_id", match_id);
                }
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