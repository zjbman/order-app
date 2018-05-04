package com.paper.order.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paper.order.R;
import com.paper.order.activity.adapter.CommentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zjbman
 * on 2018/3/1.
 */

public class CommentFragment  extends Fragment {
    private Context mContext;
    private View view;

    private CommentAdapter adapter;

    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;

    public CommentFragment(Context context){
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_comment,null);
        ButterKnife.bind(this,view);
        setListener();
        setAdapter();
        return view;
    }

    private void setAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        List<String> names = new ArrayList<>();
        for(int i = 0;i < 10;i++){
            names.add("" + i);
        }
        adapter = new CommentAdapter(mContext,names);
        recyclerView.setAdapter(adapter);
    }

    private void setListener() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
