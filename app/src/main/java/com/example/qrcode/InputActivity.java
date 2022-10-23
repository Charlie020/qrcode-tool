package com.example.qrcode;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class InputActivity extends AppCompatActivity {
    public static final String Text = "tex";
    public static final String TYPE = "1";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将状态栏透明
        StatusBar statusBar = new StatusBar(InputActivity.this);
        statusBar.setStatusBarColor(R.color.transparent);

        setContentView(R.layout.inputlayout);

        //去除默认标题栏
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }

        Button GenerateQRButton = findViewById(R.id.generateQR);
        GenerateQRButton.setOnClickListener((new GenerateListener()));
        Button GenerateODButton = findViewById(R.id.generateOD);
        GenerateODButton.setOnClickListener((new GenerateListener()));

        Button PasteButton = findViewById(R.id.paste);
        PasteButton.setOnClickListener(new PasteListener());
        Button ClearButton = findViewById(R.id.Clear);
        ClearButton.setOnClickListener(new ClearListener());

        ImageView ret = findViewById(R.id.Ret);
        ret.setOnClickListener(new changeXmlListener());
    }

    // 点击生成按钮时调用的监听器
    public class GenerateListener implements View.OnClickListener {
        public void onClick(View v) {
            EditText multiText = findViewById(R.id.ResultText);   //获取输入内容
            String text = multiText.getText().toString();
            if (text.length() == 0) {                            //判断内容是否为空
                Toast.makeText(InputActivity.this,"输入栏为空，请输入内容",Toast.LENGTH_SHORT).show();
            } else {                                             // 不为空则判断用户需要生成什么码
                Intent intent = new Intent(InputActivity.this, Generate.class);
                if (v.getId() == R.id.generateQR) {              //如果当前选择生成的是二维码
                    int n = 0;
                    for (int i = 0; i < text.length(); i++) {    // 统计字符串长度，ascii码以外的字符按3个字节计算
                        if (text.charAt(i) <= 0 || text.charAt(i) >= 128) n += 3;
                        else n += 1;
                    }
                    if (n <= 1250) {
                        intent.putExtra(Text, text);                 //将文本内容和代表二维码的识别码传入到下一页面
                        intent.putExtra(TYPE, "2");
                        startActivity(intent);
                    } else {
                        Toast.makeText(InputActivity.this,"输入栏内容过多，请控制在1250个字符以内再生成二维码！",Toast.LENGTH_LONG).show();
                    }
                }
                else if (v.getId() == R.id.generateOD) {         //如果当前选择生成的是条形码
                    boolean flag = true;
                    for (int i = 0; i < text.length(); i++) {
                        if (text.charAt(i) < 32 || text.charAt(i) > 127) {   //判断是否含有不规范字符
                            flag = false;
                            break;
                        }
                    }
                    if (!flag) {
                        Toast.makeText(InputActivity.this,"条形码中不能包含中文字符和换行",Toast.LENGTH_SHORT).show();
                    } else {
                        if (text.length() <= 80) {
                            intent.putExtra(Text, text);            //将文本内容和代表二维码的识别码传入到下一页面
                            intent.putExtra(TYPE, "1");
                            startActivity(intent);
                        } else {
                            Toast.makeText(InputActivity.this,"输入栏内容过多，请控制在80个字符以内再生成条形码！",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }
    }

    public class PasteListener implements View.OnClickListener {
        public void onClick(View v) {
            //获取系统剪贴板服务
            ClipboardManager clipboardManager = (ClipboardManager)InputActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboardManager != null) {
                // 获取剪贴板的剪贴数据集
                ClipData clipData = clipboardManager.getPrimaryClip();
                if (clipData != null && clipData.getItemCount() > 0) {
                    // 从数据集中获取（粘贴）第一条文本数据
                    ClipData.Item item = clipData.getItemAt(0);
                    if (item != null) {
                        String content = item.getText().toString();
                        EditText multiText = findViewById(R.id.ResultText);
                        int idx = multiText.getSelectionStart();
                        Editable editable = multiText.getText();
                        editable.insert(idx, content);
                        multiText.setSelection(idx + content.length());
                    }
                }
            }
        }
    }

    public class ClearListener implements View.OnClickListener {
        public void onClick(View v) {
            EditText multiText = findViewById(R.id.ResultText);   //获取输入内容
            multiText.setText("");
        }
    }

    public class changeXmlListener implements View.OnClickListener {
        public void onClick(View v) {
            finish();
        }
    }
}
