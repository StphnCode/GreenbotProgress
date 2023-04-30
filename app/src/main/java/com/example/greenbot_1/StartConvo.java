package com.example.greenbot_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import org.w3c.dom.Document;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class StartConvo extends AppCompatActivity {

    private AdapterGreenbot adapterGreenbot = new AdapterGreenbot();
    List<String> list;

    RecyclerView recyclerView;
    EditText messageEditText;
    FirebaseUser currentUser;
    FirebaseFirestore database;
    FirebaseDatabase fDatabase;
    DatabaseReference ref;
    ImageButton btnMenu, btnCall, btnInfo, btnSend;
    RecyclerView rvChatList;
    private String userUID = "";
    private String name = "";
    String currentDateTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_start_convo);

        btnMenu = findViewById(R.id.menuBtn);
        btnCall = findViewById(R.id.callBtn);
        btnInfo = findViewById(R.id.infoBtn);
        btnSend = findViewById(R.id.sendBtn);
        EditText enterMsg = findViewById(R.id.enterMsg);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userUID = currentUser.getUid();
        database = FirebaseFirestore.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyy - hh:mm a", Locale.getDefault());
        currentDateTime = dateFormat.format(new Date());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.254.114:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GreenbotAPI apiService = retrofit.create(GreenbotAPI.class);


        rvChatList = findViewById(R.id.rvChatList);
        rvChatList.setAdapter(adapterGreenbot);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        rvChatList.setLayoutManager(llm);

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




        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartConvo.this, Menu.class);
                startActivity(intent);
            }
        });
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartConvo.this, SOS_UI.class);
                startActivity(intent);
            }
        });
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartConvo.this, SelfCare.class);
                startActivity(intent);
            }
        });

        btnSend.setOnClickListener((v)->{
            if(enterMsg.getText().toString().isEmpty()) {
                Toast.makeText(StartConvo.this, "Please enter a text", Toast.LENGTH_LONG).show();
                return;
            }

            String message = enterMsg.getText().toString();
            adapterGreenbot.addChatToList(message);
            rvChatList.smoothScrollToPosition(adapterGreenbot.getItemCount());


            DocumentReference documentReference = Utility.getCollectionReferenceForChat().document();
            HashMap<String, Object> chat = new HashMap<>();
            chat.put("userId", currentUser.getUid());
            chat.put("userName", name);
            chat.put("message", message);
            chat.put("timestamp", currentDateTime);
//            database.collection("chat").add(chat);
            documentReference.set(chat).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        return;
                    }else{
                        return;
                    }
                }
            });


            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    apiService.chatWithTheBit(message).enqueue(callBack);
                }
            };

            new Thread(runnable).start();

            enterMsg.getText().clear();
        });
    }

    private Callback<ChatResponse> callBack = new Callback<ChatResponse>() {
        @Override
        public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
                String chatbotResponse = response.body().chatBotReply;
                DocumentReference documentReference = Utility.getCollectionReferenceForChat().document();
                HashMap<String, Object> chat = new HashMap<>();
                chat.put("userId", "323chatbot");
                chat.put("userName", "Chatbot");
                chat.put("message", chatbotResponse);
                chat.put("timestamp", currentDateTime);
//                database.collection("chat").add(chat);

                documentReference.set(chat).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            return;
                        }else{
                            return;
                        }
                    }
                });
                adapterGreenbot.addChatToList(chatbotResponse);
                rvChatList.smoothScrollToPosition(adapterGreenbot.getItemCount());
//                Log.d("ChatbotResponse", chatbotResponse); // add this line to log the chatbot response
            } else {
                Toast.makeText(StartConvo.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<ChatResponse> call, Throwable t) {
            Toast.makeText(StartConvo.this, "Something went wrong", Toast.LENGTH_LONG).show();
            Log.e("ChatbotError", t.getMessage()); // add this line to log the error message
        }
    };

}