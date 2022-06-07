package com.hindbyte.redfun.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.hindbyte.redfun.BuildConfig;
import com.hindbyte.redfun.R;
import com.hindbyte.redfun.utils.SQLiteHandler;
import com.hindbyte.redfun.utils.SessionManager;
import com.hindbyte.redfun.utils.ToastWindow;

public class AccountActivity extends AppCompatActivity {

    TextView moneyTextView;
    Button withDrawButton;
    SQLiteHandler db;
    SessionManager session;
    String phone;
    String pass;
    String balance;

    @SuppressLint({"SetTextI18n", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        db = new SQLiteHandler(AccountActivity.this);
        session = new SessionManager(AccountActivity.this);
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        TextView loginIDTextView = findViewById(R.id.loginIDTextView);
        moneyTextView = findViewById(R.id.moneyTextView);
        withDrawButton = findViewById(R.id.withDrawButton);
        Button passbookButton = findViewById(R.id.passbookButton);
        Button addMoneyButton = findViewById(R.id.addMoneyButton);
        Button logOutButton = findViewById(R.id.logOutButton);

        HashMap<String, String> user = db.getUserDetails();
        phone = user.get("phone");
        pass = user.get("pass");
        balance = user.get("balance");
        moneyTextView.setText("₹ " + balance);
        loginIDTextView.setText(phone);
        withDrawButton.setOnClickListener(v -> {
            final String[] method = {"BANK ACCOUNT", "UPI"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setItems(method, (dialog, which) -> {
                if ("BANK ACCOUNT".equals(method[which])) {
                    Intent intent = new Intent(this, PayoutActivity.class);
                    intent.putExtra("METHOD", "BANK_ACCOUNT");
                    startActivity(intent);
                } else if ("UPI".equals(method[which])) {
                    Intent intent = new Intent(this, PayoutActivity.class);
                    intent.putExtra("METHOD", "UPI");
                    startActivity(intent);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        passbookButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, PassbookActivity.class);
            startActivity(intent);
        });

        addMoneyButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            @SuppressLint("InflateParams") LinearLayout signInLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.dialog_add_amount, null, false);
            builder.setView(signInLayout);
            EditText adsIntervalView = signInLayout.findViewById(R.id.ad_interval_view);
            adsIntervalView.setText("100");
            adsIntervalView.requestFocus();
            builder.setPositiveButton("Add Money", (dialog, which) -> {
                String amount = adsIntervalView.getText().toString().trim();
                if (!amount.equals("") && Integer.parseInt(amount) > 0) {
                    if (Integer.parseInt(amount) < 50001) {
                        Intent intent = new Intent(this, CheckoutActivity.class);
                        intent.putExtra("TITLE", "Add ₹"+amount+ " To Wallet");
                        intent.putExtra("AMOUNT", amount);
                        startActivity(intent);
                    } else {
                        displayToast("Please Add Amount Less Than ₹ 50001", 3000);
                    }
                } else {
                    displayToast("Please Add Amount More Than ₹ 0", 2500);
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        });
        logOutButton.setOnClickListener(v -> logoutUser());
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLogin();
    }

    @SuppressLint("SetTextI18n")
    private void checkLogin() {
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST, "http://hindbyte.com/redfun/account/login.php", response -> {
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                if (!error) {
                    db.deleteUsers();
                    session.setLogin(true);
                    JSONObject user = jObj.getJSONObject("user");
                    String id = user.getString("id");
                    String name = user.getString("name");
                    balance = user.getString("balance");
                    String status = user.getString("status");
                    JSONObject app = jObj.getJSONObject("app");
                    String version = app.getString("version");
                    int versionCode = BuildConfig.VERSION_CODE;
                    if (version.contains("P") && Integer.parseInt(version.replace("P", "")) > versionCode) {
                        displayToast("Download new update", 5000);
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                        } catch (android.content.ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.hindbyte.com/web/download/")));
                        }
                    } else if (version.contains("W") && Integer.parseInt(version.replace("W", "")) > versionCode) {
                        displayToast("Download new update", 5000);
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.hindbyte.com/web/download/")));
                    }
                    db.addUser(id, phone, pass, name, balance, status);
                    moneyTextView.setText("₹ " + balance);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", phone);
                params.put("pass", pass);
                return params;
            }
        };
        strReq.setShouldCache(false);
        com.hindbyte.redfun.utils.Request.addToRequestQueue(this, strReq, tag_string_req);
    }

    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        finishAffinity();
        Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
        startActivity(intent);
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