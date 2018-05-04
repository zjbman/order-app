package com.paper.order.page.cart.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paper.order.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zjbman
 * on 2018/2/28.
 */

public class CartPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private List<String> data;

    public CartPageAdapter(Context context){
        mContext = context;
        initData();
    }

    private void initData() {
        data = new ArrayList<>();
        for(int i = 0;i < 2;i++){
            data.add("商品" + i);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(View.inflate(mContext, R.layout.page_cart_recyclerview,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            ((MyViewHolder)holder).setData(position);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private View itemView;
        @Bind(R.id.tv_store)
        TextView tv_store;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this,itemView);
        }

        public void setData(final int position){
            tv_store.setText(data.get(position));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onRecyclerViewItemClickListener != null){
                        onRecyclerViewItemClickListener.onItemClick(position,itemView);
                    }
                }
            });
        }
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener){
        onRecyclerViewItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener{
        void onItemClick(int position,View itemView);
    }
}
