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

        Intent intent = getIntent();
        String text = intent.getStringExtra(TwoActivity.Text);

        String Type = intent.getStringExtra(TwoActivity.TYPE);
        Bitmap bitmap;
        if (Type.equals("2")) {
            bitmap = QRUtils.createQRCode(text,1000,1000,null);
        } else {
            bitmap = QRUtils.createODcode(text);
        }

        ImageView img = findViewById(R.id.img);
        img.setImageBitmap(bitmap);

        ImageView ret = findViewById(R.id.Ret);
        ret.setOnClickListener(new changeXmlListener());
    }



    public class changeXmlListener implements View.OnClickListener {
        public void onClick(View v) {
            Intent intent = new Intent(Generate.this, TwoActivity.class);
            startActivity(intent);
        }
    }
}
