package com.paper.order.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.paper.order.R;
import com.paper.order.config.WebParam;
import com.paper.order.data.GoodsData;
import com.paper.order.data.GoodsDataForOrderSubmit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zjbman
 * on 2018/3/2.
 */

public class OrderSubmitAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<GoodsDataForOrderSubmit> GoodsDataForOrderSubmitList;

    public OrderSubmitAdapter(Context context, List<GoodsDataForOrderSubmit> GoodsDataForOrderSubmitList){
        mContext = context;
        this.GoodsDataForOrderSubmitList = GoodsDataForOrderSubmitList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(View.inflate(mContext, R.layout.activity_order_submit_recyclerview_item,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            try {
                (((MyViewHolder) holder)).setData(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return GoodsDataForOrderSubmitList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.iv_icon)
        ImageView iv_icon;
        @Bind(R.id.tv_title)
        TextView tv_title;
        @Bind(R.id.tv_price)
        TextView tv_price;
        @Bind(R.id.tv_number)
        TextView tv_number;
        @Bind(R.id.tv_accounts)
        TextView tv_accounts;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(int position) throws JSONException {
            GoodsDataForOrderSubmit goodsDataForOrderSubmit = GoodsDataForOrderSubmitList.get(position);

            Glide.with(mContext).load(WebParam.PIC_BASE_URL + goodsDataForOrderSubmit.getBusinessPicture())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.icon)//加载时的图片
                    .error(R.mipmap.icon)  //加载错误时的图片
                    .override(800, 800)
                    .into(iv_icon);
            tv_title.setText(goodsDataForOrderSubmit.getGoodsName());
            tv_price.setText(goodsDataForOrderSubmit.getPrice() + "");
            tv_number.setText(goodsDataForOrderSubmit.getGoodsNumber() + "");
            tv_accounts.setText(goodsDataForOrderSubmit.getPrice() * goodsDataForOrderSubmit.getGoodsNumber() + "");
        }
    }
}
