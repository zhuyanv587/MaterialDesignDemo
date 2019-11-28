package com.example.design.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.design.R;
import com.example.design.adapter.NewsAdapter;
import com.example.design.adapter.NewsGridAdapter;
import com.example.design.model.News;
import com.example.design.utils.HttpsUtil;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class RecyclerViewActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    //常量：聚合数据的url网址和handle的消息
    private static final String NEWS_URL = "http://v.juhe.cn/toutiao/index";
    private static final int GET_NEWS = 1;
    private static final int GET_NEWS_ERROR = 2;

    private Toolbar toolbar;
    private RecyclerView rvNews;
    private SwipeRefreshLayout refreshLayout;

    //数据处理的对象
    private NewsHandler handler;
    private List<News> newsList;
    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        handler = new NewsHandler(this);

        initView();
        initData();
    }


    private void initView() {
        //初始化Toolbar
        toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("RecyclerView示例");
        setSupportActionBar(toolbar);

        //初始化RecyclerView列表和自动刷新布局
        rvNews = findViewById(R.id.rv_news);
        refreshLayout = findViewById(R.id.refresh);

        //设置SwipeRefreshLayout的事件监听和进度条颜色
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void initData() {
        String appKey = "e9381e5c4302af1fff6832b7bdd07610";
        String url = NEWS_URL + "?key=" + appKey + "&type=top";
        final Request request = new Request.Builder().url(url).build();
        HttpsUtil.getInstance().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("RecyclerViewActivity", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()){
                    String json = response.body().string();
                    JSONObject obj = JSON.parseObject(json);
                    JSONObject result = obj.getJSONObject("result");
                    if (result != null){
                        JSONArray data = result.getJSONArray("data");
                        if (data != null && !data.isEmpty()){
                            Message msg = handler.obtainMessage();
                            msg.what = GET_NEWS;
                            msg.obj = data.toJSONString();
                            handler.sendMessage(msg);
                        }
                    }else {
                        Message msg = handler.obtainMessage();
                        msg.what = GET_NEWS_ERROR;
                        msg.obj = obj.getString("reason");
                        handler.sendMessage(msg);
                    }

                }else {
                    Log.e("RecyclerViewActivity", response.message());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //处理菜单项的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_linear:
                rvNews.setLayoutManager(new LinearLayoutManager(this));
                adapter = new NewsAdapter(newsList);
                rvNews.setAdapter(adapter);
                break;
            case R.id.item_grid:
                rvNews.setLayoutManager(new GridLayoutManager(this, 2));
                NewsGridAdapter adapter = new NewsGridAdapter(newsList);
                rvNews.setAdapter(adapter);
                break;
            case R.id.item_stagger:
                rvNews.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
                adapter = new NewsGridAdapter(newsList);
                rvNews.setAdapter(adapter);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        initData();
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        }, 3000);
    }

    static class NewsHandler extends Handler{
        private WeakReference<Activity> ref;

        public NewsHandler(Activity activity){
            this.ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final RecyclerViewActivity activity = (RecyclerViewActivity) this.ref.get();
            if (msg.what == GET_NEWS){
                //获取数据
                String json = (String) msg.obj;
                activity.newsList = JSON.parseArray(json,News.class);

                //设置RecyclerView的分割线和布局
                activity.rvNews.setLayoutManager(new LinearLayoutManager(activity));

                //设置Adapter
                activity.adapter = new NewsAdapter(activity.newsList);
                activity.rvNews.setAdapter(activity.adapter);

                //设置事件监听
                activity.adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(activity, NewsDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("news", activity.newsList.get(position));
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                    }
                });

            }else if (msg.what == GET_NEWS_ERROR) {
                String reason = (String) msg.obj;
                Snackbar.make(activity.rvNews, reason, Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
