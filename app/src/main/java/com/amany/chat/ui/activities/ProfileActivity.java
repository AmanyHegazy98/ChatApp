package com.amany.chat.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amany.chat.R;
import com.amany.chat.data.model.UserModel;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private ImageView cardImage,imageActive;
    private TextView name,job;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    LinearLayout message_btn ,call;
    private Intent intent;
    private List<UserModel> userModelList;
    String userid;
    private final static String default_notification_channel_id = "default";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        cardImage = findViewById(R.id.imageView11);
        name = findViewById(R.id.textView);
        message_btn = findViewById(R.id.button3);
        job=findViewById(R.id.textView2);
        imageActive=findViewById(R.id.imageView12);
        call=findViewById(R.id.button2);


        intent = getIntent();
        userid = intent.getStringExtra("id");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                UserModel userModel = snapshot.getValue(UserModel.class);
                name.setText(userModel.getName());


                if (userModel.getImage().equals("default")) {
                    cardImage.setImageResource(R.drawable.images);

                } else {
                    Glide.with(ProfileActivity.this).load(userModel.getImage()).into(cardImage);
                }
                if (userModel.getJob().equals("")){
                    job.setText("No job");


                }
                else{
                    job.setText(userModel.getJob());
                }


                if(userModel.getStatus().equals("online"))

                {
                    //Glide.with(context).load(context.getDrawable(R.drawable.ic_ellipse_1)).into(holder.imageActive);
                    imageActive.setImageResource(R.drawable.ic_ellipse_1);

                }else{
                    imageActive.setImageResource(R.drawable.ic_ellipse_4);

                    // Glide.with(context).load(context.getDrawable(R.drawable.ic_ellipse_4)).into(holder.imageActive);
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        message_btn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MainChatActivity.class);
            intent.putExtra("id", userid);

            startActivity(intent);
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(ProfileActivity.this,VoiceChat.class);
                intent1.putExtra("id2", userid);
                startActivity(intent1);


                    Uri alarmSound = RingtoneManager. getDefaultUri (RingtoneManager. TYPE_NOTIFICATION );
                    MediaPlayer mp = MediaPlayer. create (getApplicationContext(), alarmSound);
                    mp.start();
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ProfileActivity. this, default_notification_channel_id )
                                    .setSmallIcon(R.drawable. ic_launcher_foreground )
                                    .setContentTitle( "Test" )
                                    .setContentText( "Hello! This is my first push notification" ) ;
                    NotificationManager mNotificationManager = (NotificationManager)
                            getSystemService(Context. NOTIFICATION_SERVICE );
                    mNotificationManager.notify(( int ) System. currentTimeMillis () ,
                            mBuilder.build());



            }
        });
    }
}