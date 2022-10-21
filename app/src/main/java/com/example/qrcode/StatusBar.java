package com.example.qrcode;

import android.app.Activity;
import android.os.Build;
import android.view.View;

public class StatusBar {
    private Activity activity;

    //初始化activity
    public StatusBar(Activity activity){
        this.activity = activity;
    }

    //将状态栏设置为传入的color
    public void setStatusBarColor(int color){
        if (Build.VERSION.SDK_INT >= 21) {
            View view = activity.getWindow().getDecorView();
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(color));
        }
    }
}

