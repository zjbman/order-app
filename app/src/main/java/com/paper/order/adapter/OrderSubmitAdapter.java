package com.paper.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.paper.order.R;

import java.util.List;

/**
 * Created by zjbman
 * on 2018/3/2.
 */

public class OrderSubmitAdapter extends RecyclerView.Adapter {
    private Context mContext;

    private List<String> titles;

    public OrderSubmitAdapter(Context context, List<String> titles){
        mContext = context;
        this.titles = titles;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(View.inflate(mContext, R.layout.activity_order_submit_recyclerview_item,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            (((MyViewHolder) holder)).setData(position);
        }
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(View itemView) {
            super(itemView);
        }

        public void setData(int position){

        }
    }
}
