package com.example.greenbot_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Menu extends AppCompatActivity {
    ImageView btnBack, userIcon;
    FloatingActionButton btnCam;
    TextView btnAccSettings, btnToS, btnPrivacy, btnLicenses, txtMenuName;
    AppCompatButton btnSignOut;
    private FirebaseAuth mFirebaseAuth;
    DatabaseReference ref;
    FirebaseDatabase database;
    private String userUID = "";
    private String name = "";

    public String getName() {
        return name;
    }

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
        btnSignOut = findViewById(R.id.btnSignOut);
        btnToS = findViewById(R.id.btnToS);
        txtMenuName = findViewById(R.id.txtMenuName);
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
                        txtMenuName.setText(name);
                    }
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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


        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this);
                builder.setTitle("Sign-out");
                builder.setMessage("Are you sure you want to sign-out?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mFirebaseAuth.signOut();
                        startActivity(new Intent(Menu.this, Login.class));
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();

            }
        });



    }
}