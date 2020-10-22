package com.example.bdpolice;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


public class profileFragment extends Fragment {

    TextView name;
    TextView id;
    TextView rank;
    ImageView image;
    Button salary;
    Button notice;
    Button logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        name = view.findViewById(R.id.name);
        id = view.findViewById(R.id.id);
        rank = view.findViewById(R.id.rank);
        image= view.findViewById(R.id.profile_image);
        salary =  view.findViewById(R.id.salary);
        notice= view.findViewById(R.id.notice);
        logout = view.findViewById(R.id.logout);
        id.setText(SharedPrefManager.getInstance(getContext()).getUserid());
        rank.setText(SharedPrefManager.getInstance(getContext()).getUserrank());
        name.setText(SharedPrefManager.getInstance(getContext()).getUsername());
        Glide.with(getContext())
                .load(UrlAddress.ROOT_ImageURI+SharedPrefManager.getInstance(getContext()).getUserimage())
                .into(image);
        salary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SalaryshowActivity.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager.getInstance(getContext()).logout();
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);

            }
        });
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),NoticeallActivity.class);
                startActivity(intent);

            }
        });

        return view;
    }
}