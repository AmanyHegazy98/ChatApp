package com.amany.chat.ui.activities;

import static io.agora.rtc.Constants.TIMBRE_TRANSFORMATION_RINGING;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;


public class VoiceChat extends AppCompatActivity {
    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
    private static final String LOG_TAG = "amany";
    private RtcEngine mRtcEngine;
    TextView name;
    ImageView mute, unMute, message, endCall, speaker, personPhoto, activeSpeaker;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    String userid;
    int volume = 100;
    boolean speaker1 = false;


// Sets the volume of the recorded signal.



    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
    };

    private static final int PERMISSION_REQ_ID = 22;

    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_chat);


        mute = findViewById(R.id.mute);
        unMute = findViewById(R.id.unMute);
        message = findViewById(R.id.message);
        endCall = findViewById(R.id.endCall);
        speaker = findViewById(R.id.speaker);
        personPhoto = findViewById(R.id.photo);
        name = findViewById(R.id.textView6);
        activeSpeaker = findViewById(R.id.active_speaker);


        userid = getIntent().getStringExtra("id2");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                name.setText(userModel.getName());


                if (userModel.getImage().equals("default")) {
                    personPhoto.setImageResource(R.drawable.images);

                } else {
                    Glide.with(VoiceChat.this).load(userModel.getImage()).into(personPhoto);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // Initialize RtcEngine after getting the permission.
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO)) {
            initAgoraEngineAndJoinChannel();
        }


        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VoiceChat.this, MainChatActivity.class);
                intent.putExtra("id", userid);
                startActivity(intent);
            }
        });
        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unMute.setVisibility(View.VISIBLE);
                mute.setVisibility(View.GONE);
                mRtcEngine.muteLocalAudioStream(true);


            }
        });
        unMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unMute.setVisibility(View.GONE);
                mute.setVisibility(View.VISIBLE);
                mRtcEngine.muteLocalAudioStream(false);
            }
        });


        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activeSpeaker.setVisibility(View.VISIBLE);
                speaker.setVisibility(View.GONE);

                mRtcEngine.setEnableSpeakerphone(true);


            }
        });
        activeSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activeSpeaker.setVisibility(View.GONE);
                speaker.setVisibility(View.VISIBLE);
                mRtcEngine.setEnableSpeakerphone(false);
            }
        });
        endCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRtcEngine.leaveChannel();
                onBackPressed();

            }
        });
    }

    private void initAgoraEngineAndJoinChannel() {
        initializeAndJoinChannel();
        //joinChannel();
    }


    private void initializeAndJoinChannel() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
        } catch (Exception e) {
            throw new RuntimeException("Check the error");
        }
        mRtcEngine.joinChannel(getString(R.string.agora_access_token), "two", "", 0);
    }


    private void leaveChannel() {
        mRtcEngine.leaveChannel();
    }

    protected void onDestroy() {
        super.onDestroy();
        leaveChannel();
        mRtcEngine.destroy();
    }


}
