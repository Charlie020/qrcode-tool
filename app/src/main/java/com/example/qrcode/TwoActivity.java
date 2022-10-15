package com.example.qrcode;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TwoActivity extends AppCompatActivity {
    public static final String TYPE = "1";
    public static final String Text = "tex";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twolayout);

        Button GenerateQRButton = findViewById(R.id.generateQR);
        GenerateQRButton.setOnClickListener((new GenerateListener()));
        Button GenerateODButton = findViewById(R.id.generateOD);
        GenerateODButton.setOnClickListener((new GenerateListener()));

        ImageView ret = findViewById(R.id.Ret);
        ret.setOnClickListener(new changeXmlListener());
    }

    public class GenerateListener implements View.OnClickListener {
        public void onClick(View v) {
            EditText multiText = findViewById(R.id.InputText);
            String text = multiText.getText().toString();
            if (text.length() == 0) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(TwoActivity.this);
                dialog.setTitle("Note");
                dialog.setMessage("输入栏为空，请输入内容。");
                dialog.setCancelable(false);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
                dialog.show();
            } else {
                Intent intent = new Intent(TwoActivity.this, Generate.class);
                intent.putExtra(Text, text);
                if (v.getId() == R.id.generateQR) intent.putExtra(TYPE, "2");
                else if (v.getId() == R.id.generateOD) intent.putExtra(TYPE, "1");
                startActivity(intent);
            }
        }
    }

    public class changeXmlListener implements View.OnClickListener {
        public void onClick(View v) {
            finish();
        }
    }
}
