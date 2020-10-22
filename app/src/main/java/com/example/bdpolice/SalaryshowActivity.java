package com.example.bdpolice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalaryshowActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView sms;
    Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    List<salarydata> salarydataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salaryshow);

        getSupportActionBar().hide();
        recyclerView=findViewById(R.id.recyclerView);
        sms = findViewById(R.id.internettext);
        swipeRefreshLayout = findViewById(R.id.refresh);
        toolbar = findViewById(R.id.post_toolbar);
        salarydataList = new ArrayList<>();
        loadview();
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
    }

    void  loadrecycleview()
    {
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        salarydataList.clear();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlAddress.ROOT_URI+"salaryget.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
//                            Toast.makeText(getActivity(),"done",Toast.LENGTH_LONG).show();

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                //adding the product to product list
                                salarydataList.add(new salarydata(
                                        product.getString("id"),
                                        product.getString("name"),
                                        product.getString("salary"),
                                        product.getString("month"),
                                        product.getString("year"),
                                        product.getString("date")
                                ));
//                                 Toast.makeText(getActivity(),"done2",Toast.LENGTH_LONG).show();
                            }

                            //creating adapter object and setting it to recyclerview
                            SalarydataAdapter adapter = new SalarydataAdapter(getApplicationContext(), salarydataList);
                            recyclerView.setAdapter(adapter);
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
                params.put("id",SharedPrefManager.getInstance(getApplicationContext()).getUserid());
                return params;
            }

        };

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    void loadview(){
        if (!isConnected(getApplicationContext())){
            sms.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            sms.setText("Connection Check");
        }
        else {
            sms.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            loadrecycleview();

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