package com.paper.order.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.paper.order.R;
import com.paper.order.activity.OrderSubmitActivity;
import com.paper.order.activity.adapter.GoodsAdapter;
import com.paper.order.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zjbman
 * on 2018/3/1.
 */

public class GoodsFragment  extends Fragment {
    private Context mContext;
    private View view;

    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;
    @Bind(R.id.tv_goods_number)
    TextView tv_goods_number;
    @Bind(R.id.tv_accounts)
    TextView tv_accounts;
    @Bind(R.id.btn_compute)
    Button btn_compute;

    private GoodsAdapter adapter;

    public GoodsFragment(Context context){
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_goods,null);
        ButterKnife.bind(this,view);
        setAdapter();
        setListener();
        return view;
    }

    private void setAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        List<String> names = new ArrayList<>();
        for(int i = 0;i < 10;i++){
            names.add("" + i);
        }
        adapter = new GoodsAdapter(mContext,names);
        recyclerView.setAdapter(adapter);
    }

    private void setListener() {
        btn_compute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, OrderSubmitActivity.class));
            }
        });

        adapter.setOnRecyclerViewItemClickListener(new GoodsAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, View itemView) {
                ToastUtil.show(mContext,"" + position);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
