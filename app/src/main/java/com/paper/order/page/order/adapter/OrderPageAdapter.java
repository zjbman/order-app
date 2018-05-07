package com.paper.order.page.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.paper.order.R;
import com.paper.order.config.WebParam;
import com.paper.order.data.OrderData;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zjbman
 * on 2018/2/28.
 */

public class OrderPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<OrderData> orderDataList;
    private OnAgainButtonClickListener onAgainButtonClickListener;

    public OrderPageAdapter(Context context,List<OrderData> orderDataList){
        mContext = context;
        this.orderDataList = orderDataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(View.inflate(mContext, R.layout.page_order_recyclerview,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            ((MyViewHolder)holder).setData(position);
        }
    }

    @Override
    public int getItemCount() {
        return orderDataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private View itemView;

        @Bind(R.id.iv_icon)
        ImageView iv_icon;
        @Bind(R.id.tv_title)
        TextView tv_title;
        @Bind(R.id.tv_date)
        TextView tv_date;
        @Bind(R.id.tv_total_price)
        TextView tv_total_price;
        @Bind(R.id.btn_again)
        Button btn_again;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this,itemView);
        }

        public void setData(final int position){
            OrderData orderData = orderDataList.get(position);
            Glide.with(mContext).load(WebParam.PIC_BASE_URL + orderData.getBusinessPicture())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.icon)//加载时的图片
                    .error(R.mipmap.icon)  //加载错误时的图片
                    .override(800, 800)
                    .into(iv_icon);
            tv_title.setText(orderData.getBusinessName());
            tv_date.setText(orderData.getDate());
            tv_total_price.setText(orderData.getPrice() + "");


            btn_again.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onAgainButtonClickListener != null){
                        onAgainButtonClickListener.onClick(position);
                    }
                }
            });
        }
    }

    public void setOnAgainButtonClickListener(OnAgainButtonClickListener onAgainButtonClickListener){
        this.onAgainButtonClickListener = onAgainButtonClickListener;
    }

    public interface OnAgainButtonClickListener{
        void onClick(int position);
    }
}
