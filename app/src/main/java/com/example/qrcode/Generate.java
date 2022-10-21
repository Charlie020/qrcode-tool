package com.example.qrcode;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Generate extends AppCompatActivity {
    Button changeButton;
    Bitmap bitmap;
    ImageView img;
    ImageView ret;


    private static final int MY_PERMISSIONS_REQUEST_WRITE = 1;
//    private static final int MY_PERMISSIONS_REQUEST_READ = 2;
    Button SaveButton;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //将状态栏透明
        StatusBar statusBar = new StatusBar(Generate.this);
        statusBar.setStatusBarColor(R.color.transparent);

        setContentView(R.layout.generatelayout);

        //去除默认标题栏
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }

        // 初始化界面
        Intent intent = getIntent();
        String text = intent.getStringExtra(InputActivity.Text);
        String Type = intent.getStringExtra(InputActivity.TYPE);
        changeButton = findViewById(R.id.ChangeCode);
        changeButton.setOnClickListener(new ChangeListener());
        if (Type.equals("2")) {
            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.njupt);
            bitmap = GenerateUtils.createQRCode(text,1000,1000,logo);
            changeButton.setText("转为条形码");
        } else {
            bitmap = GenerateUtils.createODcode(text);
            changeButton.setText("转为二维码");
        }
        img = findViewById(R.id.img);
        img.setImageBitmap(bitmap);

        // 保存
        SaveButton = findViewById(R.id.Save);
        SaveButton.setOnClickListener(new SaveListener());

        //返回
        ret = findViewById(R.id.Ret);
        ret.setOnClickListener(new changeXmlListener());
    }



    // 保存到相册
    public class SaveListener implements View.OnClickListener {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void onClick(View view) {
            // 获得权限
            if (ContextCompat.checkSelfPermission(Generate.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Save();
            } else { //未获得权限
                // 若未获得写权限
                if (ContextCompat.checkSelfPermission(Generate.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Generate.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE);
                }
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Save();
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Generate.this);
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

    public void Save() {
        SavePhoto savePhoto = new SavePhoto(Generate.this);
        savePhoto.SaveBitmapFromView(img);
        Toast.makeText(Generate.this,"保存成功", Toast.LENGTH_SHORT).show();
    }

    public class SavePhoto{
        //存调用该类的活动
        Context context;
        public SavePhoto(Context context) {
            this.context = context;
        }
        //保存文件的方法：
        public void SaveBitmapFromView(View view) throws ParseException {
            int w = view.getWidth();
            int h = view.getHeight();
            Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bmp);
            view.draw(c);
            // 缩小图片
            Matrix matrix = new Matrix();
            matrix.postScale(0.5f,0.5f); //长和宽放大缩小的比例
            bmp = Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,true);
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            f.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            saveBitmap(bmp, f.format(new Date()) + ".JPEG");
        }

        public void saveBitmap(Bitmap bitmap, String bitName){
            String fileName;
            fileName = "/storage/emulated/0/Android/data/com.example.qrcode/"+bitName;

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            try {
                OutputStream out = context.getContentResolver().openOutputStream(uri);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
            } catch (IOException e) {
                Toast.makeText(context,"图片保存失败", Toast.LENGTH_SHORT);
                e.printStackTrace();
            }
        }
    }

    // 设置切换二维码和条形码的按钮
    public class ChangeListener implements View.OnClickListener {
        public void onClick(View v) {
            Button changeButton = findViewById(R.id.ChangeCode);
            Intent intent = getIntent();
            String text = intent.getStringExtra(InputActivity.Text);
            Bitmap bitmap;
            ImageView img = findViewById(R.id.img);
            if (changeButton.getText().equals("转为二维码")) {
                Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.njupt);
                bitmap = GenerateUtils.createQRCode(text,1000,1000,logo);
                img.setImageBitmap(bitmap);
                changeButton.setText("转为条形码");
            } else {
                boolean flag = true;
                int n = text.length();
                for (int i = 0; i < n; i++) {
                    if (text.charAt(i) < 32 || text.charAt(i) > 127) {
                        flag = false;
                        break;
                    }
                }
                if (!flag) {
                    Toast.makeText(Generate.this,"二维码内容含有中文字符或换行符，不能转为条形码。",Toast.LENGTH_SHORT).show();
                } else {
                    bitmap = GenerateUtils.createODcode(text);
                    img.setImageBitmap(bitmap);
                    changeButton.setText("转为二维码");
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
