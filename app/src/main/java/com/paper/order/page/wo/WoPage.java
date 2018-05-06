package com.paper.order.page.wo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.paper.order.R;
import com.paper.order.activity.ChangePassWordActivity;
import com.paper.order.activity.LoginActivity;
import com.paper.order.activity.UserInfoActivity;
import com.paper.order.config.WebParam;
import com.paper.order.data.UserData;
import com.paper.order.retrofit.http.MyRetrofit;
import com.paper.order.retrofit.request.GetInterface;
import com.paper.order.retrofit.response.ResponseByUserInfo;
import com.paper.order.util.SharedpreferencesUtil;


import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zjbman
 * on 2018/2/28.
 */

public class WoPage {
//    @Bind(R.id.tv_get)
//    TextView tv_get;
//    @Bind(R.id.tv_recharge)
//    TextView tv_recharge;
//    @Bind(R.id.tv_address)
//    TextView tv_address;

    private Context mContext;
    private View mView;
    @Bind(R.id.iv_icon)
    ImageView iv_icon;
    @Bind(R.id.tv_name)
    TextView tv_name;
    @Bind(R.id.tv_money)
    TextView tv_money;
    @Bind(R.id.tv_info)
    TextView tv_info;
    @Bind(R.id.tv_change_password)
    TextView tv_change_password;
    @Bind(R.id.btn_exit)
    Button btn_exit;

    private Map<String, String> user;
    private UserData userData;

    private final int REQUEST_SUCCESS = 1;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    tv_money.setText(userData.getMoney().toString());
                    break;
                default:
                    break;
            }
        }
    };

    public WoPage(Context context) {
        mContext = context;
        initView();
        setListener();
    }

    public View getView() {
        return mView;
    }

    private void initView() {
        mView = View.inflate(mContext, R.layout.page_wo, null);
        ButterKnife.bind(this, mView);

        user = SharedpreferencesUtil.getInstance().getUser(mContext);
        tv_name.setText(user.get("username") == null ? "未知用户" : user.get("username"));

        requestHttp();
    }

    public void requestHttp() {
        GetInterface request = MyRetrofit.getInstance().request(WebParam.BASE_URL);
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.get("username"));
        Call<ResponseByUserInfo> call = request.getUser("Find.html", map);
        call.enqueue(new Callback<ResponseByUserInfo>() {
            @Override
            public void onResponse(Call<ResponseByUserInfo> call, Response<ResponseByUserInfo> response) {
                if (response.body() != null) {
                userData = response.body().getMsg();
                handler.sendEmptyMessage(REQUEST_SUCCESS);
                }
            }

            @Override
            public void onFailure(Call<ResponseByUserInfo> call, Throwable t) {

            }
        });
    }

    private void setListener() {
        tv_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUserInfoActivity();
            }
        });

        tv_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChangePasswordActivity();
            }


        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* 返回到登录界面*/
                startLoginActivity();

                /* 清除账号缓存*/
                SharedpreferencesUtil.getInstance().cacheUser(mContext, "", "");
            }
        });
    }

    private void startUserInfoActivity() {
        Intent intent = new Intent(mContext, UserInfoActivity.class);
        intent.putExtra("userData",userData);
        mContext.startActivity(intent);
    }

    private void startChangePasswordActivity() {
        Intent intent = new Intent(mContext, ChangePassWordActivity.class);
        intent.putExtra("userData",userData);
        mContext.startActivity(intent);
    }

    private void startLoginActivity() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra("exit", true);
        mContext.startActivity(intent);
    }
}
