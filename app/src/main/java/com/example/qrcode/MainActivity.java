package com.example.qrcode;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

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
                // 获得权限
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Scan();
                } else { //未获得权限
                    // 若未获得相机权限
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                }
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Scan();
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("提示");
                    alertDialog.setMessage("无法访问某些权限可能会影响部分功能的使用，是否要跳转到应用设置页面更改授权？");
                    alertDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.fromParts("package", getPackageName(), null));
                            startActivity(intent);
                        }
                    });
                    alertDialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            }
        }
    }

    public void Scan() {
        Intent intent = new Intent(MainActivity.this, Scan.class);
        startActivity(intent);
    }

    public class changeXmlListener implements View.OnClickListener {
        //切换界面的类
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, InputActivity.class);  //intent是Android里用于activity之间信息传递的类
            startActivity(intent);
        }
    }

    public static class RegexUtill {
        //判断字符串是否是URL的类

        public boolean verifyUrl(String url){
            // URL验证规则
            String regEx ="^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)" +
                    "(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~/])+$";
            // 编译正则表达式
            Pattern pattern = Pattern.compile(regEx);

            Matcher matcher = pattern.matcher(url);
            // 字符串是否与正则表达式相匹配
            return matcher.matches();
        }
    }
}