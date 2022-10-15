package com.example.qrcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button generate = this.findViewById(R.id.Generate);
        generate.setOnClickListener(new changeXmlListener());
    }

    public class changeXmlListener implements View.OnClickListener {
        public void onClick(View v) {
//            FragmentManager manager = getSupportFragmentManager();
//            FragmentTransaction trans = manager.beginTransaction();
//            trans.replace()
            Intent intent = new Intent(MainActivity.this, TwoActivity.class);
            startActivity(intent);
        }
    }
}