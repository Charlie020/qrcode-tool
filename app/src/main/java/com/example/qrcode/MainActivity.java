package com.example.qrcode;



import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;





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
        generate.setOnClickListener(new changeXmlListener());//切换页面 开启新的activity
        Button generate2 = this.findViewById(R.id.Scan);
        generate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator =new IntentIntegrator(MainActivity.this);
                intentIntegrator.setPrompt("闪光");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(Capture.class);
                intentIntegrator.initiateScan();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (intentResult.getContents()!=null){
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Result");
            builder.setMessage(intentResult.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }else {
            Toast.makeText(getApplicationContext(),"OOPS... Did not scan anything",Toast.LENGTH_SHORT).show();
        }
    }

    public class changeXmlListener implements View.OnClickListener {
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, TwoActivity.class);
            startActivity(intent);
        }
    }
}
