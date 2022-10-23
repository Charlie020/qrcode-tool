package com.example.qrcode;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class Capture extends Activity {
    private boolean flag = true;
    private DecoratedBarcodeView barcodeScannerView;
    private CaptureManager capture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan);

        barcodeScannerView = (DecoratedBarcodeView) findViewById(R.id.viewfinder_view); // 扫描界面初始化
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.btn_torch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                light();
            }
        });
    }

    protected void light() {
        if (flag) {
            flag = false;   // 开闪光灯
            barcodeScannerView.setTorchOn();
        } else {
            flag = true;   // 关闪光灯
            barcodeScannerView.setTorchOff();
        }
    }

    @Override
    protected void onResume() {     //CaptureActivity函数重构，下面类似，均为默认函数
        super.onResume();
        capture.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}