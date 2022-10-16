package com.example.qrcode;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将状态栏透明
        StatusBar statusBar = new StatusBar(MainActivity.this);
        statusBar.setStatusBarColor(R.color.transparent);

        setContentView(R.layout.activity_main);

        //去除默认标题栏
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }


        Button generate = this.findViewById(R.id.Generate);    //生成二维码
        generate.setOnClickListener(new changeXmlListener());   //切换页面 开启新的activity
    }

    public class changeXmlListener implements View.OnClickListener {
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, TwoActivity.class);
            startActivity(intent);
        }
    }
}