package com.amany.chat.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amany.chat.R;
import com.amany.chat.ui.Fragments.CallsFragment;
import com.amany.chat.ui.Fragments.ChatFragment;
import com.amany.chat.ui.Fragments.GroupsFragment;
import com.amany.chat.ui.Fragments.SettingsFragment;
import com.amany.chat.data.model.UserModel;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainPage extends AppCompatActivity {
    private LinearLayout callsLinear,chatLinear,groupsLinear,settingsLinear;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private ImageView imageView,search;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        getUserData();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new ChatFragment())
                    .commit();
        }
        // bind view
        callsLinear=findViewById(R.id.layout_calls);
        chatLinear=findViewById(R.id.layout_chat);
        groupsLinear=findViewById(R.id.layout_groups);
        settingsLinear=findViewById(R.id.layout_settings);
        imageView=findViewById(R.id.imageView8);
        search=findViewById(R.id.imageView7);

        callsLinear.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new CallsFragment())
                    .commit();
        });
        chatLinear.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ChatFragment())
                    .commit();
        });
        groupsLinear.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new GroupsFragment())
                    .commit();

        });
        settingsLinear.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new SettingsFragment())
                    .commit();
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainPage.this, SearchActivity.class);
                startActivity(intent);
            }
        });




    }

    private void getUserData() {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel=snapshot.getValue(UserModel.class);

                if (userModel.getImage().equals("default")){
                    imageView.setImageResource(R.drawable.images);
                }
                else{
                    Glide.with(getApplicationContext()).load(userModel.getImage()).into(imageView);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void status(String status) {
        reference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid()).child("status");
        reference.setValue(status);

    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        status("offline");
    }
}