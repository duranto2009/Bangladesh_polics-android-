package com.example.bdpolice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class NoticeShowActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private TextView Date,Subject,Details;
    private String date,subject,details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_show);
        getSupportActionBar().hide();
        toolbar = findViewById(R.id.post_toolbar);
        Date = findViewById(R.id.date);
        Subject = findViewById(R.id.subject);
        Details = findViewById(R.id.details);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            date = extras.getString("date");
            subject = extras.getString("subject");
            details = extras.getString("details");
            Date.setText(date);
            Subject.setText(subject);
            Details.setText(details);
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
    }
}