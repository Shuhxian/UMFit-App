package com.healthy.umfit.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class SharedHandler {

    private static SharedHandler instance;
    private HandlerThread backgroundThread;
    private Handler mainHandler;
    private Handler backgroundHandler;

    static
    {
        instance = new SharedHandler();
    }

    private SharedHandler()
    {
        backgroundThread = new HandlerThread("UmFitSharedHandler");
        backgroundThread.start();
        mainHandler = new Handler(Looper.getMainLooper());
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    public static Handler getMainHandler()
    {
        return instance.mainHandler;
    }

    public static Handler getBackgroundHandler()
    {
        return instance.backgroundHandler;
    }

    public static void runOnUiThread(Runnable runnable)
    {
        if (runnable != null)
        {
            instance.mainHandler.post(runnable);
        }
    }

    public static void runOnUiThread(Runnable runnable, long delayMillis)
    {
        if (runnable != null)
        {
            instance.mainHandler.postDelayed(runnable, delayMillis);
        }
    }

    public static void runBgThread(Runnable runnable)
    {
        if (runnable != null)
        {
            instance.backgroundHandler.post(runnable);
        }
    }

    public static void runBgThread(Runnable runnable, long delayMillis)
    {
        if (runnable != null)
        {
            instance.backgroundHandler.postDelayed(runnable, delayMillis);
        }
    }

}
