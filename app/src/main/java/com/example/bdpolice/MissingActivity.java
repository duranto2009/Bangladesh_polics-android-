package com.example.bdpolice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class MissingActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView name;
    TextView address;
    TextView gender;
    TextView father_name;
    TextView age1;
    TextView missing_date;
    TextView missing_area;
    TextView details;
    ImageView image;
    private SwipeRefreshLayout swipeRefreshLayout;
    String id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing);

        getSupportActionBar().hide();
        name = findViewById(R.id.name);
        age1 = findViewById(R.id.age1);
        gender = findViewById(R.id.gender);
        address = findViewById(R.id.Address);
        father_name = findViewById(R.id.Father_name);
        missing_date = findViewById(R.id.Missing_date);
        missing_area = findViewById(R.id.missing_area);
        details = findViewById(R.id.Missing_details);
        image = findViewById(R.id.image);
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlAddress.ROOT_URI+"getdetailsmissing.php",
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
                                address.setText(product.getString("address"));
                                gender.setText(product.getString("gender"));
                                father_name.setText(product.getString("father_name"));
                                age1.setText(product.getString("age"));
                                missing_date.setText(product.getString("missing_date"));
                                missing_area.setText(product.getString("missing_area"));
                                details.setText(product.getString("details"));
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