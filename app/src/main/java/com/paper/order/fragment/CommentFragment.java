package com.paper.order.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.paper.order.R;
import com.paper.order.adapter.CommentAdapter;
import com.paper.order.config.WebParam;
import com.paper.order.data.CommentData;
import com.paper.order.retrofit.http.MyRetrofit;
import com.paper.order.retrofit.request.GetInterface;
import com.paper.order.retrofit.response.ResponseByComment;
import com.paper.order.retrofit.response.ResponseByUser;
import com.paper.order.util.SharedpreferencesUtil;
import com.paper.order.util.StringUtil;
import com.paper.order.util.ToastUtil;

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
public class CommentFragment extends Fragment {
    private Context mContext;
    private View view;
    private int businessId;
    private String username;
    private List<CommentData> commentDatas;

    private CommentAdapter adapter;

    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;
    @Bind(R.id.btn_comment)
    Button btn_comment;

    private EditText et_comment;
    private Button btn_cancel;
    private Button btn_send;

    private String comment;

    private AlertDialog dialog;

    private final int REQUEST_SECCESS = 1;
    private final int INSERT_SUCCESS = 2;
    private final int DIALOG_SHOW = 3;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SECCESS:
                    setAdapter();
                    break;

                case INSERT_SUCCESS:
                    requestHttp();
                    break;

                case DIALOG_SHOW:
                    setListener();
                    break;
                default:
                    break;
            }
        }
    };

    @SuppressLint("ValidFragment")
    public CommentFragment(Context context, int businessId) {
        mContext = context;
        this.businessId = businessId;
        Map<String, String> user = SharedpreferencesUtil.getInstance().getUser(mContext);
        username = user.get("username");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_comment, null);
        ButterKnife.bind(this, view);
        requestHttp();
        setListener();
        return view;
    }

    private void setListener() {
        if (btn_comment != null) {
            btn_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startDialog();
                }
            });

        }

        if (btn_cancel != null) {
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
        }

        if (btn_send != null) {
            btn_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    comment = et_comment.getText().toString();
                    if (StringUtil.isEmpty(comment)) {
                        ToastUtil.show(mContext, "内容不能为空");
                        return;
                    }

                    requestComment();

                    /* 请求服务器不管成功与否都置空输入框，以免重复发送请求*/
                    et_comment.setText("");
                }
            });
        }
    }

    private void requestComment() {
        final GetInterface request = MyRetrofit.getInstance().request(WebParam.BASE_URL);
        Map<String, Object> map = new HashMap<>();
        map.put("id", businessId);
        map.put("username", username);
        map.put("content", comment);
        Call<ResponseByUser> call = request.insertComment("Insert.html", map);
        call.enqueue(new Callback<ResponseByUser>() {
            @Override
            public void onResponse(Call<ResponseByUser> call, Response<ResponseByUser> response) {
                int code = response.body().getCode();
                if (code == -100) {
                    ToastUtil.show(mContext, "评论失败");
                } else if (code == 200) {
                    ToastUtil.show(mContext, "评论成功");
                    dialog.cancel();
                    handler.sendEmptyMessage(INSERT_SUCCESS);
                }
            }

            @Override
            public void onFailure(Call<ResponseByUser> call, Throwable t) {
                ToastUtil.show(mContext, "链接失败");
            }
        });
    }


    private void startDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        dialog = builder.create();
        View view = View.inflate(mContext, R.layout.dialog_commit_comment, null);
        et_comment = view.findViewById(R.id.et_comment);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_send = view.findViewById(R.id.btn_send);
        dialog.setView(view);
        dialog.show();

        handler.sendEmptyMessage(DIALOG_SHOW);
    }

    private void requestHttp() {
        GetInterface request = MyRetrofit.getInstance().request(WebParam.BASE_URL);
        Map<String, Object> map = new HashMap<>();
        map.put("id", businessId);
        Call<ResponseByComment> call = request.getComment("List.html", map);
        call.enqueue(new Callback<ResponseByComment>() {
            @Override
            public void onResponse(Call<ResponseByComment> call, Response<ResponseByComment> response) {
                if (response.body() != null) {
                    parse(response.body());
                }
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

        if (commentDatas != null && commentDatas.size() > 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            adapter = new CommentAdapter(mContext, commentDatas);
            recyclerView.setAdapter(adapter);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
