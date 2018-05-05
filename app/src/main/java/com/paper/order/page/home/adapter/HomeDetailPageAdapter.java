package com.paper.order.page.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.paper.order.R;
import com.paper.order.config.WebParam;
import com.paper.order.data.BusinessData;

import java.util.List;

/**
 * Created by zjbman
 * on 2018/2/27.
 */

public class HomeDetailPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private View mHeaderView;
    private final int ITEM_BANNER = 0;
    private final int ITEM_LIST = 1;

    private OnListItemClickListener onListItemClickListener;

    private List<BusinessData> businessDatas;

    public HomeDetailPageAdapter(Context context,List<BusinessData> businessDatas){
        mContext = context;
        this.businessDatas = businessDatas;
    }

    public void setHeaderView(View headerView){
        mHeaderView = headerView;
    }

    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null){
            return ITEM_LIST;
        }
        if(position == 0){
            return ITEM_BANNER;
        }
        return ITEM_LIST;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == ITEM_BANNER){
            return new MyViewHolder(mHeaderView);
        }
        return new MyViewHolder(View.inflate(mContext, R.layout.page_home_recyclerview_list,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == ITEM_BANNER){
            return;
        }
        final int index = getListRealPosition(holder);
        if(holder instanceof MyViewHolder){
            ((MyViewHolder)holder).setData(index);
        }

    }

    private int getListRealPosition(RecyclerView.ViewHolder holder){
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? businessDatas.size() : businessDatas.size() + 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView icon;
        private TextView tv_business_name;
        private TextView tv_business_address;
        private View itemView;

        public MyViewHolder(View itemView) {
            super(itemView);

            if(itemView == mHeaderView){
                return;
            }
            this.itemView = itemView;
            icon = itemView.findViewById(R.id.iv_icon);
            tv_business_name = itemView.findViewById(R.id.tv_business_name);
            tv_business_address = itemView.findViewById(R.id.tv_business_address);

        }

        @SuppressLint("SetTextI18n")
        public void setData(final int position){
            tv_business_name.setText("店名：" + businessDatas.get(position).getBusinessName());
            tv_business_address.setText("地址：" + businessDatas.get(position).getAddress());
            String picUrl = WebParam.PIC_BASE_URL + businessDatas.get(position).getPicture();
            Glide.with(mContext).load(picUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.icon)//加载时的图片
                    .error(R.mipmap.icon)  //加载错误时的图片
                    .override(800, 800)
                    .into(icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onListItemClickListener != null){
                        onListItemClickListener.onListItemClick(view,position);
                    }
                }
            });
        }
    }


    public interface OnListItemClickListener{
        void onListItemClick(View view,int position);
    }

    public void setOnListItemClickListener(OnListItemClickListener onListItemClickListener){
        this.onListItemClickListener = onListItemClickListener;
    }
}
