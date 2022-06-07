package com.hindbyte.redfun.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.hindbyte.redfun.R;
import com.hindbyte.redfun.utils.SQLiteHandler;
import com.hindbyte.redfun.utils.SubmitWindow;
import com.hindbyte.redfun.utils.ToastWindow;

import static com.hindbyte.redfun.utils.Request.addToRequestQueue;

public class MakeTeamActivity extends AppCompatActivity {

    String match;
    String match_id;
    String room_id;
    Spinner batsman1;
    Spinner batsman2;
    Spinner batsman3;
    Spinner batsman4;
    Spinner batsman5;
    Spinner batsman6;
    Spinner bowler1;
    Spinner bowler2;
    Spinner bowler3;
    Spinner bowler4;
    Spinner bowler5;
    Button submit;
    NestedScrollView scrollView;

    String batsman1Text;
    String batsman2Text;
    String batsman3Text;
    String batsman4Text;
    String batsman5Text;
    String batsman6Text;
    String bowler1Text;
    String bowler2Text;
    String bowler3Text;
    String bowler4Text;
    String bowler5Text;
    boolean isSubmitting = false;

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_team);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            match = bundle.getString("MATCH");
            match_id = bundle.getString("MATCH_ID");
            room_id = bundle.getString("ROOM_ID");
        }

        Button toolbar = findViewById(R.id.toolbar);
        toolbar.setText("Select Your Team");
        scrollView = findViewById(R.id.scrollView);
        batsman1 = findViewById(R.id.batsman1);
        batsman2 = findViewById(R.id.batsman2);
        batsman3 = findViewById(R.id.batsman3);
        batsman4 = findViewById(R.id.batsman4);
        batsman5 = findViewById(R.id.batsman5);
        batsman6 = findViewById(R.id.batsman6);
        bowler1 = findViewById(R.id.bowler1);
        bowler2 = findViewById(R.id.bowler2);
        bowler3 = findViewById(R.id.bowler3);
        bowler4 = findViewById(R.id.bowler4);
        bowler5 = findViewById(R.id.bowler5);
        getMatchPlayers();
        setSubmitButton();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setSubmitButton() {
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(v -> {
            boolean unique = false;
            batsman1Text = batsman1.getSelectedItem().toString();
            batsman2Text = batsman2.getSelectedItem().toString();
            batsman3Text = batsman3.getSelectedItem().toString();
            batsman4Text = batsman4.getSelectedItem().toString();
            batsman5Text = batsman5.getSelectedItem().toString();
            batsman6Text = batsman6.getSelectedItem().toString();
            bowler1Text = bowler1.getSelectedItem().toString();
            bowler2Text = bowler2.getSelectedItem().toString();
            bowler3Text = bowler3.getSelectedItem().toString();
            bowler4Text = bowler4.getSelectedItem().toString();
            bowler5Text = bowler5.getSelectedItem().toString();
            ((TextView) batsman1.getSelectedView()).setTextColor(Color.parseColor("#464646"));
            ((TextView) batsman2.getSelectedView()).setTextColor(Color.parseColor("#464646"));
            ((TextView) batsman3.getSelectedView()).setTextColor(Color.parseColor("#464646"));
            ((TextView) batsman4.getSelectedView()).setTextColor(Color.parseColor("#464646"));
            ((TextView) batsman5.getSelectedView()).setTextColor(Color.parseColor("#464646"));
            ((TextView) batsman6.getSelectedView()).setTextColor(Color.parseColor("#464646"));
            ((TextView) bowler1.getSelectedView()).setTextColor(Color.parseColor("#464646"));
            ((TextView) bowler2.getSelectedView()).setTextColor(Color.parseColor("#464646"));
            ((TextView) bowler3.getSelectedView()).setTextColor(Color.parseColor("#464646"));
            ((TextView) bowler4.getSelectedView()).setTextColor(Color.parseColor("#464646"));
            ((TextView) bowler5.getSelectedView()).setTextColor(Color.parseColor("#464646"));

            ArrayList<String> list = new ArrayList<>();
            list.add(batsman1Text);
            if(!list.contains(batsman2Text)) {
                list.add(batsman2Text);
                if(!list.contains(batsman3Text)) {
                    list.add(batsman3Text);
                    if(!list.contains(batsman4Text)) {
                        list.add(batsman4Text);
                        if(!list.contains(batsman5Text)) {
                            list.add(batsman5Text);
                            if(!list.contains(batsman6Text)) {
                                list.add(batsman6Text);
                                if(!list.contains(bowler1Text)) {
                                    list.add(bowler1Text);
                                    if(!list.contains(bowler2Text)) {
                                        list.add(bowler2Text);
                                        if(!list.contains(bowler3Text)) {
                                            list.add(bowler3Text);
                                            if(!list.contains(bowler4Text)) {
                                                list.add(bowler4Text);
                                                if(!list.contains(bowler5Text)) {
                                                    list.add(bowler5Text);
                                                    unique = true;
                                                } else {
                                                    displayToast("Select " + bowler5Text+ " Only One Time", 2000);
                                                    ((TextView) bowler5.getSelectedView()).setTextColor(Color.parseColor("#FF0000"));
                                                }
                                            } else {
                                                displayToast("Select " + bowler4Text+ " Only One Time", 2000);
                                                ((TextView) bowler4.getSelectedView()).setTextColor(Color.parseColor("#FF0000"));
                                            }
                                        } else {
                                            displayToast("Select " + bowler3Text+ " Only One Time", 2000);
                                            ((TextView) bowler3.getSelectedView()).setTextColor(Color.parseColor("#FF0000"));
                                        }
                                    } else {
                                        displayToast("Select " + bowler2Text+ " Only One Time", 2000);
                                        ((TextView) bowler2.getSelectedView()).setTextColor(Color.parseColor("#FF0000"));
                                    }
                                } else {
                                    displayToast("Select " + bowler1Text + " Only One Time", 2000);
                                    ((TextView) bowler1.getSelectedView()).setTextColor(Color.parseColor("#FF0000"));
                                }
                            } else {
                                displayToast("Select " + batsman6Text+ " Only One Time", 2000);
                                ((TextView) batsman6.getSelectedView()).setTextColor(Color.parseColor("#FF0000"));
                            }
                        } else {
                            displayToast("Select " + batsman5Text+ " Only One Time", 2000);
                            ((TextView) batsman5.getSelectedView()).setTextColor(Color.parseColor("#FF0000"));
                        }
                    } else {
                        displayToast("Select " + batsman4Text+ " Only One Time", 2000);
                        ((TextView) batsman4.getSelectedView()).setTextColor(Color.parseColor("#FF0000"));
                    }
                } else {
                    displayToast("Select " + batsman3Text+ " Only One Time", 2000);
                    ((TextView) batsman3.getSelectedView()).setTextColor(Color.parseColor("#FF0000"));
                }
            } else {
                displayToast("Select " + batsman2Text+ " Only One Time", 2000);
                ((TextView) batsman2.getSelectedView()).setTextColor(Color.parseColor("#FF0000"));
            }
            if (unique) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setMessage("Do you want to submit?");
                builder.setPositiveButton("Submit", (dialog, which) -> {
                    if (!isSubmitting) {
                        isSubmitting = true;
                        onSubmit();
                    }
                });
                builder.setNegativeButton("Cancal", (dialog, which) -> {
                });
                builder.create().show();
            }
        });
    }

    private void getMatchPlayers(){
        String tag_string_req = "req_reg";
        StringRequest strReq = new StringRequest(Request.Method.POST, "http://hindbyte.com/redfun/matches/matchplayers.php", response -> {
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                if (!error) {
                    JSONArray jArray = jObj.getJSONArray("item");
                    scrollView.setVisibility(View.VISIBLE);
                    ArrayList<String> batsman = new ArrayList<>();
                    ArrayList<String> bowler = new ArrayList<>();
                    for (int i=0; i < jArray.length(); i++) {
                        JSONObject oneObject = jArray.getJSONObject(i);
                        String player = oneObject.getString("player");
                        String type = oneObject.getString("type");
                        if (type.equals("batsman")) {
                            batsman.add(player);
                        } else if (type.equals("bowler")){
                            bowler.add(player);
                        }
                    }
                    ArrayAdapter<String> aa = new ArrayAdapter<>(this , R.layout.spinner_item, batsman);
                    batsman1.setAdapter(aa);
                    batsman2.setAdapter(aa);
                    batsman3.setAdapter(aa);
                    batsman4.setAdapter(aa);
                    batsman5.setAdapter(aa);
                    batsman6.setAdapter(aa);
                    ArrayAdapter<String> bb = new ArrayAdapter<>(this , R.layout.spinner_item, bowler);
                    bowler1.setAdapter(bb);
                    bowler2.setAdapter(bb);
                    bowler3.setAdapter(bb);
                    bowler4.setAdapter(bb);
                    bowler5.setAdapter(bb);
                } else {
                    String errorMsg = jObj.getString("error_msg");
                    displayToast(errorMsg, 2000);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> displayToast("Internet/Server Problem: Please try after some time", 1500)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("match_id", match_id);
                return params;
            }
        };
        strReq.setShouldCache(false);
        addToRequestQueue(this, strReq, tag_string_req);
    }

    private void onSubmit() {
        String tag_string_req = "req_reg";
        pDialog.setMessage("Submitting...");
        showDialog();
        SQLiteHandler db = new SQLiteHandler(this);
        HashMap<String, String> user = db.getUserDetails();
        String phone = user.get("phone");
        String pass = user.get("pass");
        StringRequest strReq = new StringRequest(Request.Method.POST, "http://hindbyte.com/redfun/teams/maketeam.php", response -> {
            hideDialog();
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                if (!error) {
                    playerReset();
                    SubmitWindow.makeText(this, "Team submitted successfully", 3000);
                } else {
                    String errorMsg = jObj.getString("error_msg");
                    if (errorMsg.equals("!balance")) {
                        String errorAmount = jObj.getString("error_amount");
                        pDialog.setMessage("You have ₹ " + errorAmount + " Less Than Match Fees");
                        showDialog();
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(() -> {
                            hideDialog();
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setCancelable(true);
                            @SuppressLint("InflateParams") LinearLayout signInLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.dialog_add_amount, null, false);
                            builder.setView(signInLayout);
                            TextView adsIntervalView = signInLayout.findViewById(R.id.ad_interval_view);
                            adsIntervalView.setText(errorAmount);
                            adsIntervalView.requestFocus();
                            builder.setPositiveButton("Add Money", (dialog, which) -> {
                                String amount = adsIntervalView.getText().toString().trim();
                                if (!amount.equals("") && Integer.valueOf(amount) >= Integer.valueOf(errorAmount)) {
                                    if (Integer.valueOf(amount) < 50001) {
                                        Intent intent = new Intent(this, CheckoutActivity.class);
                                        intent.putExtra("TITLE", "Add ₹"+amount+ " To Wallet");
                                        intent.putExtra("AMOUNT", amount);
                                        startActivity(intent);
                                    } else {
                                        displayToast("Please Add Amount Less Than ₹ 50001", 2500);
                                    }
                                } else {
                                    displayToast("Please Add Amount More Than Required", 3000);
                                }
                            });
                            builder.setNegativeButton("No", (dialog, which) -> {
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        },  2500);
                    } else {
                        displayToast(errorMsg, 2000);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            isSubmitting = false;
        }, error -> {
            isSubmitting = false;
            hideDialog();
            displayToast("Internet/Server Problem: Please try after some time", 1500);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", Objects.requireNonNull(phone));
                params.put("pass", Objects.requireNonNull(pass));
                params.put("match_id", match_id);
                params.put("room_id", room_id);
                params.put("batsman1", batsman1Text);
                params.put("batsman2", batsman2Text);
                params.put("batsman3", batsman3Text);
                params.put("batsman4", batsman4Text);
                params.put("batsman5", batsman5Text);
                params.put("batsman6", batsman6Text);
                params.put("bowler1", bowler1Text);
                params.put("bowler2", bowler2Text);
                params.put("bowler3", bowler3Text);
                params.put("bowler4", bowler4Text);
                params.put("bowler5", bowler5Text);
                return params;
            }
        };
        strReq.setShouldCache(false);
        addToRequestQueue(this, strReq, tag_string_req);
    }

    private void playerReset() {
        batsman1.setSelection(0);
        batsman2.setSelection(0);
        batsman3.setSelection(0);
        batsman4.setSelection(0);
        batsman5.setSelection(0);
        batsman6.setSelection(0);
        bowler1.setSelection(0);
        bowler2.setSelection(0);
        bowler3.setSelection(0);
        bowler4.setSelection(0);
        bowler5.setSelection(0);
    }

    private ProgressDialog pDialog;

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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