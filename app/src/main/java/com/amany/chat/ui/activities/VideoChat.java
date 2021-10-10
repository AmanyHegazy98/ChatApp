package com.amany.chat.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.amany.chat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;


public class VideoChat extends AppCompatActivity {

    private RtcEngine mRtcEngine;
    String userid;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FrameLayout container1;
    SurfaceView surfaceView1;
    ImageView mute, unMute,  endCall, video, noVideo,switchCamera;

    // Java
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
        setContentView(R.layout.activity_video_chat);

        mute = findViewById(R.id.mute);
        unMute = findViewById(R.id.unMute);
        video = findViewById(R.id.video_call);
        endCall = findViewById(R.id.endCall);
        noVideo = findViewById(R.id.noVideo);
        switchCamera = findViewById(R.id.switchCamera);

        userid = getIntent().getStringExtra("id3");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User").child(userid);
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)) {
            initializeAndJoinChannel();
        }

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


        endCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRtcEngine.leaveChannel();
                onBackPressed();

            }
        });
        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mRtcEngine.switchCamera();


            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRtcEngine.muteLocalVideoStream(false);

                video.setVisibility(View.GONE);
                noVideo.setVisibility(View.VISIBLE);


                FrameLayout container = findViewById(R.id.local_video_view_container);
                container.setVisibility(View.VISIBLE);
                SurfaceView videoSurface = (SurfaceView) container.getChildAt(0);
                videoSurface.setZOrderMediaOverlay(true);
                videoSurface.setVisibility(View.VISIBLE);
              /*  FrameLayout container2=findViewById(R.id.remote_video_view_container);
               ImageView imageView = new ImageView(VideoChat.this);
                imageView.setImageResource(R.drawable.images);
                container2.addView(imageView);*/

            }
        });
        noVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRtcEngine.muteLocalVideoStream(true);

                video.setVisibility(View.VISIBLE);
                noVideo.setVisibility(View.GONE);

                FrameLayout container = findViewById(R.id.local_video_view_container);
                container.setVisibility(View.GONE );
                SurfaceView videoSurface = (SurfaceView) container.getChildAt(0);
                videoSurface.setZOrderMediaOverlay(false);
                videoSurface.setVisibility(View.GONE );
               /* FrameLayout container2=findViewById(R.id.remote_video_view_container);
                ImageView imageView = new ImageView(VideoChat.this);
                imageView.setImageResource(R.drawable.images);
                container2.removeView(imageView);*/



            }
        });


    }


    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        // Listen for the remote user joining the channel to get the uid of the user.
        public void onUserJoined(int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Call setupRemoteVideo to set the remote video view after getting uid from the onUserJoined callback.
                    setupRemoteVideo(uid);
                }
            });
        }
        @Override
        public void onUserOffline(int uid, int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserLeft();
                }
            });
        }

        // remote user has toggled their video
        @Override
        public void onRemoteVideoStateChanged(final int uid, final int state, int reason, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserVideoToggle(state);
                }
            });
        }
    };
    private void initializeAndJoinChannel() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
        } catch (Exception e) {
            throw new RuntimeException("Check the error.");
        }

        // By default, video is disabled, and you need to call enableVideo to start a video stream.
        mRtcEngine.enableVideo();

         container1 = findViewById(R.id.local_video_view_container);
        // Call CreateRendererView to create a SurfaceView object and add it as a child to the FrameLayout.
         surfaceView1 = RtcEngine.CreateRendererView(getBaseContext());
        container1.addView(surfaceView1);
        surfaceView1.setZOrderMediaOverlay(true);
        // Pass the SurfaceView object to Agora so that it renders the local video.
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView1, VideoCanvas.RENDER_MODE_FIT, 0));
        // Join the channel with a token.
        mRtcEngine.joinChannel(getString(R.string.agora_access_token), "two", "", 0);
    }
    private void setupRemoteVideo(int uid) {
        FrameLayout container = findViewById(R.id.remote_video_view_container);
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());

        container.addView(surfaceView);


        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid));


    }
    protected void onDestroy() {
        super.onDestroy();

        mRtcEngine.leaveChannel();
        mRtcEngine.destroy();
    }
    private void onRemoteUserVideoToggle( int state) {
        FrameLayout videoContainer = findViewById(R.id.remote_video_view_container);

        SurfaceView videoSurface = (SurfaceView) videoContainer.getChildAt(0);
        videoSurface.setVisibility(state == 0 ? View.GONE : View.VISIBLE);

        // add an icon to let the other user know remote video has been disabled
        if(state == 0){
            ImageView noCamera = new ImageView(this);
            noCamera.setImageResource(R.drawable.images);
            videoContainer.addView(noCamera);
        } else {
            ImageView noCamera = (ImageView) videoContainer.getChildAt(1);
            if(noCamera != null) {
                videoContainer.removeView(noCamera);
            }
        }
    }
    private void onRemoteUserLeft() {
        removeVideo(R.id.remote_video_view_container);
    }

    private void removeVideo(int containerID) {
        FrameLayout videoContainer = findViewById(containerID);
       // videoContainer.removeAllViews();
    }
}