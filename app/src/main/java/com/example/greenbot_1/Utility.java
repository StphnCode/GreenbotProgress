package com.example.greenbot_1;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

public class Utility {

    public static String currentUserId(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    static CollectionReference getCollectionReferenceForJournal(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("journal").document(currentUser.getUid()).collection("my_journal");
    }
    static CollectionReference getCollectionReferenceForMoodTracker(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("mood tracker").document(currentUser.getUid()).collection("daily_mood");
    }
//    static CollectionReference getCollectionReferenceForChat(){
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        return FirebaseFirestore.getInstance().collection("chat").document(currentUser.getUid()).collection("user_chat");
//    }
//    static CollectionReference getCollectionReferenceForConvo(){
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        return FirebaseFirestore.getInstance().collection("conversation").document(currentUser.getUid()).collection("user_convo");
//    }
    static CollectionReference getCollectionReferenceForChatbotReply(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("Chatbot Replies").document(currentUser.getUid()).collection("chatbot_replies");
    }
    static CollectionReference getCollectionReferenceForUserChat(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("conversations").document(currentUser.getUid()).collection("user_chat");
    }

    static CollectionReference getCollectionReferenceForSchedAppointment(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("schedule").document(currentUser.getUid()).collection("appointment_sched");
    }

    static DocumentReference getChatroomReference(String chatRoomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatRoomId);
    }

    public static String getChatRoomId(String userId, String chatbotId){
        if(userId.hashCode() < chatbotId.hashCode()){
            return userId+"_"+chatbotId;
        }else{
            return chatbotId+"_"+userId;
        }
    }
    public static CollectionReference getChatroomMessageReference(String chatroomId){
        return getChatroomReference(chatroomId).collection("chats");
    }

}
