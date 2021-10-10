package com.amany.chat.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amany.chat.R;
import com.amany.chat.data.model.UserModel;
import com.bumptech.glide.Glide;

public class RingtonePage extends AppCompatActivity {
    ImageView photo,answer,cancel;
    TextView name;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringtone_page);
        photo=findViewById(R.id.imageView21);
        answer=findViewById(R.id.imageView24);
        cancel=findViewById(R.id.imageView25);
        name=findViewById(R.id.textView15);
         intent=getIntent();
         String photoCall=intent.getStringExtra("image");
        String callFrom=intent.getStringExtra("callFrom");
        String userid=intent.getStringExtra("callTo");
        name.setText(callFrom);
        if (photoCall.equals("default")) {
            photo.setImageResource(R.drawable.images);

        } else {
            Glide.with(RingtonePage.this).load(photoCall).into(photo);
        }

        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(RingtonePage.this,VoiceChat.class);
                intent2.putExtra("id2",userid);
                startActivity(intent2);

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }
}