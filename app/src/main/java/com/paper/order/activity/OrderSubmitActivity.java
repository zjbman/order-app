package com.paper.order.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.paper.order.R;
import com.paper.order.activity.base.BaseActivity;
import com.paper.order.adapter.OrderSubmitAdapter;
import com.paper.order.config.WebParam;
import com.paper.order.data.UserData;
import com.paper.order.retrofit.http.MyRetrofit;
import com.paper.order.retrofit.request.GetInterface;
import com.paper.order.retrofit.response.ResponseByUserInfo;
import com.paper.order.retrofit.response.ResponseByUsually;
import com.paper.order.util.SharedpreferencesUtil;
import com.paper.order.util.StringUtil;
import com.paper.order.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderSubmitActivity extends BaseActivity {
    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;
    @Bind(R.id.tv_compute)
    TextView tv_compute;
    @Bind(R.id.tv_accounts)
    TextView tv_accounts;
    @Bind(R.id.et_address)
    EditText et_address;
    @Bind(R.id.tl_remark)
    TextInputLayout tl_remark;
    @Bind(R.id.tl_telephone)
    TextInputLayout tl_telephone;

    private UserData userData;

    private final int REQUEST_USER_SUCCESS = 0;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case REQUEST_USER_SUCCESS:
                    if (checkUserMoney()) {
                        /* 提交订单*/
                        requestHttpForOrder();
                    } else {
                        ToastUtil.show(OrderSubmitActivity.this, "您的余额不足，无法提交订单!");
                    }
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected View setContentView() {
        return View.inflate(this, R.layout.activity_order_submit, null);
    }

    @Override
    protected Activity bindActivity() {
        return this;
    }

    @Override
    protected void initView() {
        getIntent();
        setAdapter();

    }

    private void setAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        List<String> titles = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            titles.add("" + i);
        }

        OrderSubmitAdapter adapter = new OrderSubmitAdapter(this, titles);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
        tv_compute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = et_address.getText().toString();
                String telephone = tl_telephone.getEditText().getText().toString();
                if (StringUtil.isEmpty(address)) {
                    ToastUtil.show(OrderSubmitActivity.this, "请填写配送地址！");
                    return;
                }
                if (StringUtil.isEmpty(telephone)) {
                    ToastUtil.show(OrderSubmitActivity.this, "请填写手机号码！");
                    return;
                }

                openDialog();
            }
        });
    }


    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确定提交订单吗？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /* 查询用户信息，判断余额*/
                requestHttpForUserInfo();
            }
        });

        builder.create().show();
    }

    /**
     * 判断当前用户是否有余额下单
     * true够钱； false不够钱
     */
    private boolean checkUserMoney() {
        if (userData.getMoney() >= Double.parseDouble(tv_accounts.getText().toString())) {
            return true;
        }
        return false;
    }


    public void requestHttpForUserInfo() {
        GetInterface request = MyRetrofit.getInstance().request(WebParam.BASE_URL);
        Map<String, Object> map = new HashMap<>();
        Map<String, String> user = SharedpreferencesUtil.getInstance().getUser(this);
        map.put("username", user.get("username"));
        Call<ResponseByUserInfo> call = request.getUser("Find.html", map);
        call.enqueue(new Callback<ResponseByUserInfo>() {
            @Override
            public void onResponse(Call<ResponseByUserInfo> call, Response<ResponseByUserInfo> response) {
                if (response.body() != null) {
                    userData = response.body().getMsg();
                    handler.sendEmptyMessage(REQUEST_USER_SUCCESS);
                } else {
                    ToastUtil.show(OrderSubmitActivity.this, "查询用户信息失败！ body() == null ");
                }
            }

            @Override
            public void onFailure(Call<ResponseByUserInfo> call, Throwable t) {

            }
        });
    }


    private void requestHttpForOrder() {
        GetInterface request = MyRetrofit.getInstance().request(WebParam.BASE_URL);
        Map<String, Object> map = new HashMap<>();
        map.put("username", userData.getUsername());
        map.put("address", et_address.getText().toString());
        map.put("telephone", tl_telephone.getEditText().getText().toString());
        map.put("remark", tl_remark.getEditText().getText().toString());

        map.put("businessId","" );
        map.put("goodsList", "");

        map.put("price", Double.parseDouble(tv_accounts.getText().toString()));
        Call<ResponseByUsually> call = request.addOrder("Save.html", map);
        call.enqueue(new Callback<ResponseByUsually>() {
            @Override
            public void onResponse(Call<ResponseByUsually> call, Response<ResponseByUsually> response) {
                if (response.body() == null) {
                    ToastUtil.show(OrderSubmitActivity.this, "提交订单失败！ body() == null ");
                } else {
                    int code = response.body().getCode();
                    if (code == -100) {
                        ToastUtil.show(OrderSubmitActivity.this, "提交订单失败！ code == -100 ");
                    } else if (code == 200) {
                        ToastUtil.show(OrderSubmitActivity.this, "下单成功！");
                        startMainActivity();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseByUsually> call, Throwable t) {
                ToastUtil.show(OrderSubmitActivity.this, "连接服务器失败！");
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onRelease() {

    }

}
