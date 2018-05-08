package com.paper.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.paper.order.R;
import com.paper.order.activity.base.BaseActivity;
import com.paper.order.app.ActivityManager;
import com.paper.order.config.WebParam;
import com.paper.order.data.UserData;
import com.paper.order.retrofit.http.MyRetrofit;
import com.paper.order.retrofit.request.GetInterface;
import com.paper.order.retrofit.response.ResponseByUsually;
import com.paper.order.util.MD5;
import com.paper.order.util.StringUtil;
import com.paper.order.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassWordActivity extends BaseActivity {

    private UserData userData;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btn_save)
    Button btn_save;
    @Bind(R.id.tl_old)
    TextInputLayout tl_old;
    @Bind(R.id.tl_password)
    TextInputLayout tl_password;
    @Bind(R.id.tl_password_again)
    TextInputLayout tl_password_again;

    private String oldPassword;
    private String newPassword;
    private String newPasswordAgain;

    @Override
    protected View setContentView() {
        return View.inflate(this, R.layout.activity_change_pass_word, null);
    }

    @Override
    protected Activity bindActivity() {
        return this;
    }

    @Override
    protected void initView() {
        userData = (UserData) getIntent().getSerializableExtra("userData");

         /* 设定布局中的toolbar*/
        toolbar.setTitle("修改密码");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void setListener() {


        tl_password_again.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(tl_password.getEditText().getText().toString())) {
                    tl_password_again.setHint("两次输入的密码不一致");
                } else {
                    tl_password_again.setHint("密码通过");
                }
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check()) {
                    requestHttp();


                    /** 成功或失败都清空密码,避免用户连续点击注册按钮而一直请求服务器*/
                    tl_password.getEditText().setText("");
                    tl_password_again.getEditText().setText("");
                    tl_password_again.setHint("请跟上面输入的密码保持一致,必填");

                }
            }
        });
    }

    private void requestHttp() {
        GetInterface request = MyRetrofit.getInstance().request(WebParam.BASE_URL);
        Map<String, Object> map = new HashMap<>();
        map.put("username", userData.getUsername());
        map.put("newPassword", newPassword);
        Call<ResponseByUsually> call = request.getManyParam1("Change.html", map);
        call.enqueue(new Callback<ResponseByUsually>() {
            @Override
            public void onResponse(Call<ResponseByUsually> call, Response<ResponseByUsually> response) {
                parse(response.body());
            }

            @Override
            public void onFailure(Call<ResponseByUsually> call, Throwable t) {

            }
        });
    }

    private void parse(ResponseByUsually body) {
        int code = body.getCode();
        if (code == -100) {
            ToastUtil.show(this, body.getMsg());
        } else if (code == 200) {
            ToastUtil.show(this, body.getMsg());
        }
    }

    private boolean check() {
        oldPassword = tl_old.getEditText().getText().toString();
        newPassword = tl_password.getEditText().getText().toString();
        newPasswordAgain = tl_password_again.getEditText().getText().toString();


        if (StringUtil.isEmpty(oldPassword) || StringUtil.isEmpty(newPassword) || StringUtil.isEmpty(newPasswordAgain)) {
            ToastUtil.show(ChangePassWordActivity.this, "请填写完整信息");
            return false;
        }

        if (!newPassword.equals(newPasswordAgain)) {
            ToastUtil.show(ChangePassWordActivity.this, "两次输入的新密码不一致");
            return false;
        }

        if (!MD5.md5(oldPassword).equals(userData.getPassword())) {
            System.out.println("userData.getPassword() === " + userData.getPassword());
            System.out.println("MD5.md5(oldPassword) === " + MD5.md5(oldPassword));
            ToastUtil.show(ChangePassWordActivity.this, "旧密码填写不正确");
            return false;
        }

        return true;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//         /* 将toolbar的菜单布局文件实例化*/
//        getMenuInflater().inflate(R.menu.register, menu);
//        return true;
//    }


    /* 要点：toolbar的点击监听分为了两个部分，
      一个是它左边的图标（这是系统自动生成的，如果它前面还有activity，图标是<—，id是系统分配的android.R.id.home） ；
      另一个是它右边的文字（这个是我们自定义的菜单所有的）
      左边的点击事件通过下面的方式 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
             /* 点击toolbar 返回主界面*/
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("position",4);
                startActivity(intent);
                ActivityManager.getInstance().removeActivity(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRelease() {

    }
}
