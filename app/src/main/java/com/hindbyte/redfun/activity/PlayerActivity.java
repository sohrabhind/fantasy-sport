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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hindbyte.redfun.R;
import com.hindbyte.redfun.library.SwipyRefreshLayout;
import com.hindbyte.redfun.player.PlayerAdapter;
import com.hindbyte.redfun.player.PlayerModel;
import com.hindbyte.redfun.utils.SQLiteHandler;
import com.hindbyte.redfun.utils.ToastWindow;

public class PlayerActivity extends AppCompatActivity {

    private PlayerAdapter playerAdapter;
    private List<PlayerModel> playerModelList = new ArrayList<>();
    private SwipyRefreshLayout swipeRefreshLayout;
    PlayerModel playerModel;
    Button toolbar;
    String match_id;
    String player;
    SQLiteHandler db;
    String phone;
    String pass;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            match_id = bundle.getString("MATCH_ID");
            player = bundle.getString("PLAYER");
        }

        toolbar = findViewById(R.id.toolbar);
        toolbar.setText(player);
        db = new SQLiteHandler(PlayerActivity.this);
        HashMap<String, String> user = db.getUserDetails();
        phone = user.get("phone");
        pass = user.get("pass");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        playerAdapter = new PlayerAdapter(playerModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));
        recyclerView.setAdapter(playerAdapter);
        recyclerView.setNestedScrollingEnabled(true);
        swipeRefreshLayout.setRefreshing(true);
        getTopFeed();
        swipeRefreshLayout.setOnRefreshListener(direction -> getTopFeed());
    }

    private void getTopFeed() {
        String tag_string_req = "req_reg";
        StringRequest strReq = new StringRequest(Request.Method.POST, "http://hindbyte.com/redfun/matches/playerpoints.php", response -> {
            swipeRefreshLayout.setRefreshing(false);
            try {
                playerModelList.clear();
                playerAdapter.notifyDataSetChanged();
                JSONObject jObj = new JSONObject(response);
                JSONArray jArray = jObj.getJSONArray("item");
                for (int i=0; i < jArray.length(); i++) {
                    JSONObject oneObject = jArray.getJSONObject(i);
                    String id = oneObject.getString("id");
                    String event = oneObject.getString("event");
                    String actual = oneObject.getString("actual");
                    String points = oneObject.getString("points");
                    playerModel = new PlayerModel();
                    playerModel.setId(id);
                    playerModel.setEvent(event);
                    playerModel.setActual(actual);
                    playerModel.setPoints(points);
                    playerModel.setViewType(1);
                    playerModelList.add(playerModel);
                    playerAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            swipeRefreshLayout.setRefreshing(false);
            displayToast();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("match_id", match_id);
                params.put("player", player);
                return params;
            }
        };
        strReq.setShouldCache(false);
        com.hindbyte.redfun.utils.Request.addToRequestQueue(this, strReq, tag_string_req);
    }

    private void displayToast() {
        ToastWindow.makeText(this, "Internet/Server Problem: Please try after some time", 1500);
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