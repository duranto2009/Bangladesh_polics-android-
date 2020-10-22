package com.example.bdpolice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class DetailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView name;
    TextView age;
    TextView gender;
    TextView recover;
    TextView need_revover;
    TextView arrest;
    TextView case_id;
    TextView case_subject;
    TextView case_details;
    ImageView image1;
    ImageView image2;
    ImageView image3;
    RelativeLayout relativeLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    String id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().hide();
        name = findViewById(R.id.name);
        age = findViewById(R.id.Age);
        gender = findViewById(R.id.gender);
        recover = findViewById(R.id.recover);
        need_revover = findViewById(R.id.need_recover);
        arrest = findViewById(R.id.arrest);
        case_id = findViewById(R.id.caseid);
        case_subject = findViewById(R.id.Case_subject);
        case_details = findViewById(R.id.Case_details);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        relativeLayout = findViewById(R.id.relativelayout);
        swipeRefreshLayout = findViewById(R.id.refresh);
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlAddress.ROOT_URI+"getdetailscriminal.php",
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

                                product.getString("name");
                                name.setText(product.getString("name"));
                                age.setText(product.getString("age"));
                                gender.setText(product.getString("gender"));
                                recover.setText(product.getString("recover"));
                                need_revover.setText(product.getString("need_recover"));
                                arrest.setText(product.getString("arrest"));
                                case_id.setText(product.getString("case_id"));
                                case_subject.setText(product.getString("case_subject"));
                                case_details.setText(product.getString("case_details"));
                                Glide.with(getApplicationContext())
                                        .load(UrlAddress.ROOT_ImageURI+product.getString("image1"))
                                        .into(image1);
                                Glide.with(getApplicationContext())
                                        .load(UrlAddress.ROOT_ImageURI+product.getString("image2"))
                                        .into(image2);
                                Glide.with(getApplicationContext())
                                        .load(UrlAddress.ROOT_ImageURI+product.getString("image3"))
                                        .into(image3);
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