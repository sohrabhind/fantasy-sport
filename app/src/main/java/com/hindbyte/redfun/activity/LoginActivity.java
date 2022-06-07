package com.hindbyte.redfun.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
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
import com.hindbyte.redfun.utils.SessionManager;
import com.hindbyte.redfun.utils.ToastWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.hindbyte.redfun.utils.Request.addToRequestQueue;

public class LoginActivity extends AppCompatActivity {

    private SessionManager session;
    private SQLiteHandler db;
    private ProgressDialog pDialog;
    private EditText regIdEditText;
    private EditText passEditText;
    private String phone;
    private String pass;
    private boolean firstFocus = true;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new SQLiteHandler(LoginActivity.this);
        session = new SessionManager(LoginActivity.this);
        if (session.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MatchesActivity.class);
            startActivity(intent);
            finish();
        }

        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setCancelable(false);

        regIdEditText = findViewById(R.id.regIdEditText);
        passEditText = findViewById(R.id.passEditText);
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);
        TextView termView = findViewById(R.id.termView);
        Spannable word = new SpannableString("By login or registration you are accepting our terms and condition.");
        word.setSpan(new ForegroundColorSpan(Color.parseColor("#36B0D4")), 47, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        termView.setText(word);

        regIdEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (firstFocus) {
                regIdEditText.setText("+91");
                Selection.setSelection(regIdEditText.getText(), regIdEditText.getText().length());
                firstFocus = false;
            }
        });

        regIdEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().startsWith("+91")){
                    regIdEditText.setText("+91");
                    Selection.setSelection(regIdEditText.getText(), regIdEditText.getText().length());
                }
            }
        });

        passEditText.setOnEditorActionListener((v, actionId, event) -> {
            phone = regIdEditText.getText().toString().trim();
            pass = passEditText.getText().toString().trim();
            if (!phone.isEmpty()) {
                if (phone.length() > 7 && phone.matches("^[0-9+]*$")) {
                    checkLogin();
                } else {
                    displayToast("Please enter valid phone number", 1500);
                }
            } else {
                displayToast("Please enter your name and phone number", 1500);
            }
            hideKeyboard(getCurrentFocus());
            return true;
        });

        loginButton.setOnClickListener(view -> {
            phone = regIdEditText.getText().toString().trim();
            pass = passEditText.getText().toString().trim();
            if (!phone.isEmpty()) {
                if (phone.length() > 7 && phone.matches("^[0-9+]*$")) {
                    checkLogin();
                } else {
                    displayToast("Please enter valid phone number", 1500);
                }
            } else {
                displayToast("Please enter your name and phone number", 1500);
            }
            hideKeyboard(getCurrentFocus());
        });

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        termView.setOnClickListener(view -> {
            Intent intent = new Intent(this, WebActivity.class);
            intent.putExtra("TITLE", "Terms & Conditions");
            intent.putExtra("URL", "https://www.hindbyte.com/web/termsandconditions/");
            startActivity(intent);
        });
    }

    private void checkLogin() {
        String tag_string_req = "req_login";
        pDialog.setMessage("Logging in...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, "http://hindbyte.com/redfun/account/login.php", response -> {
            hideDialog();
            hideDialog();
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                if (!error) {
                    db.deleteUsers();
                    session.setLogin(true);
                    JSONObject user = jObj.getJSONObject("user");
                    String id = user.getString("id");
                    String name = user.getString("name");
                    String balance = user.getString("balance");
                    String status = user.getString("status");
                    db.addUser(id, phone, pass, name, balance, status);
                    Intent intent = new Intent(LoginActivity.this, MatchesActivity.class);
                    startActivity(intent);
                    finish();
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
                params.put("phone", phone);
                params.put("pass", pass);
                return params;
            }
        };
        strReq.setShouldCache(false);
        addToRequestQueue(this, strReq, tag_string_req);
    }

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
        finishAffinity();
        return true;
    }
}