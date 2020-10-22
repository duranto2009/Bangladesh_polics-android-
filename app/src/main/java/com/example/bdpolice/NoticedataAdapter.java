package com.example.bdpolice;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Belal on 10/18/2017.
 */

public class NoticedataAdapter extends RecyclerView.Adapter<NoticedataAdapter.ProductViewHolder> {


    private Context mCtx1;
    private List<noticedata> noticedataList;

    public NoticedataAdapter(Context mCtx1, List<noticedata> noticedataList) {
        this.mCtx1 = mCtx1;
        this.noticedataList = noticedataList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx1);
        View view = inflater.inflate(R.layout.noticedata_list, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        final noticedata noticedata = noticedataList.get(position);

        //loading the image

        holder.textViewname.setText(SharedPrefManager.getInstance(mCtx1).getUsername());
        holder.textViewdate.setText("Date : "+noticedata.getDate()+"-"+noticedata.getMonth()+"-"+noticedata.getYear());
        holder.textViewmessage.setText(noticedata.getMessage());
        holder.textViewsubject.setText(noticedata.getSubject());
        Glide.with(mCtx1)
                .load(UrlAddress.ROOT_ImageURI+SharedPrefManager.getInstance(mCtx1).getUserimage())
                .into(holder.imageView);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mCtx1,NoticeShowActivity.class);
                i.putExtra("date", noticedata.getDate()+"-"+noticedata.getMonth()+"-"+noticedata.getYear());
                i.putExtra("subject", noticedata.getSubject());
                i.putExtra("details", noticedata.getMessage());
                mCtx1.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return noticedataList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewname, textViewsubject, textViewdate, textViewmessage;
        ImageView imageView;
        RelativeLayout relativeLayout;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewname = itemView.findViewById(R.id.textViewName);
            textViewsubject= itemView.findViewById(R.id.textViewsubject);
            textViewdate = itemView.findViewById(R.id.textViewDate);
            textViewmessage = itemView.findViewById(R.id.messagetext);
            imageView = itemView.findViewById(R.id.imageView);
            relativeLayout =itemView.findViewById(R.id.relativelayout);
        }
    }
}
