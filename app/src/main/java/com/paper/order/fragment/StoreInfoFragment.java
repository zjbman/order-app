package com.paper.order.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paper.order.R;

/**
 * Created by zjbman
 * on 2018/3/1.
 */

@SuppressLint("ValidFragment")
public class StoreInfoFragment extends Fragment{
    private Context mContext;
    private View view;

    private TextView tv_address;
    private TextView tv_phone;

    private String businessAddress;
    private String businessTelephone;

    @SuppressLint("ValidFragment")
    public StoreInfoFragment(Context context, int businessId,String businessAddress,String businessTelephone){
        mContext = context;
        this.businessTelephone =  businessTelephone;
        this.businessAddress =  businessAddress;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_storeinfo,null);
        tv_address = view.findViewById(R.id.tv_address);
        tv_phone = view.findViewById(R.id.tv_phone);

        tv_address.setText(businessAddress);
        tv_phone.setText(businessTelephone);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
