package com.example.user.searchbooks;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;

import java.util.List;

/**
 * Created by user on 11-07-2018.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private List<String> mauthername;
    private List<String> animalImages;
    private List<String> mprize;
    private List<String> msold;
    Context context;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, List<String> data,List<String> authername,List<String> prize,List<String> animalImages,List<String> sold) {
        this.mInflater = LayoutInflater.from(context);
        this.context=context;
        this.mData = data;
        this.animalImages=animalImages;
        this.mprize = prize;
        this.msold = sold;
        this.mauthername=authername;



    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String animal = mData.get(position);
        holder.myTextView.setText("BOOK NAME : "+animal);
        Glide.with(context).load(animalImages.get(position)).into(holder.imageView);
        String prize = mprize.get(position);
        holder.myTextView1.setText("PRIZE (IN RS) : "+prize);
        String auther=mauthername.get(position);
        holder.myTextView2.setText("AUTHERNAME : "+auther);
        String sold=msold.get(position);
        holder.myTextView3.setText(sold+" ");
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView,priceText;
        TextView myTextView1;
        TextView myTextView2;
        TextView myTextView3;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvAnimalName);
            imageView=itemView.findViewById(R.id.iv);
            myTextView1 = itemView.findViewById(R.id.priceTv);
            myTextView2 = itemView.findViewById(R.id.authername);
            myTextView3 = itemView.findViewById(R.id.tvsold);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
     void setClickListener(ItemClickListener itemClickListener) {
       // this.mClickListener = itemClickListener;
        this.mClickListener=itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
