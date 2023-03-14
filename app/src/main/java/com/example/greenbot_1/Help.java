package com.example.greenbot_1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

public class Help extends AppCompatActivity {

    AppCompatButton hotlineBtn, guidanceBtn;
    AppCompatImageButton backBtn;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_hl_and_fgc);


        backBtn = findViewById(R.id.backBtn);
        hotlineBtn = findViewById(R.id.hotlineBtn);
        guidanceBtn = findViewById(R.id.guidanceBtn);

        hotlineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Help.this, MhcHotline.class);
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Help.this, StartConvo.class);
                startActivity(intent);
            }
        });

        guidanceBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Help.this, StartConvo.class);
                startActivity(intent);
            }
        });
    }
}
