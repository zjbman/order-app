package com.paper.order.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paper.order.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zjbman
 * on 2018/3/1.
 */

public class GoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    List<String> titles;

    public GoodsAdapter(Context context,List<String> titles){
        mContext = context;
        this.titles = titles;
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
        return titles.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private View itemView;

        @Bind(R.id.ll_goods)
        LinearLayout ll_goods;
        @Bind(R.id.iv_icon)
        ImageView iv_icon;
        @Bind(R.id.tv_good_name)
        TextView tv_good_name;
        @Bind(R.id.tv_recommend)
        TextView tv_recommend;
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
            tv_good_name.setText(titles.get(position));

            ll_goods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onRecyclerViewItemClickListener != null){
                        onRecyclerViewItemClickListener.onItemClick(position,itemView);
                    }
                }
            });
        }
    }

    public interface OnRecyclerViewItemClickListener{
        void onItemClick(int position,View itemView);
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener){
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }
}
