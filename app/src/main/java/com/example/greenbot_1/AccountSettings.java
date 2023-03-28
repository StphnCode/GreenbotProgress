package com.example.greenbot_1;

import androidx.annotation.NonNull;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountSettings extends AppCompatActivity {
    TextView changeEmail, changePass, txtYourName;
    EditText accSettingsEmail, accSettingsPass;
    AppCompatButton btnSave;
    ImageView btnBack;
    private FirebaseAuth mFirebaseAuth;
    DatabaseReference ref;
    FirebaseDatabase database;
    private String userUID = "";
    private String name = "";
    private String email = "";
    private String password = "";

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
        txtYourName = findViewById(R.id.txtYourName);

        accSettingsEmail.setEnabled(false);
        accSettingsPass.setEnabled(false);
        btnSave.setEnabled(false);
        mFirebaseAuth = FirebaseAuth.getInstance();
        userUID = mFirebaseAuth.getCurrentUser().getUid();

        ref = database.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                    String dbUID = dataSnapshot.child("userUID").getValue(String.class);
                    if(userUID.equals(dbUID)){

                        name = dataSnapshot.child("name").getValue(String.class);
                        email = mFirebaseAuth.getCurrentUser().getEmail();
                        password = dataSnapshot.child("password").getValue(String.class);

                        txtYourName.setText(name);
                        accSettingsEmail.setText(email);
                        accSettingsPass.setText(password);

                    }
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



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
                accSettingsEmail.setEnabled(true);
                btnSave.setEnabled(true);
            }
        });
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accSettingsPass.setFocusable(true);
                accSettingsPass.setEnabled(true);
                btnSave.setEnabled(true);
            }
        });



    }
}