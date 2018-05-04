package com.paper.order.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.paper.order.R;
import com.paper.order.activity.adapter.OrderSubmitAdapter;
import com.paper.order.app.MyApplication;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrderSubmitActivity extends AppCompatActivity {
    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_submit);
        (((MyApplication) getApplication()).activityManager).addActivity(this);
        initView();
    }

    private void initView() {
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        List<String> titles = new ArrayList<>();
        for(int i = 0;i < 2;i++){
            titles.add("" + i);
        }

        OrderSubmitAdapter adapter = new OrderSubmitAdapter(this,titles);
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onDestroy() {
        (((MyApplication) getApplication()).activityManager).removeActivity(this);
        super.onDestroy();
    }
}
