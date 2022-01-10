package com.healthy.umfit;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.healthy.umfit.entity.Profile;
import com.healthy.umfit.utils.OkHttpClientConnection;
import com.healthy.umfit.utils.SharedPreferencesManager;

import java.io.IOException;
import java.security.KeyStore;
import java.util.Locale;

public class SplashScreenActivity extends Activity {
    public static final String TAG = SplashScreenActivity.class.getSimpleName();

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    private SharedPreferencesManager sharedPrefObj;
    private OkHttpClientConnection okHttpClientConnectionObj;
    private Handler handlerObj;
    private Runnable runnableObj;
    String currentLanguage = "en", currentLang;
    Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sharedPrefObj = new SharedPreferencesManager(SplashScreenActivity.this);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                setLocale();
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }

    private void setLocale() {
        Intent refresh = null;
        Resources res = getResources();
        Configuration conf = res.getConfiguration();
        if (!Locale.getDefault().getLanguage().equalsIgnoreCase("en") || !Locale.getDefault().getLanguage().equalsIgnoreCase("ms"))
        {
            conf.setLocale(Locale.ENGLISH);
        }
        else {
            conf.setLocale(Locale.getDefault());
        }
        conf.setLocale(Locale.getDefault());
        this.createConfigurationContext(conf);

        if (sharedPrefObj.isLoggedIn())
        {
            refresh = new Intent(SplashScreenActivity.this, MainActivity.class);
        }
        else
        {
            refresh = new Intent(SplashScreenActivity.this, LoginActivity.class);
        }

        refresh.putExtra(currentLang, Locale.getDefault().getLanguage());
        startActivity(refresh);

    }

//    private void run(String url, String body) throws IOException {
//        Log.d(TAG, "url: " + url + " body: " + body);
//
//        okHttpClientConnectionObj = new OkHttpClientConnection();
//
//        okHttpClientConnectionObj.postRequest(url, body);
//
//        callHandler();
//
//    }
//
//    private void callHandler() {
//        handlerObj = new Handler();
//
//        runnableObj = new Runnable() {
//            public void run() {
//                try {
//                    Log.d(TAG, "res: " + okHttpClientConnectionObj.getResult());
//                    if (okHttpClientConnectionObj.getResult().equalsIgnoreCase("")) {
//                        String result = okHttpClientConnectionObj.getResult();
//                        Log.d(TAG, "result: " + result);
//                        handlerObj.postDelayed(runnableObj, 5000);
//                    } else {
//                        handlerObj.removeCallbacks(runnableObj);
//                        Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
//                        startActivity(i);
//                        finish();
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        handlerObj.postDelayed(runnableObj, 5000);
//    }
}
