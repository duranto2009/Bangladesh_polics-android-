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

public class SalarydataAdapter extends RecyclerView.Adapter<SalarydataAdapter.ProductViewHolder> {


    private Context mCtx;
    private List<salarydata> salarydataList;

    public SalarydataAdapter(Context mCtx, List<salarydata> salarydataList) {
        this.mCtx = mCtx;
        this.salarydataList = salarydataList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.salarydata_list, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        final salarydata salarydata = salarydataList.get(position);

        //loading the image
        Glide.with(mCtx)
                .load(UrlAddress.ROOT_ImageURI+SharedPrefManager.getInstance(mCtx).getUserimage())
                .into(holder.imageView);
        holder.textViewname.setText(salarydata.getName());
        holder.textViewid.setText(salarydata.getId());
        holder.textViewdate.setText(salarydata.getMonth()+"-"+salarydata.getYear());
        holder.textViewshortdesc.setText("Paid date : "+salarydata.getDate());
        holder.salary.setText("Salary : "+salarydata.getSalary());

    }

    @Override
    public int getItemCount() {
        return salarydataList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewname, textViewid, textViewdate, textViewshortdesc,salary;
        ImageView imageView;
        RelativeLayout relativeLayout;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewname = itemView.findViewById(R.id.textViewName);
            textViewid= itemView.findViewById(R.id.textViewId);
            textViewdate = itemView.findViewById(R.id.textViewDate);
            textViewshortdesc = itemView.findViewById(R.id.textViewpaiddate);
            salary = itemView.findViewById(R.id.textViewsalary);
            imageView = itemView.findViewById(R.id.imageView);
            relativeLayout =itemView.findViewById(R.id.relativelayout);
        }
    }
}
