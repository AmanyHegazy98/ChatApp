package com.amany.chat.data.Notifications;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;

import com.amany.chat.ui.activities.RingtonePage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseCall extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String nameCallFrom = remoteMessage.getData().get("namecallfrom");
        String photo=remoteMessage.getData().get("imageview");
        String sendTo=remoteMessage.getData().get("sentto");
        Intent intent=new Intent(this, RingtonePage.class);
        //noor
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        intent.putExtra("callFrom",nameCallFrom);
        intent.putExtra("image",photo);
        intent.putExtra("callTo",sendTo);

        startActivity(intent);

    }
}
