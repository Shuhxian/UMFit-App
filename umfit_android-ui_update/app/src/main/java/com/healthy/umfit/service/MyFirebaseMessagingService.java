package com.healthy.umfit.service;

import android.content.Context;
import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.healthy.umfit.MainActivity;
import com.healthy.umfit.R;
import com.healthy.umfit.entity.User;
import com.healthy.umfit.utils.NotificationUtils;
import com.healthy.umfit.utils.OkHttpClientConnection;
import com.healthy.umfit.utils.SharedPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.healthy.umfit.TagName.*;
import static com.healthy.umfit.utils.CommonUtilities.displayMessageWithData;

/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;
    private OkHttpClientConnection okHttpClientConnectionObj;
    private User user;
    private SharedPreferencesManager sharedPrefObj;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent("pushNotification");
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
//            JSONObject data = json.getJSONObject("data");

//            String notiType = data.getString("notiType");
            String notiType = json.getString("notiType");
            String title = "";
            String message = json.getString("message");
            String imageUrl = "";
            String timestamp = "";


            if(notiType.equalsIgnoreCase(KEY_NT_DAILY_EXERCISE_COMPLETED)){
                title = getResources().getString(R.string.txt_daily_mission_completed);
                message = getResources().getString(R.string.txt_notification_msg1);
            }else if(notiType.equalsIgnoreCase(KEY_NT_WEEKLY_EXERCISE_COMPLETED)){
                title = getResources().getString(R.string.txt_weekly_mission_completed);
                message = getResources().getString(R.string.txt_notification_msg2);
            }else if(notiType.equalsIgnoreCase(KEY_NT_EXERCISE_REMINDER)){
                title = getResources().getString(R.string.txt_reminder);
                message = getResources().getString(R.string.txt_notification_msg3);
            }else if(notiType.equalsIgnoreCase(KEY_NT_HEART_RATE_EXCEED)){
                title = getResources().getString(R.string.txt_warning);
                message = getResources().getString(R.string.txt_notification_msg4);
            }else if(notiType.equalsIgnoreCase(KEY_NT_SYNC_DATA)){
                title = getResources().getString(R.string.txt_warning);
            }else if(notiType.equalsIgnoreCase(KEY_NT_PRESCIPTION_REMINDER)){
                title = getResources().getString(R.string.txt_reminder);
            }


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
//                // app is in foreground, broadcast the push message
//                Intent pushNotification = new Intent("pushNotification");
//                pushNotification.putExtra("message", message);
//                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//
//                // play notification sound
//                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//                notificationUtils.playNotificationSound();

                displayMessageWithData(getApplicationContext(), notiType);

            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("notiType", notiType);

                // check for image attachment
//                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
//                } else {
//                    // image is present, show notification with image
//                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
//                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void sendRegistrationToServer(String token)
    {
        okHttpClientConnectionObj = new OkHttpClientConnection();
        sharedPrefObj = new SharedPreferencesManager(getApplicationContext());
        if (sharedPrefObj.getPref(KEY_LOGIN) != null)
        {
            user = new User(sharedPrefObj.getPref(KEY_LOGIN));
            String url = hostUrl + KEY_USER + KEY_UPDATE_FCM_TOKEN + "/" + user.getUserId();
            String body = updtJSONString(token);
            okHttpClientConnectionObj.postRequest(url,body);
            if (okHttpClientConnectionObj.getStatusCode() == 200)
            {
                Toast.makeText(getApplicationContext(), "Updated FCM Token : " + token, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String updtJSONString( String newFCMToken){
        String jsonString = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fcmToken", newFCMToken);
            jsonString = jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
//    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
//        notificationUtils = new NotificationUtils(context);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
//    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
//        Toast.makeText(getApplicationContext(), "3newToken: " + s, Toast.LENGTH_SHORT).show();

        Log.d(TAG, "onNewToken: s: " + s);
        sendRegistrationToServer(s);

    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
        Log.d(TAG, "onMessageSent: s: " + s);
    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
        Log.d(TAG, "onSendError: s: " + s);
    }
}