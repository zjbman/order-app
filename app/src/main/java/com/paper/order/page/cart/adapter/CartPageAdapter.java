package com.paper.order.page.cart.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.paper.order.R;
import com.paper.order.config.WebParam;
import com.paper.order.data.CartData;
import com.paper.order.data.CartOneData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zjbman
 * on 2018/2/28.
 */

public class CartPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<CartOneData> cartDataList;

    /** 这是记录当前每一个item有多少数量的map*/
    private Map<Integer,Integer> numberMap = new HashMap<>();

    public CartPageAdapter(Context context, List<CartOneData> cartDataList,Map<Integer,Integer> numberMap){
        mContext = context;
        this.cartDataList = cartDataList;
        this.numberMap = numberMap;
    }

    /** 当商品数量发生改变时就通知适配器，固定商品数量，使之不在重新绘制页面的时候被重置*/
    public void notify(Map<Integer,Integer> numberMap){
        this.numberMap = numberMap;
        /*刷新适配器*/
        notifyDataSetChanged();
    }

    /** 移除条目时 通知同步适配器*/
    public void notifyCartDataList(List<CartOneData> cartOneData){
        this.cartDataList = cartOneData;
        /*刷新适配器*/
        notifyDataSetChanged();
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
        return cartDataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private View itemView;

        @Bind(R.id.tv_business_name)
        TextView tv_business_name;
        @Bind(R.id.tv_delete)
        TextView tv_delete;

        @Bind(R.id.iv_goods_picture)
        ImageView iv_goods_picture;
        @Bind(R.id.tv_goods_name)
        TextView tv_goods_name;
        @Bind(R.id.tv_goods_details)
        TextView tv_goods_details;
        @Bind(R.id.tv_goods_price)
        TextView tv_goods_price;
        @Bind(R.id.btn_decrease)
        ImageButton btn_decrease;
        @Bind(R.id.btn_add)
        ImageButton btn_add;
        @Bind(R.id.tv_number)
        TextView tv_number;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this,itemView);
        }

        public void setData(final int position){
            final CartOneData cartData = cartDataList.get(position);

            tv_business_name.setText(cartData.getBusinessName());
            Glide.with(mContext).load(WebParam.PIC_BASE_URL + cartData.getPicture())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.icon)//加载时的图片
                    .error(R.mipmap.icon)  //加载错误时的图片
                    .override(800, 800)
                    .into(iv_goods_picture);
            tv_goods_name.setText(cartData.getGoodsName());
            tv_goods_details.setText(cartData.getDetails());
            tv_goods_price.setText(cartData.getPrice() + "");



            if(numberMap.containsKey(position)){
                tv_number.setText(numberMap.get(position) + "");
            }else{
                tv_number.setText("0");
            }

            /* 减号*/
            btn_decrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onBtnDecreaseClickListener != null){
                        onBtnDecreaseClickListener.onClick(position,tv_number,cartData.getPrice());
                    }
                }
            });

             /* 加号*/
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onBtnAddClickListener != null){
                        onBtnAddClickListener.onClick(position,tv_number,cartData.getPrice());
                    }
                }
            });

            /* 移除*/
            tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onDeleteClickListener != null){
                        onDeleteClickListener.onClick(position,tv_number);
                    }
                }
            });
        }
    }



    private OnBtnDecreaseClickListener onBtnDecreaseClickListener;
    private OnBtnAddClickListener onBtnAddClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnBtnDecreaseClickListener{
        void onClick(int position,TextView tv_number,Double price);
    }

    public interface OnBtnAddClickListener{
        void onClick(int position,TextView tv_number,Double price);
    }

    public interface OnDeleteClickListener{
        void onClick(int position,TextView tv_number);
    }

    public void setOnBtnDecreaseClickListener(OnBtnDecreaseClickListener onBtnDecreaseClickListener){
        this.onBtnDecreaseClickListener = onBtnDecreaseClickListener;
    }

    public void setOnBtnAddClickListener(OnBtnAddClickListener onBtnAddClickListener){
        this.onBtnAddClickListener = onBtnAddClickListener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener){
        this.onDeleteClickListener = onDeleteClickListener;
    }
}
