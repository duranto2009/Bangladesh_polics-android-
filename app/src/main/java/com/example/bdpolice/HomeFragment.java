package com.example.bdpolice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private ImageView imageView;
    private TextView textView;
    private TextView textView1;
    private TextView textView2;
    private TextView internet_text;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    String value;
    String id;
    //this is the JSON Data URL
    //make sure you are using the correct ip else it will not work
    String URL_PRODUCTS = "";

    //a list to store all the products
    List<peopledata> peopledataList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        imageView = view.findViewById(R.id.profile_image);
        textView = view.findViewById(R.id.textView);
        textView1 = view.findViewById(R.id.textView1);
        textView2 = view.findViewById(R.id.textView2);
        internet_text=view.findViewById(R.id.internettext);
        recyclerView = view.findViewById(R.id.recyclerViewpersondetails);
        swipeRefreshLayout = view.findViewById(R.id.refresh);
        textView1.setText(SharedPrefManager.getInstance(getContext()).getUserid());
        textView.setText(SharedPrefManager.getInstance(getContext()).getUsername());
        textView2.setText(SharedPrefManager.getInstance(getContext()).getUserrank());
        Glide.with(getContext())
                .load(UrlAddress.ROOT_ImageURI+SharedPrefManager.getInstance(getContext()).getUserimage())
                .into(imageView);
        peopledataList = new ArrayList<>();

        if (getArguments() != null) {
            value = this.getArguments().getString("value");
            id = this.getArguments().getString("id");
        }
        if (value=="Criminal")
        {
            URL_PRODUCTS = UrlAddress.ROOT_URI+"criminaldataget.php";
        }
        if (value=="Missing")
        {
            URL_PRODUCTS = UrlAddress.ROOT_URI+"missingdataget.php";
        }
        if (value=="Person")
        {
            URL_PRODUCTS = UrlAddress.ROOT_URI+"peopledataget.php";
        }
        if (value=="Officer")
        {
            URL_PRODUCTS = UrlAddress.ROOT_URI+"employeedataget.php";
        }
        loadview();

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
        return view;
    }
    void loadrecycleview()
    {
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        peopledataList.clear();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PRODUCTS,
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
                                peopledataList.add(new peopledata(
                                        product.getString("id"),
                                        product.getString("name"),
                                        product.getString("photo"),
                                        product.getString("rank"),
                                        product.getString("batch"),
                                        product.getString("serial"),
                                        value
                                ));
//                                 Toast.makeText(getActivity(),"done2",Toast.LENGTH_LONG).show();
                            }

                            //creating adapter object and setting it to recyclerview
                            PeopledataAdapter adapter = new PeopledataAdapter(getActivity(), peopledataList);
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
                                getActivity(),
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
                params.put("value",value);
                return params;
            }

        };

        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
    void loadview(){
        if (!isConnected(getActivity())){
            internet_text.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            internet_text.setText("Connection Check");
        }
        else {
            internet_text.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            if (id!="UnKnown" && id!="")
            {
                loadrecycleview();
            }
            if (id=="UnKnown")
            {
                Toast.makeText(getActivity(), "ID not found",Toast.LENGTH_LONG).show();
            }

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