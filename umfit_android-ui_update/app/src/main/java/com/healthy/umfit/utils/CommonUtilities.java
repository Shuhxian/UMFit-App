package com.healthy.umfit.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthy.umfit.R;
import com.healthy.umfit.entity.User;
import com.healthy.umfit.entity.UserPrescription;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.MediaType;

public class CommonUtilities {
    public static final String TAG = CommonUtilities.class.getSimpleName();
    public static final String huamiAccessToken = "AQVBQDpyQktGXip6SltGeiouXAQABAAAAAAN3AWzf3cnGXMxO18qpc-XBaUp56-T4-eDddfdynuAmaOPcgUzqTytaPTca64XeyH6C8DQeiOMbuTpemOg07SQ0EpdAr1unZ5ofcNhV509Yg66DVHpgUKmeECtJ05zQ8o4oGQBTUaHY7QFt3cXGYAH8_vMwuCtSaSqBR4IZ93G-";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String APP_ID = "3797751666123158";
    public static final String APP_SECRET = "YjZlNmY1ODM2ZDhmMTkzNQ==";
    public static final String DISPLAY_MESSAGE_ACTION = "com.healthy.umfit.DISPLAY_MESSAGE";
    public static final int POST_DELAY_TIMER = 500;
    public static SharedPreferencesManager sharedPrefObj = null;
    public static UserPrescription userPrescription = null;
    public static User commonUser = null;


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getCurrentDateByPattern(String pattern) {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.ENGLISH);

        return df.format(c);

    }

    public static String getDateByTimestamp(String pattern, String timestamp) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timestamp) * 1000);
        String date = DateFormat.format(pattern, cal).toString();
        return date;
    }

    public static String getFirstDayByCurrentMonth() {
        return getCurrentDateByPattern("yyyy") + "-" + getCurrentDateByPattern("MM") + "-01";
    }

    public static String getDayOfTheWeek(String pattern, int day){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);

        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, day);

        String date = DateFormat.format(pattern, cal).toString();

        return date;
    }

    public static String convertDateTime(String datetime, String inputPattern, String outputPattern){
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.ENGLISH);

        Date date = null;
        String formattedDateTime = "";
        try{
            date = inputFormat.parse(datetime);
            formattedDateTime = outputFormat.format(date);
        }catch (ParseException e){
            e.printStackTrace();
        }

        return formattedDateTime;
    }

    public static long getDatesDifference(String dateStr) {
        Date date1 = new Date(System.currentTimeMillis());
        Date date2 = new Date(Long.parseLong(dateStr) * 1000);

        Log.d(TAG, "date1: " + date1 + " date2: " + date2 + " datestr: " + dateStr + " syscurre: " + System.currentTimeMillis());

        return (date1.getTime() / 1000 / 60 / 60) - (date2.getTime() / 1000 / 60 / 60);
    }

    public static String getCurrentTimeZone() {
        return TimeZone.getDefault().getID();
    }

    public static String getCurrentTimeStamp(){
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        return ts;
    }

    public static void showAlertDialog(final Activity activity, String message, final boolean finish) {
        final android.app.AlertDialog ShowAlertDialog;
        LayoutInflater li = LayoutInflater.from(activity);
        View promptsView = li.inflate(R.layout.dialog_common, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final LinearLayout llRoot = promptsView.findViewById(R.id.ll_root);
        final Button btnOk = promptsView.findViewById(R.id.btn_ok);
        final TextView tvMsg = promptsView.findViewById(R.id.tv_msg);

        try {
            tvMsg.setText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ShowAlertDialog = alertDialogBuilder.create();
        ShowAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ShowAlertDialog.show();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAlertDialog.dismiss();

                if (finish) {
                    activity.finish();
                }
            }
        });
    }

    public static void showPositiveDialog(final Activity activity, String title, String message) {
        AlertDialog dialog;
        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

// 2. Chain together various setter methods to set the dialog characteristics
        builder.setTitle(title);
        builder.setMessage(message);
//                .setTitle(R.string.dialog_title);

        builder.setPositiveButton(R.string.txt_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                dialog.dismiss();
            }
        });
//        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                // User cancelled the dialog
//            }
//        });
// 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

//    public static String MD5(String md5){
//       try{
//           MessageDigest md = MessageDigest.getInstance("MD5");
//           byte[] array = md.digest(md5.getBytes());
//           StringBuilder sb = new StringBuilder();
//
//           for(byte anArray: array){
//               sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1,3));
//           }
//
//           return sb.toString();
//       }catch (java.security.NoSuchAlgorithmException e){
//           Log.d(TAG, "md5 error: " + e);
//       }
//
//       return null;
//    }

    public static void displayMessageWithData(Context context, String notiType) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra("notiType", notiType);

        context.sendBroadcast(intent);
    }

    public static boolean isUpdateRequired(int startTime, int endTime) {
        int difference = endTime - startTime;
        Log.d(TAG, "starttime: " + startTime + " endtime: " + endTime + " diff: " + difference);

        if (difference > (5 * 60)) {
            return true;
        } else {
            return false;
        }

    }
}
