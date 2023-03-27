package com.example.greenbot_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.internal.ClippableRoundedCornerLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Registration extends AppCompatActivity {
    Button btnRegister;
    TextView txtSignIn;
    EditText editEnterName, editEmail, editPassword, editRePassword;
    ProgressBar progressBar;
    //DatabaseReference dbReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://greenbot-1-default-rtdb.firebaseio.com/");
    DatabaseReference ref;
    FirebaseDatabase database;
    Member member;
    long maxId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_registration);

        btnRegister = findViewById(R.id.btnRegister);
        txtSignIn = findViewById(R.id.txtHaveAcc);
        progressBar = findViewById(R.id.progressBar);
        editEnterName = findViewById(R.id.editEnterName);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editRePassword = findViewById(R.id.editRePassword);
        ref = database.getInstance().getReference().child("Users");
        member = new Member();


        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);
            }
        });

        String text = "Already have an account? Sign-in";
        SpannableString ss = new SpannableString(text);
        ForegroundColorSpan fcsGreen = new ForegroundColorSpan(Color.GREEN);
        ss.setSpan(fcsGreen, 25,32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtSignIn.setText(ss);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxId=(snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnRegister.setOnClickListener(v-> userRegister());



    }
    void userRegister(){
        String name = editEnterName.getText().toString();
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        String rePassword = editRePassword.getText().toString();


        boolean isValidated = validateData(name, email, password, rePassword);
        if(checkEmpty(editEnterName)){
            editEnterName.setError("Required field!");

        }
        if(checkEmpty(editEmail)){
            editEmail.setError("Required field!");

        }
        if(checkEmpty(editPassword)){
            editPassword.setError("Required field!");

        }
        if(checkEmpty(editRePassword)){
            editRePassword.setError("Required field!");

        }
        if(!isValidated){
            return;
        }
        createAccountInFirebase(name, email, password);




    }

    void createAccountInFirebase(String name, String email, String password){
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Registration.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // creating account is done
                            Utility.showToast(Registration.this, "Successfully registered!");
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            sendDataToDB(name, email, password);
                            startNextActivity();

                        }else{
                            //failed
                            Utility.showToast(Registration.this, task.getException().getLocalizedMessage());

                        }
                    }
                });
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            btnRegister.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            btnRegister.setVisibility(View.VISIBLE);
        }
    }

    void sendDataToDB(String name, String email, String password){
        long id = maxId +1;
        member.setName(name);
        member.setEmail(email);
        member.setPassword(password);
        member.setId(id);



        ref.child(String.valueOf(id)).setValue(member);

    }



    boolean checkEmpty(EditText text){
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }


    boolean validateData(String name, String email, String password, String rePassword){
        // validate the data entered by the user
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editEmail.setError("Email is invalid!");
            return false;
        }
        if(password.length() < 8){
            editPassword.setError("Password must be at least 8 characters long.");
            return false;
        }
        if(!password.equals(rePassword)){
            editRePassword.setError("Password not matched!");
            return false;
        }
        return true;
    }

    private void startNextActivity() {
        Intent intent = new Intent(Registration.this, Birthday.class);
        startActivity(intent);
        finish();

    }
}