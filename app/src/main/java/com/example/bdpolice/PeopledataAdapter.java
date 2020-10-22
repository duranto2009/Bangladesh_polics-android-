package com.example.bdpolice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Belal on 10/18/2017.
 */

public class PeopledataAdapter extends RecyclerView.Adapter<PeopledataAdapter.ProductViewHolder> {


    private Context mCtx;
    private List<peopledata> peopledataList;

    public PeopledataAdapter(Context mCtx, List<peopledata> peopledataList) {
        this.mCtx = mCtx;
        this.peopledataList = peopledataList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.criminaldata_list, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        final peopledata peopledata = peopledataList.get(position);

        //loading the image
        Glide.with(mCtx)
                .load(peopledata.getImage())
                .into(holder.imageView);

        holder.textViewname.setText(peopledata.getName());
        holder.textViewid.setText(peopledata.getId());
        holder.textViewdate.setText(peopledata.getbatch());
        holder.textViewshortdesc.setText(peopledata.getrank());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (peopledata.getValue() == "Criminal")
                {
                    Intent i = new Intent(mCtx,DetailsActivity.class);
                    i.putExtra("id", peopledata.getSerial());
                    mCtx.startActivity(i);
                }
                if (peopledata.getValue() == "Missing")
                {
                    Intent i = new Intent(mCtx,MissingActivity.class);
                    i.putExtra("id", peopledata.getSerial());
                    mCtx.startActivity(i);
                }
                if (peopledata.getValue() == "Person")
                {
                    Intent i = new Intent(mCtx,PeopleActivity.class);
                    i.putExtra("id", peopledata.getSerial());
                    mCtx.startActivity(i);
                }
                if (peopledata.getValue() == "Officer")
                {
                    Intent i = new Intent(mCtx,empolyeeActivity.class);
                    i.putExtra("id", peopledata.getSerial());
                    mCtx.startActivity(i);
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return peopledataList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewname, textViewid, textViewdate, textViewshortdesc;
        ImageView imageView;
        RelativeLayout relativeLayout;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewname = itemView.findViewById(R.id.textViewName);
            textViewid= itemView.findViewById(R.id.textViewId);
            textViewdate = itemView.findViewById(R.id.textViewDate);
            textViewshortdesc = itemView.findViewById(R.id.textViewShotDes);
            imageView = itemView.findViewById(R.id.imageView);
            relativeLayout =itemView.findViewById(R.id.relativelayout);
        }
    }
}
