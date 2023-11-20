package com.example.greenbot_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenbot_1.adapter.ChatRecyclerAdapter;
import com.example.greenbot_1.model.ChatMessageModel;
import com.example.greenbot_1.model.ChatRoomModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class StartConvo extends AppCompatActivity {


    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private ChatRoomModel chatRoomModel;
    private ChatRecyclerAdapter adapter;
    FirebaseUser currentUser;
    FirebaseFirestore database;
    FirebaseDatabase fDatabase;
    DatabaseReference ref;
    ImageButton btnMenu, btnCall, btnInfo, btnSend;
    RecyclerView rvChatList;
    private EditText enterMsg;
    private String userUID = "";
    private String chatbotID = "";
    private String name = "";
    String currentDateTime = "";
    private String chatRoomId;

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
        enterMsg = findViewById(R.id.enterMsg);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userUID = currentUser.getUid();
        chatbotID = "greenbot";
        database = FirebaseFirestore.getInstance();
        fDatabase = FirebaseDatabase.getInstance();

        chatRoomId = Utility.getChatRoomId(userUID, chatbotID);

        getOrCreateChatroomModel();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyy - hh:mm a", Locale.getDefault());
        currentDateTime = dateFormat.format(new Date());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.254.103:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GreenbotAPI apiService = retrofit.create(GreenbotAPI.class);



        //recycler view
        rvChatList = findViewById(R.id.rvChatList);
        setupChatRecyclerView();

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

        // Inside `onCreate` method, when adding a new message:
        btnSend.setOnClickListener(v -> {
            if (enterMsg.getText().toString().isEmpty()) {
                Toast.makeText(StartConvo.this, "Please enter a text", Toast.LENGTH_LONG).show();
                return;
            }

            String userMessage = enterMsg.getText().toString();

            sendMessageToChatbot(userMessage);

            Call<ChatResponse> call = apiService.getChatbotResponse(userMessage);
            call.enqueue(callBack);
        });
    }

    private void setupChatRecyclerView() {
        Query query = Utility.getChatroomMessageReference(chatRoomId).orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>().setQuery(query, ChatMessageModel.class).build();
        adapter = new ChatRecyclerAdapter(options, getApplicationContext());
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setReverseLayout(true);
        rvChatList.setLayoutManager(llm);
        rvChatList.setAdapter(adapter);
        adapter.startListening();
    }

    private void sendMessageToChatbot(String userMessage) {
        chatRoomModel.setLastMessageTimestamp(Timestamp.now());
        chatRoomModel.setLastMessageSenderId(userUID);
        Utility.getChatroomReference(chatRoomId).set(chatRoomModel);

        ChatMessageModel chatMessageModel = new ChatMessageModel(userMessage, userUID, Timestamp.now());
        Utility.getChatroomMessageReference(chatRoomId).add(chatMessageModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    enterMsg.getText().clear();
                }
            }
        });

    }
    private void saveChatbotReply(String chatbotReply){
        chatRoomModel.setLastMessageTimestamp(Timestamp.now());
        chatRoomModel.setLastMessageSenderId(chatbotID);
        Utility.getChatroomReference(chatRoomId).set(chatRoomModel);

        ChatMessageModel chatMessageModel = new ChatMessageModel(chatbotReply, chatbotID, Timestamp.now());

        Utility.getChatroomMessageReference(chatRoomId).add(chatMessageModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    // Chatbot's response is successfully saved in the "Chatbot Replies" collection
                    Log.e("Chatbot Reply", "saved");
                }
            }
        });
    }


    private void getOrCreateChatroomModel(){
        Utility.getChatroomReference(chatRoomId).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                chatRoomModel = task.getResult().toObject(ChatRoomModel.class);
                if(chatRoomModel == null){
                    //first time chat between the user and greenbot
                    chatRoomModel = new ChatRoomModel(
                            chatRoomId,
                            Arrays.asList(userUID, chatbotID),
                            Timestamp.now(),
                            ""
                    );
                    Utility.getChatroomReference(chatRoomId).set(chatRoomModel);
                }
            }
        });
    }

    private Callback<ChatResponse> callBack = new Callback<ChatResponse>() {
        @Override
        public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
                String chatbotResponse = response.body().chatBotReply;

                // Log the chatbot response
                Log.d("ChatbotResponse", "Chatbot Response: " + chatbotResponse);

                // To save the chatbot's response in Firestore
                saveChatbotReply(chatbotResponse);


            } else {
                // Handle errors when chatbot response retrieval fails
                Log.e("ChatbotResponseError", "Failed to get chatbot response");
            }
        }

        @Override
        public void onFailure(Call<ChatResponse> call, Throwable t) {
            // Handle Retrofit or network errors
            Log.e("ChatbotError", t.getMessage());
        }
    };
    

}