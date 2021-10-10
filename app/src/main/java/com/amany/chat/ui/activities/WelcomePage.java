package com.amany.chat.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.amany.chat.R;

public class WelcomePage extends AppCompatActivity {
    private Button lets_go;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        lets_go=findViewById(R.id.button);



        lets_go.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomePage.this, MainPage.class);
            startActivity(intent);
        });
    }

}