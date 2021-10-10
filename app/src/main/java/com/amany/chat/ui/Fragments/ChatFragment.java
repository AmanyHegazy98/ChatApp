package com.amany.chat.ui.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amany.chat.ui.Adapter.MessageAdapter;
import com.amany.chat.ui.Adapter.PeopleAdapter;
import com.amany.chat.ui.activities.LoadingDialog;
import com.amany.chat.data.Notifications.Token;
import com.amany.chat.R;
import com.amany.chat.data.model.ChatModel;
import com.amany.chat.data.model.MainChatModel;
import com.amany.chat.data.model.UserModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {
    private MessageAdapter messageAdapter;
    private PeopleAdapter peopleAdapter;
    private RecyclerView recyclerView2,recyclerView;
    private TabLayout tabLayout;
    private List<UserModel> userModelList;
    private FirebaseUser firebaseUser;
    private List<ChatModel> chatModelList;
    private List<String> userList;
    private DatabaseReference reference;
    private    LoadingDialog  loadingDialog;



    public ChatFragment() {
    }


    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      loadingDialog=new LoadingDialog(getActivity());
        loadingDialog.startLoadingDialog();




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView2 = rootView.findViewById(R.id.recycler_view_chat);
        recyclerView= rootView.findViewById(R.id.recycler_view_people);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        tabLayout = rootView.findViewById(R.id.tabLayout);
        messageAdapter = new MessageAdapter(getContext(), chatModelList);
        recyclerView2.setAdapter(messageAdapter);
        recyclerView.setAdapter(peopleAdapter);
        reference =FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int unread=0;
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    MainChatModel chatModel=snapshot1.getValue(MainChatModel.class);
                if(chatModel.getReceiver().equals(firebaseUser.getUid())&& chatModel.getIsSeen().equals("false")){
                    unread++;

                }

                }
                if (unread==0){
                    tabLayout.getTabAt(0).setText("Message");
                }else
                {
                    tabLayout.getTabAt(0).setText("("+unread+") Message");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







        userModelList = new ArrayList<>();
        chatModelList = new ArrayList<>();

        checkChat();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    checkChat();
                    recyclerView.setVisibility(View.GONE);
                    recyclerView2.setVisibility(View.VISIBLE);

                } else {
                    readUser();
                    recyclerView2.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);


                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return rootView;
    }

    private void checkChat() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        userList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    MainChatModel chatModel = snapshot1.getValue(MainChatModel.class);
                    if (chatModel.getSender().equals(firebaseUser.getUid())) {
                        userList.add(chatModel.getReceiver());
                    }
                    if (chatModel.getReceiver().equals(firebaseUser.getUid())) {
                        userList.add(chatModel.getSender());
                    }


                }
                readChat();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void readUser() {
  loadingDialog.dismissDialog();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModelList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    UserModel userModel = snapshot1.getValue(UserModel.class);
                    if (!userModel.getId().equals(firebaseUser.getUid())) {
                        userModelList.add(userModel);

                    }
                }

                peopleAdapter = new PeopleAdapter(getContext(), userModelList);
                recyclerView.setAdapter(peopleAdapter);





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private  void updateToken(String token){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        reference.child(firebaseUser.getUid()).setValue(token1);

    }



    private void readChat() {
        chatModelList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                chatModelList.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    ChatModel chatModel = snapshot1.getValue(ChatModel.class);

                    if (userList.contains(chatModel.getId())) {
                        chatModelList.add(chatModel);
                    }


                    messageAdapter = new MessageAdapter(getContext(), chatModelList);
                    recyclerView2.setAdapter(messageAdapter);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            updateToken( task.getResult());
        });

loadingDialog.dismissDialog();
    }

/*
    @Override
    public void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
    }*/
}