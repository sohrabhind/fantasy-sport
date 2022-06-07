package com.hindbyte.redfun.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

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

public class RegisterActivity extends AppCompatActivity {

    private SessionManager session;
    private SQLiteHandler db;
    private ProgressDialog pDialog;
    private EditText regIdEditText;
    private EditText regNameEditText;
    private EditText passEditText;
    private EditText verifyPassEditText;
    private String phone;
    private String pass;
    private String verifyPass;
    private String name;
    private boolean firstFocus = true;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new SQLiteHandler(RegisterActivity.this);
        session = new SessionManager(RegisterActivity.this);
        if (session.isLoggedIn()) {
            session.setLogin(false);
            db.deleteUsers();
        }

        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setCancelable(false);

        regIdEditText = findViewById(R.id.regIdEditText);
        regNameEditText = findViewById(R.id.regNameEditText);
        passEditText = findViewById(R.id.passEditText);
        verifyPassEditText = findViewById(R.id.verifyPassEditText);
        Button registerButton = findViewById(R.id.registerButton);

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

        verifyPassEditText.setOnEditorActionListener((v, actionId, event) -> {
            name = regNameEditText.getText().toString().trim();
            phone = regIdEditText.getText().toString().trim();
            pass = passEditText.getText().toString().trim();
            verifyPass = verifyPassEditText.getText().toString().trim();
            if (!phone.isEmpty() && !name.isEmpty()) {
                if (phone.length() > 7 && phone.matches("^[0-9+]*$")) {
                    if (pass.length() > 5) {
                        if (verifyPass.equals(pass)) {
                            registerUser();
                        } else {
                            displayToast("Password do not match", 1500);
                        }
                    } else {
                        displayToast("Password length is too small", 1500);
                    }
                } else {
                    displayToast("Please enter valid phone number", 1500);
                }
            } else {
                displayToast("Please enter your name and phone number", 1500);
            }
            hideKeyboard(getCurrentFocus());
            return true;
        });

        registerButton.setOnClickListener(view -> {
            name = regNameEditText.getText().toString().trim();
            phone = regIdEditText.getText().toString().trim();
            pass = passEditText.getText().toString().trim();
            verifyPass = verifyPassEditText.getText().toString().trim();
            if (!phone.isEmpty() && !name.isEmpty()) {
                if (phone.length() > 7 && phone.matches("^[0-9+]*$")) {
                    if (pass.length() > 5) {
                        if (verifyPass.equals(pass)) {
                            registerUser();
                        } else {
                            displayToast("Password do not match", 1500);
                        }
                    } else {
                        displayToast("Password length is too small", 1500);
                    }
                } else {
                    displayToast("Please enter valid phone number", 1500);
                }
            } else {
                displayToast("Please enter your name and phone number", 1500);
            }
            hideKeyboard(getCurrentFocus());
        });
    }

    private void registerUser() {
        String tag_string_req = "req_reg";
        pDialog.setMessage("Registering...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, "http://hindbyte.com/redfun/account/signup.php", response -> {
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
                    Intent intent = new Intent(RegisterActivity.this, MatchesActivity.class);
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
                params.put("name", name);
                return params;
            }
        };
        strReq.setShouldCache(false);
        com.hindbyte.redfun.utils.Request.addToRequestQueue(this, strReq, tag_string_req);
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
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        return true;
    }
}