package com.hindbyte.redfun.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.hindbyte.redfun.R;
import com.hindbyte.redfun.utils.SQLiteHandler;
import com.hindbyte.redfun.utils.SubmitWindow;
import com.hindbyte.redfun.utils.ToastWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PayoutActivity extends AppCompatActivity {

    private EditText ifscCode;
    private EditText accountNumber;
    private EditText verifyAccountNumber;
    private EditText upiId;
    private EditText withDrawAmount;
    private String method;
    private String phone;
    private String pass;
    private String account = "";
    private String ifsc = "";
    private String upi = "";
    private String amount = "0";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            method = bundle.getString("METHOD");
        }

        SQLiteHandler db = new SQLiteHandler(PayoutActivity.this);
        HashMap<String, String> user = db.getUserDetails();
        phone = user.get("phone");
        pass = user.get("pass");

        TextView textIntro = findViewById(R.id.textIntro);
        accountNumber = findViewById(R.id.accountNumber);
        verifyAccountNumber = findViewById(R.id.verifyAccountNumber);
        ifscCode = findViewById(R.id.ifscCode);
        upiId = findViewById(R.id.upiId);
        withDrawAmount = findViewById(R.id.withDrawAmount);
        Button withDrawButton = findViewById(R.id.withDrawButton);
        if (method.equals("BANK_ACCOUNT")) {
            textIntro.setText("Fill account details for withdraw");
            upiId.setVisibility(View.GONE);

            withDrawAmount.setOnEditorActionListener((v, actionId, event) -> {
                withdrawByNEFT();
                hideKeyboard(getCurrentFocus());
                return true;
            });

            withDrawButton.setOnClickListener(view -> {
                withdrawByNEFT();
                hideKeyboard(getCurrentFocus());
            });
        } else if (method.equals("UPI")) {
            textIntro.setText("Fill UPI details for withdraw");
            accountNumber.setVisibility(View.GONE);
            verifyAccountNumber.setVisibility(View.GONE);
            ifscCode.setVisibility(View.GONE);

            withDrawAmount.setOnEditorActionListener((v, actionId, event) -> {
                withdrawByUPI();
                hideKeyboard(getCurrentFocus());
                return true;
            });

            withDrawButton.setOnClickListener(view -> {
                withdrawByUPI();
                hideKeyboard(getCurrentFocus());
            });
        }
    }

    private void withdrawByNEFT() {
        account = accountNumber.getText().toString().trim();
        String verifyAccount = verifyAccountNumber.getText().toString().trim();
        ifsc = ifscCode.getText().toString().trim().toUpperCase();
        amount = withDrawAmount.getText().toString().trim();
        if (account.length() > 5) {
            if (account.equals(verifyAccount)) {
                if (ifsc.matches("^[A-Z|a-z]{4}[0][0-9]{6}$")) {
                    if (!amount.isEmpty() && Integer.valueOf(amount) >= 100) {
                        withDrawMoney();
                    } else {
                        displayToast("Payment threshold is ₹ 100", 1500);
                    }
                } else {
                    displayToast("Please enter valid account ifsc code", 1500);
                }
            } else {
                displayToast("Account number do not match", 1500);
            }
        } else {
            displayToast("Please enter valid account number", 1500);
        }
    }

    private void withdrawByUPI() {
        upi = upiId.getText().toString().trim();
        amount = withDrawAmount.getText().toString().trim();
        if (upi.matches("^\\w+@\\w+$")) {
            if (!amount.isEmpty() && Integer.valueOf(amount) >= 100) {
                withDrawMoney();
            } else {
                displayToast("Payment threshold is ₹ 100", 1500);
            }
        } else {
            displayToast("Please enter valid upi", 1500);
        }
    }

    @SuppressLint("SetTextI18n")
    private void withDrawMoney() {
        String tag_string_req = "req_withdraw";
        StringRequest strReq = new StringRequest(Request.Method.POST, "http://hindbyte.com/redfun/account/withdraw.php", response -> {
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                if (!error) {
                    SubmitWindow.makeText(this, "Money will be transferred to your account soon", 3000);
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
                params.put("phone", phone);
                params.put("pass", pass);
                params.put("account", account);
                params.put("ifsc", ifsc);
                params.put("upi", upi);
                params.put("amount", amount);
                return params;
            }
        };
        strReq.setShouldCache(false);
        com.hindbyte.redfun.utils.Request.addToRequestQueue(this, strReq, tag_string_req);
    }

    private void displayToast(String str, int duration) {
        ToastWindow.makeText(this, str, duration);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK && onKeyCodeBack();
    }

    private boolean onKeyCodeBack() {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
        return true;
    }
}