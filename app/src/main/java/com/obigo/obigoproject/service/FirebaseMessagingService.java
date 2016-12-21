package com.obigo.obigoproject.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.obigo.obigoproject.R;
import com.obigo.obigoproject.activity.MessageActivity;
import com.obigo.obigoproject.util.WakeUpScreenUtil;

/**
 * Created by O BI HE ROCK on 2016-12-14
 * 김용준, 최현욱
 * FirebaseMessagingService 서버에서 발송한 메시지를 받음
 * 전달받은 메시지를 클릭하면 MessageActivity로 이동
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{
    private static final String TAG = "FirebaseMsgService";

    // 서버에서 Push Message 전달시 받는 작업
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = remoteMessage.getData().get("title");
        String content = remoteMessage.getData().get("content");

        sendNotification(title, content);
    }

    // 메시지를 받음
    private void sendNotification(String title, String content) {
        // 화면이 켜지는 작업 - 10초 유지
        WakeUpScreenUtil.acquire(getApplicationContext(), 10000);

        // 메시지를 클릭하면 메시지 리스트로 이동
        Intent intent = new Intent(this, MessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // ID of notification
        notificationManager.notify(0, notificationBuilder.build());
    }
}