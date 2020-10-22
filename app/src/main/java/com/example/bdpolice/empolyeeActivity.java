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

public class empolyeeActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView name;
    TextView rank;
    TextView batch_id;
    TextView batch_no;
    TextView phone_number;
    TextView email_id;
    TextView birth_date;
    TextView gender;
    TextView current_address;
    TextView permanent_address;
    TextView unite;
    TextView fair;
    TextView fair_date;
    TextView suspend;
    TextView suspend_date;
    ImageView image;
    private SwipeRefreshLayout swipeRefreshLayout;
    String id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empolyee);

        getSupportActionBar().hide();
        name = findViewById(R.id.name);
        rank = findViewById(R.id.rank);
        batch_id = findViewById(R.id.batch_id);
        batch_no = findViewById(R.id.batch_no);
        phone_number = findViewById(R.id.phone_number);
        email_id = findViewById(R.id.email_id);
        birth_date = findViewById(R.id.birth_date);
        gender = findViewById(R.id.gender);
        current_address = findViewById(R.id.current_address);
        permanent_address = findViewById(R.id.permanent_address);
        unite = findViewById(R.id.unite);
        fair = findViewById(R.id.fair);
        fair_date = findViewById(R.id.fair_date);
        suspend = findViewById(R.id.suspend);
        suspend_date = findViewById(R.id.suspend_date);
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlAddress.ROOT_URI+"getdetailsemployee.php",
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
                                rank.setText(product.getString("rank"));
                                batch_id.setText(product.getString("batch_id"));
                                batch_no.setText(product.getString("batchno"));
                                phone_number.setText(product.getString("phonenumber"));
                                email_id.setText(product.getString("email"));
                                birth_date.setText(product.getString("birth"));
                                gender.setText(product.getString("gender"));
                                current_address.setText(product.getString("current_address"));
                                permanent_address.setText(product.getString("permanent_address"));
                                unite.setText(product.getString("unite"));
                                fair.setText(product.getString("fair"));
                                fair_date.setText(product.getString("fair_date"));
                                suspend.setText(product.getString("suspend"));
                                suspend_date.setText(product.getString("suspend_date"));
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