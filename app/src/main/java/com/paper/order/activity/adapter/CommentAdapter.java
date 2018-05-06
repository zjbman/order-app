package com.paper.order.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paper.order.R;
import com.paper.order.data.CommentData;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zjbman
 * on 2018/3/1.
 */

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<CommentData> commentDatas;

    public CommentAdapter(Context context, List<CommentData> commentDatas) {
        mContext = context;
        this.commentDatas = commentDatas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(commentDatas == null || commentDatas.size() < 1){
            return new MyViewHolder1(View.inflate(mContext, R.layout.fragment_comment_null, null));
        }
        return new MyViewHolder(View.inflate(mContext, R.layout.fragment_comment_list, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).setData(position);
        }
    }

    @Override
    public int getItemCount() {
        return commentDatas.size();
    }

    class MyViewHolder1 extends RecyclerView.ViewHolder {
        public MyViewHolder1(View itemView) {
            super(itemView);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_username)
        TextView tv_username;
        @Bind(R.id.tv_content)
        TextView tv_content;
        @Bind(R.id.tv_date)
        TextView tv_date;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final int position) {
            CommentData commentData = commentDatas.get(position);
            tv_username.setText(commentData.getUserName());
            tv_content.setText(commentData.getComment());
            tv_date.setText(commentData.getDate());
        }
    }
}



