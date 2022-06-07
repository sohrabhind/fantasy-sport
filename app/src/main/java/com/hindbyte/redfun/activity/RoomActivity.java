package com.hindbyte.redfun.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hindbyte.redfun.R;

public class RoomActivity extends AppCompatActivity {

    Button toolbar;
    String match;
    String match_id;
    String room_id;
    String status;
    TextView winnersButton;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            match = bundle.getString("MATCH");
            match_id = bundle.getString("MATCH_ID");
            room_id = bundle.getString("ROOM_ID");
            status = bundle.getString("STATUS");
        }
        toolbar = findViewById(R.id.toolbar);
        toolbar.setText(match);

        Button myTeamButton = findViewById(R.id.myTeamButton);
        winnersButton = findViewById(R.id.winnersButton);
        Button myPointsButton = findViewById(R.id.myPointsButton);
        Button rankButton = findViewById(R.id.rankButton);
        if (status.equals("CLOSED") && bundle != null) {
            winnersButton.setVisibility(View.VISIBLE);
            myTeamButton.setVisibility(View.GONE);
            String spots = bundle.getString("SPOTS");
            String winners = bundle.getString("WINNERS");
            winnersButton.setText("Total teams: "+spots+", Winners: "+winners);
        } else {
            myTeamButton.setOnClickListener(v -> {
                Intent intent = new Intent(this, MakeTeamActivity.class);
                intent.putExtra("MATCH", match);
                intent.putExtra("MATCH_ID", match_id);
                intent.putExtra("ROOM_ID", room_id);
                startActivity(intent);
            });
        }

        myPointsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyTeamsActivity.class);
            intent.putExtra("MATCH", match);
            intent.putExtra("MATCH_ID", match_id);
            intent.putExtra("ROOM_ID", room_id);
            startActivity(intent);
        });

        rankButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, TeamsActivity.class);
            intent.putExtra("MATCH", match);
            intent.putExtra("MATCH_ID", match_id);
            intent.putExtra("ROOM_ID", room_id);
            startActivity(intent);
        });
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