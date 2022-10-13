package com.example.qrcode;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class TwoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twolayout);
        ImageView ret = findViewById(R.id.Ret);
        ret.setOnClickListener(new changeXmlListener());
    }
    public class changeXmlListener implements View.OnClickListener {
        public void onClick(View v) {
            Intent intent = new Intent(TwoActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
