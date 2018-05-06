package com.paper.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.paper.order.R;
import com.paper.order.activity.base.BaseActivity;
import com.paper.order.app.ActivityManager;
import com.paper.order.config.WebParam;
import com.paper.order.data.UserData;
import com.paper.order.page.wo.WoPage;
import com.paper.order.retrofit.http.MyRetrofit;
import com.paper.order.retrofit.request.GetInterface;
import com.paper.order.retrofit.response.ResponseByUser;
import com.paper.order.retrofit.response.ResponseByUserInfo;
import com.paper.order.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends BaseActivity {

    private UserData userData;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btn_update)
    Button btn_update;
    @Bind(R.id.tl_username)
    TextInputLayout tl_username;
    @Bind(R.id.tl_name)
    TextInputLayout tl_name;
    @Bind(R.id.tl_telephone)
    TextInputLayout tl_telephone;
    @Bind(R.id.tl_email)
    TextInputLayout tl_email;
    @Bind(R.id.tl_qq)
    TextInputLayout tl_qq;

    @Override
    protected View setContentView() {
        return View.inflate(this, R.layout.activity_user_info, null);
    }

    @Override
    protected Activity bindActivity() {
        return this;
    }

    @Override
    protected void initView() {
        userData = (UserData) getIntent().getSerializableExtra("userData");

        /* 设定布局中的toolbar*/
        toolbar.setTitle("个人信息");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tl_username.getEditText().setText(userData.getUsername());
        tl_name.getEditText().setText(userData.getName());
        tl_telephone.getEditText().setText(userData.getTelephone());
        tl_email.getEditText().setText(userData.getEmail());
        tl_qq.getEditText().setText(userData.getQq());

        tl_username.setEnabled(false);

    }

    @Override
    protected void setListener() {
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = tl_username.getEditText().getText().toString();
                String name = tl_name.getEditText().getText().toString();
                String telephone = tl_telephone.getEditText().getText().toString();
                String email = tl_email.getEditText().getText().toString();
                String qq = tl_qq.getEditText().getText().toString();

                requestHttp(username,name,telephone,email,qq);

            }
        });
    }

    private void requestHttp(String username,String name,String telephone,String email,String qq) {
        GetInterface request = MyRetrofit.getInstance().request(WebParam.BASE_URL);
        Map<String,Object> map = new HashMap<>();
        map.put("username",username);
        map.put("name",name);
        map.put("telephone",telephone);
        map.put("email",email);
        map.put("qq",qq);
        Call<ResponseByUser> call = request.getManyParam1("Update.html", map);
        call.enqueue(new Callback<ResponseByUser>() {
            @Override
            public void onResponse(Call<ResponseByUser> call, Response<ResponseByUser> response) {
                parse(response.body());
            }

            @Override
            public void onFailure(Call<ResponseByUser> call, Throwable t) {

            }
        });
    }


    private void parse(ResponseByUser body) {
        int code = body.getCode();
        if (code == -100) {
            ToastUtil.show(this, body.getMsg());
        } else if (code == 200) {
            ToastUtil.show(this, body.getMsg());

            startActivity(new Intent(this, MainActivity.class));
            ActivityManager.getInstance().removeActivity(this);
        }
    }

    @Override
    protected void onRelease() {

    }

    /* 要点：toolbar的点击监听分为了两个部分，
     一个是它左边的图标（这是系统自动生成的，如果它前面还有activity，图标是<—，id是系统分配的android.R.id.home） ；
     另一个是它右边的文字（这个是我们自定义的菜单所有的）
     左边的点击事件通过下面的方式 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
             /* 点击toolbar 返回主界面*/
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                ActivityManager.getInstance().removeActivity(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
