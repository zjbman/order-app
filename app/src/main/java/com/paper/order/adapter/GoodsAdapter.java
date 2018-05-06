package com.paper.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.paper.order.R;
import com.paper.order.config.WebParam;
import com.paper.order.data.GoodsData;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zjbman
 * on 2018/3/1.
 */

public class GoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<GoodsData> goodsDatas;

    public GoodsAdapter(Context context,List<GoodsData> goodsDatas){
        mContext = context;
        this.goodsDatas = goodsDatas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(View.inflate(mContext, R.layout.fragment_goods_list,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            ((MyViewHolder)holder).setData(position);
        }
    }

    @Override
    public int getItemCount() {
        return goodsDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private View itemView;

        @Bind(R.id.ll_goods)
        LinearLayout ll_goods;

        @Bind(R.id.iv_icon)
        ImageView iv_icon;
        @Bind(R.id.tv_good_name)
        TextView tv_good_name;
        @Bind(R.id.tv_details)
        TextView tv_details;
        @Bind(R.id.tv_price)
        TextView tv_price;

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
            final GoodsData goodsData = goodsDatas.get(position);
            tv_good_name.setText(goodsData.getGoodsName());
            tv_details.setText(goodsData.getDetail());
            tv_price.setText(goodsData.getPrice() + "");

            Glide.with(mContext).load(WebParam.PIC_BASE_URL + goodsData.getPicture())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.icon)//加载时的图片
                    .error(R.mipmap.icon)  //加载错误时的图片
                    .override(800, 800)
                    .into(iv_icon);

            tv_number.setText("0");

            /* 减号*/
            btn_decrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onBtnDecreaseClickListener != null){
                        onBtnDecreaseClickListener.onClick(position,tv_number,goodsData.getPrice());
                    }
                }
            });

             /* 加号*/
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onBtnAddClickListener != null){
                        onBtnDecreaseClickListener.onClick(position,tv_number,goodsData.getPrice());
                    }
                }
            });
        }
    }


    private OnBtnDecreaseClickListener onBtnDecreaseClickListener;
    private OnBtnAddClickListener onBtnAddClickListener;

    public interface OnBtnDecreaseClickListener{
        void onClick(int position,TextView tv_number,Double price);
    }

    public interface OnBtnAddClickListener{
        void onClick(int position,TextView tv_number,Double price);
    }

    public void setOnBtnDecreaseClickListener(OnBtnDecreaseClickListener onBtnDecreaseClickListener){
        this.onBtnDecreaseClickListener = onBtnDecreaseClickListener;
    }

    public void setOnBtnAddClickListener(OnBtnAddClickListener onBtnAddClickListener){
        this.onBtnAddClickListener = onBtnAddClickListener;
    }
}
