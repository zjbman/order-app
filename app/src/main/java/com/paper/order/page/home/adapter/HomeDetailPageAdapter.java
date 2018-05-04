package com.paper.order.page.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.paper.order.R;

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

    List<String> titles;

    public HomeDetailPageAdapter(Context context,List<String> titles){
        mContext = context;
        this.titles = titles;
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
        return mHeaderView == null ? titles.size() : titles.size() + 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView icon;
        private TextView title;
        private TextView detail;
        private TextView price;
        private View itemView;

        public MyViewHolder(View itemView) {
            super(itemView);

            if(itemView == mHeaderView){
                return;
            }
            this.itemView = itemView;
            icon = itemView.findViewById(R.id.iv_icon);
            title = itemView.findViewById(R.id.tv_title);
            detail = itemView.findViewById(R.id.tv_detail);
            price = itemView.findViewById(R.id.tv_price);

        }

        public void setData(final int position){
            title.setText(titles.get(position));
            icon.setBackgroundResource(R.mipmap.icon);
            detail.setText("df");
            price.setText("232");

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
