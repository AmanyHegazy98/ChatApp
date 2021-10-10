package com.amany.chat.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.amany.chat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {
    private Button signup;
    private TextView login;
    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            Intent intent=new Intent(StartActivity.this, MainPage.class);
            startActivity(intent);
            finish();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        signup=findViewById(R.id.button);
        login=findViewById(R.id.sing_in);



        signup.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, SignUp.class);
            startActivity(intent);

        });
    login.setOnClickListener(v -> {
            Intent intent2 = new Intent(StartActivity.this, Login.class);
            startActivity(intent2);

        });


    }
}