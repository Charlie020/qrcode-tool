package com.example.qrcode;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
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

        boolean isURL=new RegexUtill().verifyUrl(intentResult.getContents()); //判断回传信息是否是URL

        if (isURL){
            //浏览器部分  增加网络权限
            String uriString=intentResult.getContents();   //获取URL
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(uriString);
            intent.setData(content_url);    //使用得到的URL打开系统默认的浏览器
            startActivity(intent);
        }
        else {
            //不是URL 直接将文本输出
            //使用一个文本确认框的形式
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);  //创建一个消息提示框对象显示文本
            builder.setTitle("文本内容：");
            builder.setMessage(intentResult.getContents());
            builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }
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
            String regEx ="^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~/])+$";
            // 编译正则表达式
            Pattern pattern = Pattern.compile(regEx);

            Matcher matcher = pattern.matcher(url);
            // 字符串是否与正则表达式相匹配
            return matcher.matches();
        }
    }
}
