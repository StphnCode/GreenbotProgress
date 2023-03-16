package com.example.greenbot_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountSettings extends AppCompatActivity {
    TextView changeEmail, changePass;
    EditText accSettingsEmail, accSettingsPass;
    AppCompatButton btnSave;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_account_settings);

        accSettingsEmail = findViewById(R.id.accSettingsEmail);
        accSettingsPass = findViewById(R.id.accSettingsPass);
        btnBack = findViewById(R.id.btnAccSettingsBack);
        btnSave = findViewById(R.id.btnSave);
        changeEmail = findViewById(R.id.changeEmail);
        changePass = findViewById(R.id.changePass);

        accSettingsEmail.setFocusable(false);
        accSettingsPass.setFocusable(false);
        btnSave.setEnabled(false);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountSettings.this, Menu.class);
                startActivity(intent);
            }
        });

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accSettingsEmail.setFocusable(true);
                btnSave.setEnabled(true);
            }
        });
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accSettingsPass.setFocusable(true);
                btnSave.setEnabled(true);
            }
        });



    }
}