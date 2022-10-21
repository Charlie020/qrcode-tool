package com.example.qrcode;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将状态栏透明
        StatusBar statusBar = new StatusBar(MainActivity.this);
        statusBar.setStatusBarColor(R.color.transparent);

        //设置当前界面UI样式
        setContentView(R.layout.activity_main);

        //去除默认标题栏
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }

        Button generate = this.findViewById(R.id.Generate);    //生成二维码
        generate.setOnClickListener(new changeXmlListener());  //切换页面 开启新的activity

        Button scan = this.findViewById(R.id.Scan);    //扫描二维码
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator =new IntentIntegrator(MainActivity.this);
                intentIntegrator.setPrompt("请将识别框对准二维码");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(Capture.class);
                intentIntegrator.initiateScan();
            }
        });
    }


    //消息回传  处理扫描界面获得的二维码信息并在当前活动进行处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);  //获取回传信息

        if (intentResult.getContents()!=null){
            //浏览器部分  增加网络权限
            String uriString=intentResult.getContents();   //获取URL
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(uriString);
            intent.setData(content_url);
            startActivity(intent);


        }
    }

    public class changeXmlListener implements View.OnClickListener {
        //切换界面的类
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, TwoActivity.class);  //intent是Android里用于activity之间信息传递的类
            startActivity(intent);
        }
    }
}
