package com.example.greenbot_1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenbot_1.StartConvo;

public class Privacy extends AppCompatActivity {

    private Button nxtBtn;
    private RadioButton radioButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_privacy);

        nxtBtn = findViewById(R.id.nxtBtn);
        radioButton = findViewById(R.id.radioButton);
        nxtBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(radioButton.isChecked()) {
                    openConvo();
                }else{
                    Utility.showToast(Privacy.this, "You must agree to the terms and condition to register your account");
                }


            }
        });

    }

    public void openConvo(){
        Intent intent = new Intent(Privacy.this, VerifyEmail.class);
        startActivity(intent);
    }

}