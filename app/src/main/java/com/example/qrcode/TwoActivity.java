package com.example.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class TwoActivity extends AppCompatActivity {
    public static final String TYPE = "1";
    public static final String Text = "tex";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将状态栏透明
        StatusBar statusBar = new StatusBar(TwoActivity.this);
        statusBar.setStatusBarColor(R.color.transparent);

        setContentView(R.layout.twolayout);

        //去除默认标题栏
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }

        Button GenerateQRButton = findViewById(R.id.generateQR);
        GenerateQRButton.setOnClickListener((new GenerateListener()));
        Button GenerateODButton = findViewById(R.id.generateOD);
        GenerateODButton.setOnClickListener((new GenerateListener()));

        ImageView ret = findViewById(R.id.Ret);
        ret.setOnClickListener(new changeXmlListener());
    }

    public class GenerateListener implements View.OnClickListener {
        public void onClick(View v) {
            EditText multiText = findViewById(R.id.InputText);   //获取输入内容
            String text = multiText.getText().toString();
            if (text.length() == 0) {
                Toast.makeText(TwoActivity.this,"输入栏为空，请输入内容",Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(TwoActivity.this, Generate.class);
                if (v.getId() == R.id.generateQR) {
                    intent.putExtra(Text, text);
                    intent.putExtra(TYPE, "2");
                    startActivity(intent);
                }
                else if (v.getId() == R.id.generateOD) {
                    boolean flag = true;
                    int n = text.length();
                    for (int i = 0; i < n; i++) {
                        if (text.charAt(i) < 32 || text.charAt(i) > 127) flag = false;
                    }
                    if (!flag) {
                        Toast.makeText(TwoActivity.this,"条形码中不能包含中文字符和换行",Toast.LENGTH_SHORT).show();
                    } else {
                        intent.putExtra(Text, text);
                        intent.putExtra(TYPE, "1");
                        startActivity(intent);
                    }
                }

            }
        }
    }

    public class changeXmlListener implements View.OnClickListener {
        public void onClick(View v) {
            finish();
        }
    }
}
