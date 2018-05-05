package com.paper.order.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paper.order.R;
import com.paper.order.activity.adapter.CommentAdapter;
import com.paper.order.config.WebParam;
import com.paper.order.data.CommentData;
import com.paper.order.retrofit.http.MyRetrofit;
import com.paper.order.retrofit.request.GetInterface;
import com.paper.order.retrofit.response.ResponseByComment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zjbman
 * on 2018/3/1.
 */

@SuppressLint("ValidFragment")
public class CommentFragment  extends Fragment {
    private Context mContext;
    private View view;
    private int businessId;
    private List<CommentData> commentDatas;

    private CommentAdapter adapter;

    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;


    private final int REQUEST_SECCESS = 1;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SECCESS:
                    setAdapter();
                    break;
                default:
                    break;
            }
        }
    };

    @SuppressLint("ValidFragment")
    public CommentFragment(Context context, int businessId){
        mContext = context;
        this.businessId = businessId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_comment,null);
        ButterKnife.bind(this,view);
        requestHttp();
        return view;
    }

    private void requestHttp() {
        GetInterface request = MyRetrofit.getInstance().request(WebParam.BASE_URL);
        Map<String, Object> map = new HashMap<>();
        map.put("id", businessId);
        Call<ResponseByComment> call = request.getComment("List.html", map);
        call.enqueue(new Callback<ResponseByComment>() {
            @Override
            public void onResponse(Call<ResponseByComment> call, Response<ResponseByComment> response) {
                parse(response.body());
            }

            @Override
            public void onFailure(Call<ResponseByComment> call, Throwable t) {

            }
        });
    }

    private void parse(ResponseByComment body) {
        List<ResponseByComment.Msg> msgs = body.getMsg();
        commentDatas = new ArrayList<>();
        for (ResponseByComment.Msg msg : msgs) {
            commentDatas.add(new CommentData(msg));
        }

        handler.sendEmptyMessage(REQUEST_SECCESS);
    }

    private void setAdapter() {

        if(commentDatas != null && commentDatas.size() > 0){
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
            adapter = new CommentAdapter(mContext,commentDatas);
            recyclerView.setAdapter(adapter);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
