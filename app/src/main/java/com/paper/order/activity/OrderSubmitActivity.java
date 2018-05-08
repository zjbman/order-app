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
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.paper.order.R;
import com.paper.order.activity.base.BaseActivity;
import com.paper.order.adapter.OrderSubmitAdapter;
import com.paper.order.app.ActivityManager;
import com.paper.order.config.WebParam;
import com.paper.order.data.GoodsData;
import com.paper.order.data.GoodsDataForOrderSubmit;
import com.paper.order.data.UserData;
import com.paper.order.retrofit.http.MyRetrofit;
import com.paper.order.retrofit.request.GetInterface;
import com.paper.order.retrofit.response.ResponseByGoods;
import com.paper.order.retrofit.response.ResponseByUserInfo;
import com.paper.order.retrofit.response.ResponseByUsually;
import com.paper.order.util.SharedpreferencesUtil;
import com.paper.order.util.StringUtil;
import com.paper.order.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private UserData userData;
    private String  goodsList;
    private List<GoodsData> goodsDatas;
    private List<GoodsDataForOrderSubmit> GoodsDataForOrderSubmitList;

    private final int REQUEST_USER_SUCCESS = 0;
    private final int REQUEST_GOODS_SUCCESS = 1;
    private final int DATA_OK = 2;

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

                case REQUEST_GOODS_SUCCESS:
//                    setAdapter();
                    break;

                case DATA_OK:
                    tv_accounts.setText((11 + totalMoney) + "");
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
          /* 设定布局中的toolbar*/
        toolbar.setTitle("提交订单");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// 给左上角图标的左边加上一个返回的图标

        goodsList = getIntent().getStringExtra("goodsList");
        parseGoodsList();
//        requestHttpForGoods();
        setAdapter();
    }

    private Double totalMoney = 0.0;

    private void parseGoodsList() {
        JSONArray jsonArray = null;
        try {
            GoodsDataForOrderSubmitList = new ArrayList<>();
            jsonArray = new JSONArray(goodsList);
            for(int i = 0;i < jsonArray.length();i++){
                GoodsDataForOrderSubmit goodsDataForOrderSubmit = new GoodsDataForOrderSubmit();
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                int businessId = jsonObject.getInt("businessId");
                int goodsId = jsonObject.getInt("goodsId");
                int goodsNumber = jsonObject.getInt("goodsNumber");
                Double price = jsonObject.getDouble("price");
                String businessName = jsonObject.getString("businessName");
                String businessPicture = jsonObject.getString("businessPicture");
                String goodsName = jsonObject.getString("goodsName");

                goodsDataForOrderSubmit.setBusinessId(businessId);
                goodsDataForOrderSubmit.setGoodsId(goodsId);
                goodsDataForOrderSubmit.setGoodsNumber(goodsNumber);
                goodsDataForOrderSubmit.setPrice(price);
                goodsDataForOrderSubmit.setBusinessName(businessName);
                goodsDataForOrderSubmit.setBusinessPicture(businessPicture);
                goodsDataForOrderSubmit.setGoodsName(goodsName);

                GoodsDataForOrderSubmitList.add(goodsDataForOrderSubmit);

                totalMoney = totalMoney + (goodsNumber * price);
            }

            handler.sendEmptyMessage(DATA_OK);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void requestHttpForGoods() {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(goodsList);
            StringBuilder businessIdStr = new StringBuilder();
            for(int i = 0;i < jsonArray.length();i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                int businessId = jsonObject.getInt("businessId");
                businessIdStr.append(businessId);

                if(i != jsonArray.length() - 1){
                    businessIdStr.append(",");
                }
            }

            GetInterface request = MyRetrofit.getInstance().request(WebParam.BASE_URL);
            Map<String,Object> map = new HashMap<>();
            map.put("ids",businessIdStr.toString());
            Call<ResponseByGoods> call = request.getGoods("GetMany.html", map);
            call.enqueue(new Callback<ResponseByGoods>() {
                @Override
                public void onResponse(Call<ResponseByGoods> call, Response<ResponseByGoods> response) {
                    if(response.body() == null){
                        ToastUtil.show(OrderSubmitActivity.this,"body() == null");
                    }else{
                        int code = response.body().getCode();
                        if(code == -100){
                            ToastUtil.show(OrderSubmitActivity.this,"code == -100");
                        }else if(code == 200){
                            parse(response.body().getMsg());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseByGoods> call, Throwable t) {
                    ToastUtil.show(OrderSubmitActivity.this,"连接失败");
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parse(List<ResponseByGoods.Msg> msgs) {
        goodsDatas = new ArrayList<>();
        for (ResponseByGoods.Msg msg : msgs) {
            goodsDatas.add(new GoodsData(msg));
        }

        handler.sendEmptyMessage(REQUEST_GOODS_SUCCESS);
    }

    private void setAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        OrderSubmitAdapter adapter = new OrderSubmitAdapter(this, GoodsDataForOrderSubmitList);
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
        map.put("goodsList", goodsList);//json数组格式，包含businessId，goodsId，goodsNumber 三个字段
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
                intent.putExtra("position",3);
                startActivity(intent);
                ActivityManager.getInstance().removeActivity(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
