package com.example.design.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.design.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置标题栏的标题
        Toolbar toolbar = findViewById(R.id.tool_bar);

        toolbar.setTitle("Android 5.0新特性");
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setSubtitle("Material Design控件使用");
        setSupportActionBar(toolbar);

    }

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.btn_recycler:
                intent = new Intent(MainActivity.this,RecyclerViewActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_coordinator:
                intent = new Intent(MainActivity.this,CoordinatorActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_tabLayout:
                intent = new Intent(MainActivity.this,TabLayoutActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_navigation:
                intent = new Intent(MainActivity.this,DrawerNavigationActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_notification:
                intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
                break;
        }
    }
}
