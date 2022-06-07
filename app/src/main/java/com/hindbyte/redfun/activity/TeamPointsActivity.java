package com.hindbyte.redfun.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.hindbyte.redfun.R;
import com.hindbyte.redfun.library.SwipyRefreshLayout;
import com.hindbyte.redfun.utils.SQLiteHandler;
import com.hindbyte.redfun.utils.ToastWindow;

public class TeamPointsActivity extends AppCompatActivity {

    private SwipyRefreshLayout swipeRefreshLayout;
    ScrollView scrollView;
    LinearLayout player1;
    LinearLayout player2;
    LinearLayout player3;
    LinearLayout player4;
    LinearLayout player5;
    LinearLayout player6;
    LinearLayout player7;
    LinearLayout player8;
    LinearLayout player9;
    LinearLayout player10;
    LinearLayout player11;
    TextView batsman1;
    TextView batsman1Points;
    TextView batsman2;
    TextView batsman2Points;
    TextView batsman3;
    TextView batsman3Points;
    TextView batsman4;
    TextView batsman4Points;
    TextView batsman5;
    TextView batsman5Points;
    TextView batsman6;
    TextView batsman6Points;
    TextView bowler1;
    TextView bowler1Points;
    TextView bowler2;
    TextView bowler2Points;
    TextView bowler3;
    TextView bowler3Points;
    TextView bowler4;
    TextView bowler4Points;
    TextView bowler5;
    TextView bowler5Points;
    Button submit;
    Button toolbar;

    String match;
    String match_id;
    String id;
    String phone;
    String pass;
    String playerName1;
    String playerName2;
    String playerName3;
    String playerName4;
    String playerName5;
    String playerName6;
    String playerName7;
    String playerName8;
    String playerName9;
    String playerName10;
    String playerName11;

    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_points);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            match = bundle.getString("MATCH");
            match_id = bundle.getString("MATCH_ID");
            id = bundle.getString("ID");
        }

        SQLiteHandler db = new SQLiteHandler(this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setText(match);
        scrollView = findViewById(R.id.scrollView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        submit = findViewById(R.id.submit);
        player1 = findViewById(R.id.player1);
        player2 = findViewById(R.id.player2);
        player3 = findViewById(R.id.player3);
        player4 = findViewById(R.id.player4);
        player5 = findViewById(R.id.player5);
        player6 = findViewById(R.id.player6);
        player7 = findViewById(R.id.player7);
        player8 = findViewById(R.id.player8);
        player9 = findViewById(R.id.player9);
        player10 = findViewById(R.id.player10);
        player11 = findViewById(R.id.player11);
        batsman1 = findViewById(R.id.batsman1);
        batsman1Points = findViewById(R.id.batsman1Points);
        batsman2 = findViewById(R.id.batsman2);
        batsman2Points = findViewById(R.id.batsman2Points);
        batsman3 = findViewById(R.id.batsman3);
        batsman3Points = findViewById(R.id.batsman3Points);
        batsman4 = findViewById(R.id.batsman4);
        batsman4Points = findViewById(R.id.batsman4Points);
        batsman5 = findViewById(R.id.batsman5);
        batsman5Points = findViewById(R.id.batsman5Points);
        batsman6 = findViewById(R.id.batsman6);
        batsman6Points = findViewById(R.id.batsman6Points);
        bowler1 = findViewById(R.id.bowler1);
        bowler1Points = findViewById(R.id.bowler1Points);
        bowler2 = findViewById(R.id.bowler2);
        bowler2Points = findViewById(R.id.bowler2Points);
        bowler3 = findViewById(R.id.bowler3);
        bowler3Points = findViewById(R.id.bowler3Points);
        bowler4 = findViewById(R.id.bowler4);
        bowler4Points = findViewById(R.id.bowler4Points);
        bowler5 = findViewById(R.id.bowler5);
        bowler5Points = findViewById(R.id.bowler5Points);

        HashMap<String, String> user = db.getUserDetails();
        phone = user.get("phone");
        pass = user.get("pass");
        swipeRefreshLayout.setRefreshing(true);
        getMyPoints();

        swipeRefreshLayout.setOnRefreshListener(direction -> getMyPoints());

        player1.setOnClickListener(view -> {
            Intent intent = new Intent(this, PlayerActivity.class);
            intent.putExtra("MATCH_ID", match_id);
            intent.putExtra("PLAYER", playerName1);
            startActivity(intent);
        });
        player2.setOnClickListener(view -> {
            Intent intent = new Intent(this, PlayerActivity.class);
            intent.putExtra("MATCH_ID", match_id);
            intent.putExtra("PLAYER", playerName2);
            startActivity(intent);
        });
        player3.setOnClickListener(view -> {
            Intent intent = new Intent(this, PlayerActivity.class);
            intent.putExtra("MATCH_ID", match_id);
            intent.putExtra("PLAYER", playerName3);
            startActivity(intent);
        });
        player4.setOnClickListener(view -> {
            Intent intent = new Intent(this, PlayerActivity.class);
            intent.putExtra("MATCH_ID", match_id);
            intent.putExtra("PLAYER", playerName4);
            startActivity(intent);
        });
        player5.setOnClickListener(view -> {
            Intent intent = new Intent(this, PlayerActivity.class);
            intent.putExtra("MATCH_ID", match_id);
            intent.putExtra("PLAYER", playerName5);
            startActivity(intent);
        });
        player6.setOnClickListener(view -> {
            Intent intent = new Intent(this, PlayerActivity.class);
            intent.putExtra("MATCH_ID", match_id);
            intent.putExtra("PLAYER", playerName6);
            startActivity(intent);
        });
        player7.setOnClickListener(view -> {
            Intent intent = new Intent(this, PlayerActivity.class);
            intent.putExtra("MATCH_ID", match_id);
            intent.putExtra("PLAYER", playerName7);
            startActivity(intent);
        });
        player8.setOnClickListener(view -> {
            Intent intent = new Intent(this, PlayerActivity.class);
            intent.putExtra("MATCH_ID", match_id);
            intent.putExtra("PLAYER", playerName8);
            startActivity(intent);
        });
        player9.setOnClickListener(view -> {
            Intent intent = new Intent(this, PlayerActivity.class);
            intent.putExtra("MATCH_ID", match_id);
            intent.putExtra("PLAYER", playerName9);
            startActivity(intent);
        });
        player10.setOnClickListener(view -> {
            Intent intent = new Intent(this, PlayerActivity.class);
            intent.putExtra("MATCH_ID", match_id);
            intent.putExtra("PLAYER", playerName10);
            startActivity(intent);
        });
        player11.setOnClickListener(view -> {
            Intent intent = new Intent(this, PlayerActivity.class);
            intent.putExtra("MATCH_ID", match_id);
            intent.putExtra("PLAYER", playerName11);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @SuppressLint("SetTextI18n")
    private void getMyPoints(){
        String tag_string_req = "req_reg";
        StringRequest strReq = new StringRequest(Request.Method.POST, "http://hindbyte.com/redfun/teams/teampoints.php", response -> {
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                if (!error) {
                    scrollView.setVisibility(View.VISIBLE);
                    JSONArray jArray = jObj.getJSONArray("item");
                    playerName1 = jArray.getJSONObject(0).getString("player");
                    playerName2 = jArray.getJSONObject(1).getString("player");
                    playerName3 = jArray.getJSONObject(2).getString("player");
                    playerName4 = jArray.getJSONObject(3).getString("player");
                    playerName5 = jArray.getJSONObject(4).getString("player");
                    playerName6 = jArray.getJSONObject(5).getString("player");
                    playerName7 = jArray.getJSONObject(6).getString("player");
                    playerName8 = jArray.getJSONObject(7).getString("player");
                    playerName9 = jArray.getJSONObject(8).getString("player");
                    playerName10 = jArray.getJSONObject(9).getString("player");
                    playerName11 = jArray.getJSONObject(10).getString("player");
                    int points1 = jArray.getJSONObject(0).getInt("points");
                    int points2 = jArray.getJSONObject(1).getInt("points");
                    int points3 = jArray.getJSONObject(2).getInt("points");
                    int points4 = jArray.getJSONObject(3).getInt("points");
                    int points5 = jArray.getJSONObject(4).getInt("points");
                    int points6 = jArray.getJSONObject(5).getInt("points");
                    int points7 = jArray.getJSONObject(6).getInt("points");
                    int points8 = jArray.getJSONObject(7).getInt("points");
                    int points9 = jArray.getJSONObject(8).getInt("points");
                    int points10 = jArray.getJSONObject(9).getInt("points");
                    int points11 = jArray.getJSONObject(10).getInt("points");

                    batsman1.setText(playerName1);
                    batsman1Points.setText("POINTS: "+points1);
                    batsman2.setText(playerName2);
                    batsman2Points.setText("POINTS: "+points2);
                    batsman3.setText(playerName3);
                    batsman3Points.setText("POINTS : "+points3);
                    batsman4.setText(playerName4);
                    batsman4Points.setText("POINTS : "+points4);
                    batsman5.setText(playerName5);
                    batsman5Points.setText("POINTS : "+points5);
                    batsman6.setText(playerName6);
                    batsman6Points.setText("POINTS : "+points6);
                    bowler1.setText(playerName7);
                    bowler1Points.setText("POINTS : "+points7);
                    bowler2.setText(playerName8);
                    bowler2Points.setText("POINTS : "+points8);
                    bowler3.setText(playerName9);
                    bowler3Points.setText("POINTS : "+points9);
                    bowler4.setText(playerName10);
                    bowler4Points.setText("POINTS : "+points10);
                    bowler5.setText(playerName11);
                    bowler5Points.setText("POINTS : "+points11);
                    int totalPoints = points1 + points2 + points3 + points4 + points5 + points6 + points7 + points8 + points9 + points10 + points11;
                    toolbar.setText("Total Points : " + totalPoints);
                } else {
                    String errorMsg = jObj.getString("error_msg");
                    displayToast(errorMsg, 2000);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            swipeRefreshLayout.setRefreshing(false);
        }, error -> {
            swipeRefreshLayout.setRefreshing(false);
            displayToast("Internet/Server Problem: Please try after some time", 1500);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("match_id", match_id);
                params.put("phone", Objects.requireNonNull(phone));
                params.put("pass", Objects.requireNonNull(pass));
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