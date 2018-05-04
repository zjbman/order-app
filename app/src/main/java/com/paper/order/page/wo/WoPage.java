package com.paper.order.page.wo;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.paper.order.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zjbman
 * on 2018/2/28.
 */

public class WoPage {
    private Context mContext;
    private View mView;
    @Bind(R.id.iv_icon)
    ImageView iv_icon;
    @Bind(R.id.tv_name)
    TextView tv_name;
    @Bind(R.id.tv_money)
    TextView tv_money;
    @Bind(R.id.tv_get)
    TextView tv_get;
    @Bind(R.id.tv_recharge)
    TextView tv_recharge;
    @Bind(R.id.tv_address)
    TextView tv_address;
    @Bind(R.id.tv_info)
    TextView tv_info;
    @Bind(R.id.tv_change_password)
    TextView tv_change_password;
    @Bind(R.id.btn_exit)
    Button btn_exit;


    public WoPage(Context context){
        mContext = context;
        initView();
        setListener();
    }

    public View getView(){
        return mView;
    }

    private void initView() {
        mView = View.inflate(mContext, R.layout.page_wo,null);
        ButterKnife.bind(this,mView);
    }

    private void setListener() {
        iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tv_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tv_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tv_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tv_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tv_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
