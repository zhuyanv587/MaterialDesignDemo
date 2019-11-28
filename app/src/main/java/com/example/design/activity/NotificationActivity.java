package com.example.design.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import com.example.design.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnNormal;
    private Button btnFold;
    private Button btnHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initView();
    }

    private void initView() {
        // 设置标题栏的标题
        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Notification");
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setSubtitle("三种不同类型的通知");
        setSupportActionBar(toolbar);

        btnNormal = findViewById(R.id.btn_normal);
        btnFold = findViewById(R.id.btn_fold);
        btnHang = findViewById(R.id.btn_hang);

        btnNormal.setOnClickListener(this);
        btnFold.setOnClickListener(this);
        btnHang.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "normal";
            String channelName = "普通";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            createNotificationChannel(channelId, channelName, importance);

            channelId = "fold";
            channelName = "折叠";
            importance = NotificationManager.IMPORTANCE_LOW;
            createNotificationChannel(channelId, channelName, importance);

            channelId = "hang";
            channelName = "悬挂";
            importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);
        }

    }

    private void createNotificationChannel(String channelId, String channelName, int importance) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_normal:
                sendNormalNotification();
                break;
            case R.id.btn_fold:
                sendFoldNotification();
                break;
            case R.id.btn_hang:
                sendHangNotification();
                break;
        }

    }

    private void sendNormalNotification() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.baidu.com"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification notification = new NotificationCompat.Builder(this, "normal")
                .setContentTitle("普通通知")
                .setContentText("今天中午吃什么")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .build();

        manager.notify(1, notification);
    }

    private void sendFoldNotification() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.baidu.com"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        //用RemoteViews创建自定义Notification下拉时的样式
        RemoteViews show = new RemoteViews(getPackageName(), R.layout.notification_fold);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, "fold")
                .setContentTitle("折叠通知")
                .setContentText("折叠通知用于自定义通知视图")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .build();

        notification.bigContentView = show;
        manager.notify(2, notification);
    }

    private void sendHangNotification() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.baidu.com"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "hang")
                .setContentTitle("悬挂通知")
                .setContentText("Android 8.0的悬挂通知就是将importance设为IMPORTANCE_HIGH")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setFullScreenIntent(pendingIntent, true);
        }
        manager.notify(3, builder.build());
    }

}