package com.example.qrcode;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Generate extends AppCompatActivity {
    Button changeButton;
    Bitmap bitmap;
    ImageView img;
    ImageView ret;


    private static final int MY_PERMISSIONS_REQUEST_SAVE = 1;
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
        String text = intent.getStringExtra(TwoActivity.Text);
        String Type = intent.getStringExtra(TwoActivity.TYPE);
        changeButton = findViewById(R.id.ChangeCode);
        changeButton.setOnClickListener(new ChangeListener());
        if (Type.equals("2")) {
            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.njupt);
            bitmap = QRUtils.createQRCode(text,1000,1000,logo);
            changeButton.setText("转为条形码");
        } else {
            bitmap = QRUtils.createODcode(text);
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
        public void onClick(View view) {
            if (ContextCompat.checkSelfPermission(Generate.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(Generate.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(Generate.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(Generate.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(Generate.this, "请授予相关权限！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    ActivityCompat.requestPermissions(Generate.this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_SAVE);
                    ActivityCompat.requestPermissions(Generate.this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_SAVE);
                }
            } else {
                Save();
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
            Date date = new Date();
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            saveBitmap(bmp, f.format(date) + ".JPEG");
        }

        public void saveBitmap(Bitmap bitmap, String bitName){
            String fileName;
            File file;
            System.out.println(Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/"+bitName);
            fileName = Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/"+bitName;
            file = new File(fileName);
            FileOutputStream out;
            try {
                out = new FileOutputStream(file);
                // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
                if(bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                    out.flush();
                    out.close();
                    // 插入图库
//                    MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), bitName, null);
                }
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 发送广播，通知刷新图库的显示
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
        }
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SAVE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //授权成功
                    Save();
                } else {
                    //授权失败
                    Toast.makeText(this, "授权失败！", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    // 设置切换二维码和条形码的按钮
    public class ChangeListener implements View.OnClickListener {
        public void onClick(View v) {
            Button changeButton = findViewById(R.id.ChangeCode);
            Intent intent = getIntent();
            String text = intent.getStringExtra(TwoActivity.Text);
            Bitmap bitmap;
            ImageView img = findViewById(R.id.img);
            if (changeButton.getText().equals("转为二维码")) {
                Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.njupt);
                bitmap = QRUtils.createQRCode(text,1000,1000,logo);
                img.setImageBitmap(bitmap);
                changeButton.setText("转为条形码");
            } else {
                boolean flag = true;
                int n = text.length();
                for (int i = 0; i < n; i++) {
                    if (text.charAt(i) < 32 || text.charAt(i) > 127) flag = false;
                }
                if (!flag) {
                    Toast.makeText(Generate.this,"二维码内容含有中文字符和换行，不能转为条形码。",Toast.LENGTH_SHORT).show();
                } else {
                    bitmap = QRUtils.createODcode(text);
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
