package com.example.bdpolice;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$");

    private TextInputEditText textInputphone;
    private TextInputEditText  textInputPassword;
    private TextView login_error,login_forget_pass,login_creat_acc;
    private ProgressBar login_progress;
    private Button login_btn;
    private String phoneNumber;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        textInputPassword=findViewById(R.id.login_password);
        textInputphone=findViewById(R.id.login_phoneNumber);
        login_btn=findViewById(R.id.login_btn);
        login_error=findViewById(R.id.login_password_wrong);
        login_forget_pass=findViewById(R.id.login_forget_btn);
        login_creat_acc=findViewById(R.id.login_reg_btn);
        login_progress=findViewById(R.id.login_progressBar);


        login_forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regintent=new Intent(LoginActivity.this,ForgetPassActivity.class);
                startActivity(regintent);
                finish();
            }
        });
        login_creat_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regintent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(regintent);
                finish();
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatephone() && validatePassword())
                {
                    if (isConnected(getApplicationContext()))
                    {
                        login_progress.setVisibility(View.VISIBLE);
                        phoneNumber = textInputphone.getText().toString().trim();
                        password = textInputPassword.getText().toString().trim();
                        Toast.makeText(getApplicationContext(),UrlAddress.ROOT_URI+"loginphp.php",Toast.LENGTH_LONG).show();


                        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlAddress.ROOT_URI+"loginphp.php", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if(!jsonObject.getBoolean("error")){
                                        SharedPrefManager.getInstance(getApplicationContext())
                                                .userLogin(
                                                        jsonObject.getString("id"),
                                                        jsonObject.getString("name"),
                                                        jsonObject.getString("rank"),
                                                        jsonObject.getString("image")
                                                );
                                        login_progress.setVisibility(View.INVISIBLE);
                                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }else{
                                        login_error.setText(jsonObject.getString("message"));
//                                        Toast.makeText(
//                                                getApplicationContext(),
//                                                jsonObject.getString("message"),
//                                                Toast.LENGTH_LONG
//                                        ).show();
                                    }
                                    login_progress.setVisibility(View.INVISIBLE);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(LoginActivity.this,error.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("phoneNumber",textInputphone.getText().toString().trim());
                                params.put("password",textInputPassword.getText().toString().trim());
                                return  params;
                            }
                        };

                        RequestHandler.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Internet connect failed", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


    }

    private boolean validatephone() {
        String phoneNumber = textInputphone.getText().toString().trim();

        int lenth=phoneNumber.length();


        if (lenth !=11) {

            textInputphone.setError("Number not valid");
            return false;
        }
        if (phoneNumber.isEmpty()) {
            textInputphone.setError("Field can't be empty");
            return false;
        }
        else {
            textInputphone.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = textInputPassword.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }

    public boolean isConnected(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnectedOrConnecting()){
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else
                return false;
        }else
            return false;
    }


}
