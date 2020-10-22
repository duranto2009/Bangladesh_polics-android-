package com.example.bdpolice;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class ForgetPassActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$");

    private TextInputEditText textInputphone;
    private TextInputEditText  textInputPassword;
    private TextInputEditText  textInputPasswordconfirm;
    private LinearLayout layoutphonenumber;
    private LinearLayout layoutverification;
    private Button donebtn,verificationbtn;
    String mVerificationId, mPhoneNumber, myCCP;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseAuth mAuth;
    PinView otp;
    private TextView phoneerror,forgetpass_login_btn;
    String phoneNumber ;
    String password ;
    String date ;
    int layout=0;
    private ProgressBar regprogress1,regprogress2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        getSupportActionBar().hide();

        textInputphone = (TextInputEditText) findViewById(R.id.forgetpass_phoneNumber);
        textInputPassword=(TextInputEditText) findViewById(R.id.forgetpass_password);
        textInputPasswordconfirm=(TextInputEditText) findViewById(R.id.forgetpass_confirm_password);
        layoutphonenumber = findViewById(R.id.layoutphonenumber);
        layoutverification = findViewById(R.id.layoutphoneVerification);
        donebtn = findViewById(R.id.forgetpass_btn);
        otp = findViewById(R.id.forgetpass_pinview);
        verificationbtn=findViewById(R.id.forgetpass_Verify_btn);
        phoneerror=findViewById(R.id.forgetpass_verification_status);
        mAuth=FirebaseAuth.getInstance();
        forgetpass_login_btn=findViewById(R.id.forgetpass_login_btn);
        regprogress1=findViewById(R.id.forgetpass_progressBar1);
        regprogress2=findViewById(R.id.forgetpass_progressBar2);

        textInputPasswordconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validatePassword();
            }
        });
        textInputPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validatephone();
            }
        });
        forgetpass_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
       donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatephone()) {
                    if (isConnected(getApplicationContext())) {
                         {

                            phoneNumber = textInputphone.getText().toString().trim();
                            numbervalidation();

                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Internet connection failed", Toast.LENGTH_LONG).show();
                    }
                }
                else {

                }
            }
        });
        verificationbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                //problem
                String code = otp.getText().toString();
                if (!TextUtils.isEmpty(code) && validatePassword() && validateConfirmPassword()) {
                    if (confirmpassword())
                    {
                        verifyPhoneNumberWithCode(mVerificationId, code);
                        regprogress1.setVisibility(View.INVISIBLE);
                        regprogress2.setVisibility(View.VISIBLE);

                    }
                    else {
                        phoneerror.setVisibility(View.VISIBLE);
                        phoneerror.setText("Password not match");

                    }

                }
                else
                {
                    showSnackBar("Please enter the phone code");
                    return;
                }

            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                String code = credential.getSmsCode();
                if (code != null) {
                    layoutphonenumber.setVisibility(View.GONE);
                    layoutverification.setVisibility(View.VISIBLE);
                    regprogress1.setVisibility(View.INVISIBLE);
                    setOTPpin(credential);
                }
//                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.

                regprogress1.setVisibility(View.INVISIBLE);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                    textInputphone.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    showSnackBar("Not change password at this time");
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.


                // Save verification ID and resending token so we can use them later
                regprogress1.setVisibility(View.INVISIBLE);
                mVerificationId = verificationId;
                mResendToken = token;
                layoutphonenumber.setVisibility(View.GONE);
                layoutverification.setVisibility(View.VISIBLE);
                regprogress1.setVisibility(View.INVISIBLE);

                // ...
            }
        };






    }
    private void numbervalidation()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlAddress.ROOT_URI+"forgetpassvalidation.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String s=jsonObject.getString("message");
                    if ( s.equals("Done"))
                    {
                        oth();
                    }
                    else
                    {
                        showSnackBar(jsonObject.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phoneNumber",phoneNumber);
                return  params;
            }
        };

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
    private void oth()
    {
        regprogress1.setVisibility(View.VISIBLE);
        mPhoneNumber ="+88"+textInputphone.getText().toString().trim();
        showSnackBar(mPhoneNumber);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mPhoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                ForgetPassActivity.this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

        phoneerror.setVisibility(View.INVISIBLE);
        phoneNumber = textInputphone.getText().toString().trim();
    }

    private boolean validatephone() {
        String phoneNumber = textInputphone.getText().toString().trim();

        int lenth=phoneNumber.length();
        if (lenth !=11) {

            textInputphone.setError("Please enter a valid phone number");
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
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            textInputPassword.setError("Password too weak");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        String passwordInput = textInputPasswordconfirm.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPasswordconfirm.setError("Field can't be empty");
            return false;
        } else {
            textInputPasswordconfirm.setError(null);
            return true;
        }
    }

    private boolean confirmpassword()
    {
        String temppassword = textInputPassword.getText().toString().trim();
        String tempconfirmpassword = textInputPasswordconfirm.getText().toString().trim();
        if (temppassword.equals(tempconfirmpassword))
        {
            password = textInputPassword.getText().toString().trim();
            return true;
        }
        else {
            return false;
        }
    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            AuthCredential credential = PhoneAuthProvider.getCredential(mPhoneNumber, password);//EmailAuthProvider

                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                            }
                                        }
                                    });

                                }
                            });
                            // Sign in success, update UI with the signed-in user's information

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlAddress.ROOT_URI+"forgetpasschange.php", new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {



                                    try {

                                        JSONObject jsonObject = new JSONObject(response);


                                        regprogress2.setVisibility(View.INVISIBLE);
                                        if (!jsonObject.getBoolean("error"))
                                        {
                                            showSnackBar(jsonObject.getString("message"));
                                            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(),jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("phoneNumber",phoneNumber);
                                    params.put("password",password);
                                    return  params;
                                }
                            };

                            RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);


                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                showSnackBar("Invalid Code");
                                Toast.makeText(getApplicationContext(), "Invalid Code", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    public void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
    public  void  setOTPpin(PhoneAuthCredential credential)
    {
        if (credential!=null) {
            //problem
            otp.setText(credential.getSmsCode());
        }
    }
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        if (isConnected(getApplicationContext())) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithPhoneAuthCredential(credential);
        }
        else {
            regprogress2.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Network connection faild", Toast.LENGTH_LONG).show();
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

    @Override
    protected void onStart() {
        super.onStart();
        if (layout == 0)
        {
            layoutphonenumber.setVisibility(View.VISIBLE);
            layoutverification.setVisibility(View.GONE);
            layout=1;
        }


    }
    @Override
    public void onBackPressed() {
        if (layout == 1)
        {
            layoutphonenumber.setVisibility(View.VISIBLE);
            layoutverification.setVisibility(View.GONE);
            layout =0;
        }
        else
        {
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

}