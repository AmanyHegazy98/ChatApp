package com.amany.chat.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import com.amany.chat.R;
import com.amany.chat.ui.Adapter.PeopleAdapter;
import com.amany.chat.data.model.UserModel;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    EditText searchBar;
    ArrayList<UserModel> userModelArrayList;
    UserModel userModel;
    RecyclerView recyclerView;
    ImageView profile;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchBar = findViewById(R.id.search_bar);
        recyclerView = findViewById(R.id.recycler1);
        profile=findViewById(R.id.imageView8);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this, RecyclerView.VERTICAL, false));
        userModelArrayList = new ArrayList<>();
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString().toLowerCase());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel=snapshot.getValue(UserModel.class);

                if (userModel.getImage().equals("default")){
                    profile.setImageResource(R.drawable.images);
                }
                else{
                    Glide.with(getApplicationContext()).load(userModel.getImage()).into(profile);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void searchUsers(String s) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("User").orderByChild("search").startAt(s).endAt(s + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                userModelArrayList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    userModel = snapshot1.getValue(UserModel.class);
                    if (!userModel.getId().equals(firebaseUser.getUid())) {
                        userModelArrayList.add(userModel);
                    }

                }
                PeopleAdapter peopleAdapter = new PeopleAdapter(SearchActivity.this, userModelArrayList);
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(peopleAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}