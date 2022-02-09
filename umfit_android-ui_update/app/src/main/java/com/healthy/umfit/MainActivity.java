package com.healthy.umfit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.healthy.umfit.entity.User;
import com.healthy.umfit.entity.UserPrescription;
import com.healthy.umfit.utils.CommonUtilities;
import com.healthy.umfit.utils.OkHttpClientConnection;
import com.healthy.umfit.utils.SharedHandler;
import com.healthy.umfit.utils.SharedPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

import static com.healthy.umfit.TagName.*;
import static com.healthy.umfit.utils.CommonUtilities.*;

public class MainActivity extends AppCompatActivity implements
        WorkoutFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener,
        NotificationsFragment.OnFragmentInteractionListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private ActionBar toolbar;
    private SharedPreferencesManager sharedPrefObj;

    private OkHttpClientConnection okHttpClientConnectionObj;
    private Handler handlerProfileObj, handlerExerciseObj;
    private Runnable runnableProfileObj, runnableExerciseObj;

    private User userObj;
    private String request_type = null;

    //prevent reloading of bottom menu (default index is 0 : home)
    private static int currentBMIndex = 0, pressedBMIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
//        sharedPrefObj.clearAllPref(true);
        sharedPrefObj = new SharedPreferencesManager(MainActivity.this);

        toolbar = getSupportActionBar();
        toolbar.hide();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);

        registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));

        if(sharedPrefObj.getPref(KEY_LOGIN) != null) {
            userObj = new User(sharedPrefObj.getPref(KEY_LOGIN));
            try {
                Log.d("Test", userObj.getUserToken());
            }catch(Exception e){
                Log.d("Test","UserToken");
            }
            try {
                Log.d("Test", userObj.getUserId());
            }catch(Exception e){
                Log.d("Test","UserId");
            }
            try {
                Log.d("Test", userObj.getEmail());
            }catch(Exception e){
                Log.d("Test","Email");
            }
            try {
                Log.d("Test", userObj.getPhoneNumber());
            }catch(Exception e){
                Log.d("Test","Phone Number");
            }
            try {
                Log.d("Test",userObj.getUserPrescription().toString());
            }catch(Exception e){
                Log.d("Test","Prescription");
            }
            userPrescription = userObj.getUserPrescription();
        }

        loadFragment(new HomeFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_status:
                    pressedBMIndex = 0;
                    fragment = new HomeFragment();
                    break;
                case R.id.navigation_workout:
                    pressedBMIndex = 1;
                    fragment = new WorkoutFragment();
                    break;
                case R.id.navigation_stats:
                    pressedBMIndex = 2;
                    fragment = new StatsFragment();
                    break;
                case R.id.navigation_profile:
                    pressedBMIndex = 3;
                    fragment = new ProfileFragment();
                    break;
            }

            if (currentBMIndex == pressedBMIndex) {

                //do nothing

            } else if (fragment != null) {
                currentBMIndex = pressedBMIndex;
                loadFragment(fragment);
                return true;
            }
            return false;
        }
    };


    private void loadFragment(Fragment fragment) {
        //load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //you can leave it empty
    }

    private void displayNotificationDialog(String notiType) {
        String title = "", message = "";
        if (notiType.equalsIgnoreCase(KEY_NT_DAILY_EXERCISE_COMPLETED)) {
            title = getResources().getString(R.string.txt_daily_mission_completed);
            message = getResources().getString(R.string.txt_notification_msg1);
        } else if (notiType.equalsIgnoreCase(KEY_NT_WEEKLY_EXERCISE_COMPLETED)) {
            title = getResources().getString(R.string.txt_weekly_mission_completed);
            message = getResources().getString(R.string.txt_notification_msg2);
        } else if (notiType.equalsIgnoreCase(KEY_NT_EXERCISE_REMINDER)) {
            title = getResources().getString(R.string.txt_reminder);
            message = getResources().getString(R.string.txt_notification_msg3);
        } else if (notiType.equalsIgnoreCase(KEY_NT_HEART_RATE_EXCEED)) {
            title = getResources().getString(R.string.txt_warning);
            message = getResources().getString(R.string.txt_notification_msg4);
        }else if(notiType.equalsIgnoreCase(KEY_NT_SYNC_DATA)){
            title = getResources().getString(R.string.txt_warning);
            message = getResources().getString(R.string.txt_notification_sync_data);
        }else if(notiType.equalsIgnoreCase(KEY_NT_PRESCIPTION_REMINDER)){
            title = getResources().getString(R.string.txt_reminder);
            message = getResources().getString(R.string.txt_notification_prescription);
        }

        showPositiveDialog(MainActivity.this, title, message);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.hasExtra("notiType")) {
            String notiType = intent.getStringExtra("notiType");

            displayNotificationDialog(notiType);

            clearCurrentBMIndex();
        }
    }

    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(DISPLAY_MESSAGE_ACTION)) {
                String notiType = intent.getStringExtra("notiType");

                if (notiType != null) {
                    displayNotificationDialog(notiType);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            unregisterReceiver(mHandleMessageReceiver);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    public static void clearCurrentBMIndex() {
        currentBMIndex = -1;
    }

}
