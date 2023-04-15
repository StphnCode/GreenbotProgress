package com.example.greenbot_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    FirebaseUser user;
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
        user = mFirebaseAuth.getCurrentUser();

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
                AlertDialog.Builder changeEmailDialog = new AlertDialog.Builder(view.getContext());
                changeEmailDialog.setTitle("Change email");
                changeEmailDialog.setMessage("Do you want to change your email?");
                changeEmailDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        accSettingsEmail.setFocusable(true);
                        accSettingsEmail.setEnabled(true);
                        btnSave.setEnabled(true);
                    }
                });
                changeEmailDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                changeEmailDialog.show();

            }
        });
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder changePasswordDialog = new AlertDialog.Builder(view.getContext());
                changePasswordDialog.setTitle("Change password");
                changePasswordDialog.setMessage("Do you want to change your password?");
                changePasswordDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        accSettingsPass.setText("");
                        accSettingsPass.setFocusable(true);
                        accSettingsPass.setEnabled(true);

                        btnSave.setEnabled(true);
                    }
                });
                changePasswordDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                changePasswordDialog.show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newPassword = accSettingsPass.getText().toString();
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(view.getContext());
                saveDialog.setTitle("Save");
                saveDialog.setMessage("Do you want to save the changes made to your account?");

                saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(newPassword.length() >= 8){
                            user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Utility.showToast(AccountSettings.this, "Password changed successfully");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Utility.showToast(AccountSettings.this, "Password change failed");
                                }
                            });
                        }else{
                            Utility.showToast(AccountSettings.this, "Password must be at least 8 characters long");
                        }
                    }
                });
                saveDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                saveDialog.show();

            }
        });



    }
}