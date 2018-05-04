package com.paper.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.paper.order.R;
import com.paper.order.activity.base.BaseActivity;
import com.paper.order.app.ActivityManager;
import com.paper.order.config.WebParam;
import com.paper.order.retrofit.http.MyRetrofit;
import com.paper.order.retrofit.response.ResponseByHttp;
import com.paper.order.util.MD5;
import com.paper.order.util.SharedpreferencesUtil;
import com.paper.order.util.StringUtil;
import com.paper.order.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tl_username)
    TextInputLayout tl_username;
    @Bind(R.id.tl_password)
    TextInputLayout tl_password;
    @Bind(R.id.btn_login)
    Button btn_login;

    private String username;
    private String password;

    @Override
    protected View setContentView() {
        return View.inflate(this, R.layout.activity_login, null);
    }

    @Override
    protected Activity bindActivity() {
        return this;
    }

    @Override
    protected void initView() {
        /* 设定布局中的toolbar*/
        toolbar.setTitle("欢迎登录");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        autoLogin();
    }

    /**
     * 尝试自动登录
     */
    private void autoLogin() {
        Map<String, String> user = SharedpreferencesUtil.getInstance().getUser(this);
        username = user.get("username");
        password = user.get("password");

        if(!StringUtil.isEmpty(username) && !StringUtil.isEmpty(password)){
            tl_username.setHint("");
            tl_password.setHint("");
            tl_username.getEditText().setText(username);
            tl_password.getEditText().setText(password);

            requestHttp();
        }
    }

    @Override
    protected void setListener() {
        /* 这是toolbar右边部分的点击监听事件，这个id是我们给toolbar设置的menu中的id*/
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_register:
                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                        ActivityManager.getInstance().removeActivity(LoginActivity.this);
                        break;
                }
                return true;
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkUserNameAndPassword()) {
                    return;
                }

                requestHttp();

                /** 成功或失败都清空密码,避免用户连续点击登录按钮而一直请求服务器*/
                tl_password.getEditText().setText("");
            }
        });
    }

    private void requestHttp() {
        Map<String,Object> map = new HashMap<>();
        map.put("username",username);
        map.put("password",MD5.md5(password));
        Call<ResponseByHttp> call = MyRetrofit.getInstance().request(WebParam.BASE_URL, "Login.html", map);

        // 发送网络请求(异步)
        call.enqueue(new Callback<ResponseByHttp>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<ResponseByHttp> call, Response<ResponseByHttp> response) {
                //请求处理,输出结果
                int code = response.body().getCode();
                handle(code);
            }

            //请求失败时候的回调
            @Override
            public void onFailure(Call<ResponseByHttp> call, Throwable throwable) {
                ToastUtil.show(LoginActivity.this, "连接失败");
            }
        });
    }

    /**
     * 处理返回结果
     *
     * @param code
     */
    private void handle(int code) {
        switch (code) {
            case 200://登录成功
                ToastUtil.show(this, "登录成功");
                cacheUser();
                startMainActivity();
                break;
            case -100://登录失败
                ToastUtil.show(this, "登录失败");
                break;
            case 102://用户不存在
                ToastUtil.show(this, "账号或密码不正确");
                break;
            case 103://账号或密码不正确
                ToastUtil.show(this, "账号或密码不正确");
                break;
            default:
                ToastUtil.show(this, "登录出现未知错误,状态码 " + code);
                break;
        }
    }

    /**
     * 登录成功后就保存当前的用户信息，用于下一次自动登录
     */
    private void cacheUser() {
        SharedpreferencesUtil.getInstance().cacheUser(this,username,password);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        ActivityManager.getInstance().removeActivity(this);
    }

    /**
     * 检查账号和密码是否输入正确
     *
     * @return
     */
    private boolean checkUserNameAndPassword() {
        username = tl_username.getEditText().getText().toString();
        password = tl_password.getEditText().getText().toString();

        if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password)) {
            ToastUtil.show(this, "账号或密码不能为空!");
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         /* 将toolbar的菜单布局文件实例化*/
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    protected void onRelease() {

    }
}
