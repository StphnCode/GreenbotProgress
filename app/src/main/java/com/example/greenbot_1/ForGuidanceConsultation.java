package com.example.greenbot_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.StringValue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import io.grpc.okhttp.internal.Util;

public class ForGuidanceConsultation extends AppCompatActivity {
    ImageView btnBrBack;
    AppCompatButton btnSelectDate, btnSelectTime, btnSet;
    String selectedDate = "";
    String selectedTime = "";
    String dayOfTheWeek = "";
    int currentYear, currentMonth, currentDay;
    FirebaseUser currentUser;
    FirebaseDatabase fDatabase;
    DatabaseReference ref;
    String name="";
    private String userUID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_for_guidance_consultation);

        btnBrBack = findViewById(R.id.btnBrBack);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSelectTime = findViewById(R.id.btnSelectTime);
        btnSet = findViewById(R.id.btnSet);
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userUID = currentUser.getUid();
//        btnSelectDate.setText(selectedDate);

        ref = fDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                    String dbUID = dataSnapshot.child("userUID").getValue(String.class);
                    if(userUID.equals(dbUID)){

                        name = dataSnapshot.child("name").getValue(String.class);

                    }
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnBrBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForGuidanceConsultation.this, Help.class);
                startActivity(intent);
            }
        });

        btnSelectDate.setOnClickListener((v) -> selectDate());


        btnSelectTime.setOnClickListener((v) -> selectTime());

        btnSet.setOnClickListener((v) -> setSched());
    }
    private void selectDate(){
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                Date d_name = new Date(year, month, day-1);
                dayOfTheWeek = sdf.format(d_name);

                if(year < currentYear){
                    Utility.showToast(ForGuidanceConsultation.this, "Selected Date is invalid");
                }else if(year == currentYear && month < currentMonth && day < currentDay){
                    Utility.showToast(ForGuidanceConsultation.this, "Selected Date is invalid");
                }else if(year == currentYear && month == currentMonth && day <= currentDay){
                    Utility.showToast(ForGuidanceConsultation.this, "Selected Date is invalid");
                }else if(dayOfTheWeek.equals("Sunday")){
                    Utility.showToast(ForGuidanceConsultation.this, "Consultation Day is only from MON-SAT");
                }
                else{
                    selectedDate = String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day);

                    btnSelectDate.setText(selectedDate);


                }

            }
        }, currentYear, currentMonth, currentDay);

        dialog.show();
        dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
        dialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
    }

    private void selectTime(){
        if(dayOfTheWeek == ""){
            Utility.showToast(ForGuidanceConsultation.this, "Select Date");
        }else{
            TimePickerDialog dialog = new TimePickerDialog(this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                    String AM_PM;
                    String mins;
                    if(hours > 12){
                        AM_PM = "PM";
                        hours = hours - 12;
                    }else if (hours == 12){
                        AM_PM = "PM";
                    }else {
                        AM_PM = "AM";
                    }

                    if(minutes < 10){
                        mins = "0" + String.valueOf(minutes);
                    }else{
                        mins = String.valueOf(minutes);
                    }
                    if(hours < 7 && AM_PM == "AM" || hours > 5 && AM_PM == "PM"){
                        Utility.showToast(ForGuidanceConsultation.this, "Selected Time is invalid");
                        btnSelectTime.setText("SELECT TIME");
                    }else if(dayOfTheWeek.equals("Saturday")  && hours > 10 && AM_PM == "AM"){
                        Utility.showToast(ForGuidanceConsultation.this, "Consultation Hours during Saturday is only until 10:00AM");
                        btnSelectTime.setText("SELECT TIME");
                    }
                    else{
                        selectedTime = String.valueOf(hours)+":"+mins+" "+AM_PM;
                        btnSelectTime.setText(selectedTime);
                    }


                }
            }, Calendar.getInstance().get(Calendar.HOUR), Calendar.getInstance().get(Calendar.MINUTE), false);
            dialog.show();
            dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            dialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);

        }

    }

    private void setSched(){
        if(selectedDate.isEmpty() && selectedTime.isEmpty()){
            Utility.showToast(ForGuidanceConsultation.this, "Select Date and Time for your appointment");
        }else if(selectedDate.isEmpty()){
            Utility.showToast(ForGuidanceConsultation.this, "Select Date for your appointment");
        }else if(selectedTime.isEmpty()){
            Utility.showToast(ForGuidanceConsultation.this, "Select Time for your appointment");
        }else {
            DocumentReference documentReference = Utility.getCollectionReferenceForSchedAppointment().document();
            HashMap<String, Object> sched = new HashMap<>();
            sched.put("userName", name);
            sched.put("Date of appointment", selectedDate);
            sched.put("Time of appointment", selectedTime);

            documentReference.set(sched).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Utility.showToast(ForGuidanceConsultation.this, "Wait for the confirmation of your appointment schedule");
                        Intent intent = new Intent(ForGuidanceConsultation.this, StartConvo.class);
                        startActivity(intent);
                    }else{
                        Utility.showToast(ForGuidanceConsultation.this, "Retry");
                    }
                }
            });

        }


    }
}