package com.example.bdpolice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PeopleActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView name;
    TextView father_name;
    TextView current_address;
    TextView permanent_address;
    TextView birth_date;
    TextView gender;
    TextView religion;
    TextView married;
    TextView phone_number;
    TextView email_id;
    TextView nid_no;
    TextView passport_no;
    TextView emergency_name;
    TextView emergency_relation;
    TextView emergency_phone_number;
    TextView emergency_address;
    ImageView image;
    private SwipeRefreshLayout swipeRefreshLayout;
    String id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        getSupportActionBar().hide();
        name = findViewById(R.id.name);
        father_name = findViewById(R.id.father_name);
        current_address = findViewById(R.id.current_address);
        permanent_address = findViewById(R.id.permanent_address);
        birth_date = findViewById(R.id.birth_date);
        gender = findViewById(R.id.gender);
        religion = findViewById(R.id.religion);
        married = findViewById(R.id.married);
        phone_number = findViewById(R.id.phone_number);
        email_id = findViewById(R.id.email_id);
        nid_no = findViewById(R.id.nid_no);
        passport_no = findViewById(R.id.passport_no);
        emergency_name = findViewById(R.id.emergency_name);
        emergency_relation = findViewById(R.id.emergency_relation);
        emergency_phone_number = findViewById(R.id.emergency_phone_number);
        emergency_address = findViewById(R.id.emergency_address);
        swipeRefreshLayout = findViewById(R.id.refresh);
        image =findViewById(R.id.image);
        toolbar = findViewById(R.id.post_toolbar);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
            //The key argument here must match that used in the other activity
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
            toolbar.setTitle("BD Police");
            toolbar.setSubtitle("(Discipline Security Progress)");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //finish();
                    onBackPressed();
                }
            });
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadview();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },900);
            }
        });
        loadview();
    }

    protected void  loadview()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlAddress.ROOT_URI+"getdetailspeople.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                //adding the product to product list
                                name.setText(product.getString("name"));
                                father_name.setText(product.getString("father_name"));
                                current_address.setText(product.getString("current_address"));
                                permanent_address.setText(product.getString("permanent_address"));
                                birth_date.setText(product.getString("birth"));
                                gender.setText(product.getString("gender"));
                                religion.setText(product.getString("religion"));
                                married.setText(product.getString("merried"));
                                phone_number.setText(product.getString("phonenumber"));
                                email_id.setText(product.getString("email_id"));
                                nid_no.setText(product.getString("nid_no"));
                                passport_no.setText(product.getString("passport_no"));
                                emergency_name.setText(product.getString("e_name"));
                                emergency_relation.setText(product.getString("e_relation"));
                                emergency_phone_number.setText(product.getString("e_phone"));
                                emergency_address.setText(product.getString("e_address"));
                                Glide.with(getApplicationContext())
                                        .load(UrlAddress.ROOT_ImageURI+product.getString("image"))
                                        .into(image);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(
                                getApplicationContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
                return params;
            }

        };

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}