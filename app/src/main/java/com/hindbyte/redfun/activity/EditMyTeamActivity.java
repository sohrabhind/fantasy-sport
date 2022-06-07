package com.hindbyte.redfun.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.hindbyte.redfun.R;
import com.hindbyte.redfun.utils.SQLiteHandler;
import com.hindbyte.redfun.utils.SubmitWindow;
import com.hindbyte.redfun.utils.ToastWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditMyTeamActivity extends AppCompatActivity {

    String match;
    String match_id;
    String id;
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
    SQLiteHandler db;
    String phone;
    String pass;

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_team);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            match = bundle.getString("MATCH");
            match_id = bundle.getString("MATCH_ID");
            id = bundle.getString("ID");
        }
        db = new SQLiteHandler(this);
        Button toolbar = findViewById(R.id.toolbar);
        toolbar.setText("Update Your Team");
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
        HashMap<String, String> user = db.getUserDetails();
        phone = user.get("phone");
        pass = user.get("pass");
        getMyTeamPlayers();
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
                builder.setPositiveButton("Submit", (dialog, which) -> onSubmit());
                builder.setNegativeButton("Cancal", (dialog, which) -> {
                });
                builder.create().show();
            }
        });
    }

    private void getMyTeamPlayers(){
        String tag_string_req = "req_reg";
        StringRequest strReq = new StringRequest(Request.Method.POST, "http://hindbyte.com/redfun/teams/myteamplayers.php", response -> {
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
                    batsman1.setSelection(aa.getPosition(jObj.getString("batsman1")));
                    batsman2.setAdapter(aa);
                    batsman2.setSelection(aa.getPosition(jObj.getString("batsman2")));
                    batsman3.setAdapter(aa);
                    batsman3.setSelection(aa.getPosition(jObj.getString("batsman3")));
                    batsman4.setAdapter(aa);
                    batsman4.setSelection(aa.getPosition(jObj.getString("batsman4")));
                    batsman5.setAdapter(aa);
                    batsman5.setSelection(aa.getPosition(jObj.getString("batsman5")));
                    batsman6.setAdapter(aa);
                    batsman6.setSelection(aa.getPosition(jObj.getString("batsman6")));
                    ArrayAdapter<String> bb = new ArrayAdapter<>(this , R.layout.spinner_item, bowler);
                    bowler1.setAdapter(bb);
                    bowler1.setSelection(bb.getPosition(jObj.getString("bowler1")));
                    bowler2.setAdapter(bb);
                    bowler2.setSelection(bb.getPosition(jObj.getString("bowler2")));
                    bowler3.setAdapter(bb);
                    bowler3.setSelection(bb.getPosition(jObj.getString("bowler3")));
                    bowler4.setAdapter(bb);
                    bowler4.setSelection(bb.getPosition(jObj.getString("bowler4")));
                    bowler5.setAdapter(bb);
                    bowler5.setSelection(bb.getPosition(jObj.getString("bowler5")));
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
                params.put("phone", Objects.requireNonNull(phone));
                params.put("pass", Objects.requireNonNull(pass));
                params.put("match_id", match_id);
                params.put("id", id);
                return params;
            }
        };
        strReq.setShouldCache(false);
        com.hindbyte.redfun.utils.Request.addToRequestQueue(this, strReq, tag_string_req);
    }



    private void onSubmit() {
        String tag_string_req = "req_reg";
        pDialog.setMessage("Submitting...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, "http://hindbyte.com/redfun/teams/editteam.php", response -> {
            hideDialog();
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                if (!error) {
                    SubmitWindow.makeText(this, "Team updated successfully", 3000);
                } else {
                    String errorMsg = jObj.getString("error_msg");
                    displayToast(errorMsg, 2000);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            hideDialog();
            displayToast("Internet/Server Problem: Please try after some time", 1500);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", Objects.requireNonNull(phone));
                params.put("pass", Objects.requireNonNull(pass));
                params.put("id", id);
                params.put("match_id", match_id);
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
        com.hindbyte.redfun.utils.Request.addToRequestQueue(this, strReq, tag_string_req);
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