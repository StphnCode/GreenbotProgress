package com.example.greenbot_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Menu extends AppCompatActivity {
    ImageView btnBack, userIcon;
    FloatingActionButton btnCam;
    TextView btnAccSettings, btnToS, btnPrivacy, btnLicenses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_menu);

        btnAccSettings = findViewById(R.id.btnAccSettings);
        btnBack = findViewById(R.id.btnBack);
        btnCam = findViewById(R.id.btnCam);
        btnLicenses = findViewById(R.id.btnLicenses);
        btnPrivacy = findViewById(R.id.btnPrivacy);
        btnToS = findViewById(R.id.btnToS);

        btnAccSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, AccountSettings.class);
                startActivity(intent);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, StartConvo.class);
                startActivity(intent);
            }
        });

        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        btnPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, MenuPrivacyPolicy.class);
                startActivity(intent);
            }
        });

        btnToS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, TermsOfService.class);
                startActivity(intent);
            }
        });



    }
}