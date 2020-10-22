package com.example.bdpolice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private Spinner spinnermenu;
    String menutext[] = {"Criminal","Missing","Person","Officer"};
    ArrayAdapter <String> adapterspring;
    private Bitmap bitmap=null;
    private Uri mImageUri;
    Fragment selectedFragment =null;
    String spinnertext;
    String id="";
    String Urllink=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        bottomNavigationView= findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        toolbar = findViewById(R.id.post_toolbar);
        spinnermenu= findViewById(R.id.spinner);
        adapterspring = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,menutext);
        spinnermenu.setAdapter(adapterspring);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setTitle("BD Police");
            toolbar.setSubtitle("(Discipline Security Progress)");

        }
        if (!SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn())
        {
            Intent intent = new Intent(getApplication(),LoginActivity.class);
            startActivity(intent);
            finish();
        }


        spinnertext = spinnermenu.getSelectedItem().toString();
        Bundle bundle = new Bundle();
        bundle.putString("value", spinnertext);
        bundle.putString("id", id);
        // set Fragmentclass Arguments
        selectedFragment = new HomeFragment();
        selectedFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment).commit();

        spinnermenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextSize(10);

                switch (i)
                {
                    case 0:
                        if(bitmap != null){
                            Urllink=UrlAddress.ROOT_URI2+"Criminal";
                            getid();
                            spinnertext = spinnermenu.getSelectedItem().toString();
                            Bundle bundle1 = new Bundle();
                            bundle1.putString("value", spinnertext);
                            bundle1.putString("id", id);
                            // set Fragmentclass Arguments
                            selectedFragment = new HomeFragment();
                            selectedFragment.setArguments(bundle1);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    selectedFragment).commit();
                        }
                        break;
                    case 1:
                        if(bitmap != null){
                            Urllink=UrlAddress.ROOT_URI2+"Missing";
                            getid();
                            spinnertext = spinnermenu.getSelectedItem().toString();
                            Bundle bundle4 = new Bundle();
                            bundle4.putString("value", spinnertext);
                            bundle4.putString("id", id);
                            // set Fragmentclass Arguments
                            selectedFragment = new HomeFragment();
                            selectedFragment.setArguments(bundle4);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    selectedFragment).commit();
                        }
                        break;
                    case 2:
                        if(bitmap != null){
                            Urllink=UrlAddress.ROOT_URI2+"Person";
                            getid();
                            spinnertext = spinnermenu.getSelectedItem().toString();
                            Bundle bundle2 = new Bundle();
                            bundle2.putString("value", spinnertext);
                            bundle2.putString("id", id);
                            // set Fragmentclass Arguments
                            selectedFragment = new HomeFragment();
                            selectedFragment.setArguments(bundle2);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    selectedFragment).commit();
                        }
                        break;
                    case 3:
                        if(bitmap != null){
                            Urllink=UrlAddress.ROOT_URI2+"Officer";
                            getid();
                            spinnertext = spinnermenu.getSelectedItem().toString();
                            Bundle bundle3 = new Bundle();
                            bundle3.putString("value", spinnertext);
                            bundle3.putString("id", id);
                            // set Fragmentclass Arguments
                            selectedFragment = new HomeFragment();
                            selectedFragment.setArguments(bundle3);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    selectedFragment).commit();
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode==RESULT_OK) {

            this.getContentResolver().notifyChange(mImageUri, null);
            ContentResolver cr = this.getContentResolver();
            try
            {
                bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
            }
            catch (Exception e)
            {
                Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
            }
            spinnertext=spinnermenu.getSelectedItem().toString();
            Urllink=UrlAddress.ROOT_URI2+spinnertext;

            getid();
        }
    }
    public  void getid()
    {
//        Toast.makeText(
//                getApplicationContext(),
//                Urllink,
//                Toast.LENGTH_LONG
//        ).show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Urllink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error")){
                                spinnertext = spinnermenu.getSelectedItem().toString();
                                Bundle bundle4 = new Bundle();
                                bundle4.putString("value", spinnertext);
                                id=obj.getString("result");
//                                Toast.makeText(
//                                        getApplicationContext(),
//                                        id,
//                                        Toast.LENGTH_LONG
//                                ).show();
                                bundle4.putString("id", id);
                                // set Fragmentclass Arguments
                                selectedFragment = new HomeFragment();
                                selectedFragment.setArguments(bundle4);
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                        selectedFragment).commit();

                            }
                            else{
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Some thing wrong",
                                        Toast.LENGTH_LONG
                                ).show();
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
                params.put("billal",imageToString(bitmap));
                return params;
            }

        };

        RequestHandler.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
        bottomNavigationView.getMenu().findItem(R.id.page_1).setChecked(true);
    }
    public   String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imagbytes = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imagbytes,Base64.DEFAULT);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.page_1:
                            spinnermenu.setVisibility(View.VISIBLE);
                            spinnertext = spinnermenu.getSelectedItem().toString();
                            if(bitmap != null){
                                Urllink=UrlAddress.ROOT_URI2+spinnertext;

                            }
                            Bundle bundle = new Bundle();
                            bundle.putString("value",spinnertext);
                            bundle.putString("id",id);
                            // set Fragmentclass Arguments
                            selectedFragment = new HomeFragment();
                            selectedFragment.setArguments(bundle);
                            break;
                        case R.id.page_2:
//                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            startActivityForResult(intent,100);
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            File photo;
                            try
                            {
                                // place where to store camera taken picture
                                photo = createTemporaryFile("picture", ".jpg");
                                photo.delete();
                            }
                            catch(Exception e)
                            {
                                Toast.makeText(MainActivity.this, "Please check SD card! Image shot is impossible!",Toast.LENGTH_LONG);                                return false;
                            }
                            mImageUri = Uri.fromFile(photo);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                            //start camera intent
                            startActivityForResult(intent,100);
                            break;
                        case R.id.page_3:
                            spinnermenu.setVisibility(View.INVISIBLE);
                            selectedFragment = new profileFragment();
                            break;

                    }
                   if (selectedFragment != null)
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();
                    }
                    return true;
                }

            };

    private File createTemporaryFile(String part, String ext) throws Exception
    {
        File tempDir= Environment.getExternalStorageDirectory();
        tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
        if(!tempDir.exists())
        {
            tempDir.mkdirs();
        }
        return File.createTempFile(part, ext, tempDir);
    }
}