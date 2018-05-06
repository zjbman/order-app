package com.paper.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.paper.order.R;
import com.paper.order.activity.base.BaseActivity;
import com.paper.order.app.ActivityManager;
import com.paper.order.config.WebParam;
import com.paper.order.retrofit.http.MyRetrofit;
import com.paper.order.retrofit.request.GetInterface;
import com.paper.order.retrofit.response.ResponseByUser;
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

public class RegisterActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btn_login)
    Button btn_login;
    @Bind(R.id.tl_username_register)
    TextInputLayout tl_username_register;
    @Bind(R.id.tl_password_register)
    TextInputLayout tl_password_register;
    @Bind(R.id.tl_password_again_register)
    TextInputLayout tl_password_again_register;
    @Bind(R.id.tl_telephone_register)
    TextInputLayout tl_telephone_register;
    @Bind(R.id.tl_email_register)
    TextInputLayout tl_email_register;
    @Bind(R.id.tl_qq_register)
    TextInputLayout tl_qq_register;

    private String username;
    private String password;
    private String passwordAgain;
    private String telephone;
    private String email;
    private String qq;


    @Override
    protected View setContentView() {
        return View.inflate(this, R.layout.activity_register, null);
    }

    @Override
    protected Activity bindActivity() {
        return this;
    }

    @Override
    protected void initView() {
        /* 设定布局中的toolbar*/
        toolbar.setTitle("欢迎注册");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void setListener() {
        /* 这是toolbar右边部分的点击监听事件，这个id是我们给toolbar设置的menu中的id*/
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_login:
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        ActivityManager.getInstance().removeActivity(RegisterActivity.this);
                        break;
                }
                return true;
            }
        });

        tl_password_again_register.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                System.out.println(editable.toString());
                if(!editable.toString().equals(tl_password_register.getEditText().getText().toString())){
                    tl_password_again_register.setHint("两次输入的密码不一致");
                }else{
                    tl_password_again_register.setHint("密码通过");
                }
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkInput()){
                    return;
                }

                requestHttp();

                /** 成功或失败都清空密码,避免用户连续点击注册按钮而一直请求服务器*/
                tl_password_register.getEditText().setText("");
                tl_password_again_register.getEditText().setText("");
                tl_password_again_register.setHint("请跟上面输入的密码保持一致,必填");
            }
        });
    }

    private void requestHttp() {
        Map<String,Object> map = new HashMap<>();
        map.put("username",username);
        map.put("password", MD5.md5(password));
        map.put("name",username);
        map.put("telephone",telephone);
        map.put("email",email);
        map.put("qq",qq);
        GetInterface request = MyRetrofit.getInstance().request(WebParam.BASE_URL);
        Call<ResponseByUser> call = request.getManyParam1("Register.html", map);

        // 发送网络请求(异步)
        call.enqueue(new Callback<ResponseByUser>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<ResponseByUser> call, Response<ResponseByUser> response) {
                //请求处理,输出结果
                if(response.body() != null) {
                    int code = response.body().getCode();
                    handle(code);
                }
            }

            //请求失败时候的回调
            @Override
            public void onFailure(Call<ResponseByUser> call, Throwable throwable) {
                ToastUtil.show(RegisterActivity.this, "连接失败");
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
            case 200://注册成功
                ToastUtil.show(this, "注册成功");
                startLoginActivity();
                break;
            case -100://注册失败
                ToastUtil.show(this, "注册失败");
                break;
            case 104://用户名已存在
                ToastUtil.show(this, "用户名已存在");
                break;
            default:
                ToastUtil.show(this, "注册出现未知错误,状态码 " + code);
                break;
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("exit",true);
        startActivity(intent);
        ActivityManager.getInstance().removeActivity(this);
         /* 清除账号缓存*/
        SharedpreferencesUtil.getInstance().cacheUser(this,"","");
    }

    /**
     * 检查用户注册信息填写是否合格
     */
    private boolean checkInput() {
        username = tl_username_register.getEditText().getText().toString();
        password = tl_password_register.getEditText().getText().toString();
        passwordAgain = tl_password_again_register.getEditText().getText().toString();
        telephone = tl_telephone_register.getEditText().getText().toString();
        email = tl_email_register.getEditText().getText().toString();
        qq = tl_qq_register.getEditText().getText().toString();

        if(StringUtil.isEmpty(username) || StringUtil.isEmpty(password) || StringUtil.isEmpty(passwordAgain)
                || StringUtil.isEmpty(telephone)){
            ToastUtil.show(this,"请填写完整必填的信息");
            return false;
        }

        if(!password.equals(passwordAgain)){
            ToastUtil.show(this,"两次密码输入不一致");
            return false;
        }

        if(StringUtil.isEmpty(email)){
            email = "";
        }
        if(StringUtil.isEmpty(qq)){
            qq = "";
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         /* 将toolbar的菜单布局文件实例化*/
        getMenuInflater().inflate(R.menu.register, menu);
        return true;
    }

    @Override
    protected void onRelease() {

    }



        /* 要点：toolbar的点击监听分为了两个部分，
       一个是它左边的图标（这是系统自动生成的，如果它前面还有activity，图标是<—，id是系统分配的android.R.id.home） ；
       另一个是它右边的文字（这个是我们自定义的菜单所有的）
       左边的点击事件通过下面的方式 */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//             /* 点击toolbar 返回主界面*/
//            case android.R.id.home:
//                startActivity(new Intent(this, MainActivity.class));
//                ActivityManager.getInstance().removeActivity(this);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
