package com.example.qrcode;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Scan extends AppCompatActivity {
    IntentIntegrator intentIntegrator =new IntentIntegrator(Scan.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将状态栏透明
        StatusBar statusBar = new StatusBar(Scan.this);
        statusBar.setStatusBarColor(R.color.transparent);

        //设置当前界面UI样式
        setContentView(R.layout.scan);

        //去除默认标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        intentIntegrator.setPrompt("请将识别框对准二维码");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setCaptureActivity(Capture.class);
        intentIntegrator.initiateScan();

    }

    //消息回传  处理扫描界面获得的二维码信息并在当前活动进行处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);  //获取回传信息

        boolean isURL=new MainActivity.RegexUtill().verifyUrl(intentResult.getContents()); //判断回传信息是否是URL

        if (isURL){
            //浏览器部分  增加网络权限
            String uriString=intentResult.getContents();   //获取URL
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(uriString);
            intent.setData(content_url);    //使用得到的URL打开系统默认的浏览器
            intentIntegrator.initiateScan();
            startActivity(intent);
        }
        else {
            //不是URL 直接将文本输出
            //使用一个文本确认框的形式
            AlertDialog.Builder builder=new AlertDialog.Builder(Scan.this);  //创建一个消息提示框对象显示文本
            builder.setTitle("文本内容：");
            builder.setMessage(intentResult.getContents());
            builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    finish();
                    intentIntegrator.initiateScan();
                }
            });
            builder.show();
        }
    }
}
