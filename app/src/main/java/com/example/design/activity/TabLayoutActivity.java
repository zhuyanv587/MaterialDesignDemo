package com.example.design.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.design.R;
import com.example.design.adapter.NewsFragmentAdapter;
import com.example.design.fragment.NewsFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class TabLayoutActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabs;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);

        initView();
        initViewPager();
    }

    private void initViewPager() {
        //组装tab的标题集合
        List<String> titles = new ArrayList<>();
        titles.add("精选");
        titles.add("新闻");
        titles.add("体育");
        titles.add("购物");
        titles.add("视频");
        titles.add("健康");

        //根据tab的个数初始化Fragment的集合
        List<Fragment> fragments = new ArrayList<>();
        for (String title : titles) {
            tabs.addTab(tabs.newTab().setText(title));
            fragments.add(NewsFragment.newInstance());
        }

        //创建ViewPager的adapter
        NewsFragmentAdapter adapter = new NewsFragmentAdapter(
                getSupportFragmentManager(), fragments, titles);

        //viewPager的设置adapter
        viewPager.setAdapter(adapter);

        //关联TabLayout和viewPager的实现联动，关键点：adapter必须重写getPageTitle()方法
        tabs.setupWithViewPager(viewPager);
    }

    private void initView() {
        tabs = findViewById(R.id.tabs_layout);
        viewPager = findViewById(R.id.view_pager);

        // 初始化Toolbar
        toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("TabLayout示例");
        setSupportActionBar(toolbar);
    }
}
