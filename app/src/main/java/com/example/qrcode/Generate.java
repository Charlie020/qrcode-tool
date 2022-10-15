package com.example.qrcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Generate extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generatelayout);

        // 初始化界面
        Intent intent = getIntent();
        String text = intent.getStringExtra(TwoActivity.Text);
        String Type = intent.getStringExtra(TwoActivity.TYPE);
        Button changeButton = findViewById(R.id.ChangeCode);
        changeButton.setOnClickListener(new ChangeListener());
        Bitmap bitmap;
        if (Type.equals("2")) {
            bitmap = QRUtils.createQRCode(text,1000,1000,null);
            changeButton.setText("转为条形码");
        } else {
            bitmap = QRUtils.createODcode(text);
            changeButton.setText("转为二维码");
        }
        ImageView img = findViewById(R.id.img);
        img.setImageBitmap(bitmap);



        ImageView ret = findViewById(R.id.Ret);
        ret.setOnClickListener(new changeXmlListener());
    }

    public class ChangeListener implements View.OnClickListener {
        public void onClick(View v) {
            Button changeButton = findViewById(R.id.ChangeCode);
            Intent intent = getIntent();
            String text = intent.getStringExtra(TwoActivity.Text);
            Bitmap bitmap;
            if (changeButton.getText().equals("转为二维码")) {
                bitmap = QRUtils.createQRCode(text,1000,1000,null);
                changeButton.setText("转为条形码");
            } else {
                bitmap = QRUtils.createODcode(text);
                changeButton.setText("转为二维码");
            }
            ImageView img = findViewById(R.id.img);
            img.setImageBitmap(bitmap);
        }
    }

    public class changeXmlListener implements View.OnClickListener {
        public void onClick(View v) {
            finish();
        }
    }
}
