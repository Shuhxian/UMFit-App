package com.healthy.umfit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.healthy.umfit.entity.Activity;
import com.healthy.umfit.entity.HeartRate;

import com.healthy.umfit.entity.User;
import com.healthy.umfit.utils.SharedPreferencesManager;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.healthy.umfit.TagName.*;
import static com.healthy.umfit.utils.CommonUtilities.*;
import androidx.fragment.app.FragmentTransaction;

public class HomeFragment extends Fragment{
    private static final String TAG = HomeFragment.class.getSimpleName();

    private String urlHeartRate = "", urlActivities = "", urlSports = "";
    private ArrayList<HeartRate> heartRateList = new ArrayList<>();
    private ArrayList<Activity> activityList = new ArrayList<>();
    private ArrayList<Activity> workoutList = new ArrayList<>();
    private TextView tvExerciseSessions, tvTargetHeartRate, tvMaximumHeartRate, tvExerciseDuration, tvExerciseLastUpdated, tvSteps, tvWorkoutLastUpdated;
    private TextView tvExerciseSessionsDesc, tvTargetHeartRateDesc, tvMaximumHeartRateDesc, tvExerciseDurationDesc, tvStepsDesc;
    private AppCompatEditText edtInputDate;
    //    private LinearLayout llWorkout;
    private ProgressBar pbStatus;
    private ScrollView svStatus;
    private SwipeRefreshLayout srlStatus;
    private String request_type = null;
    private ProgressBar pbStepCounter;

    private String interval = "daily";
    private String inputDate = "";
    private String currentDate = "", currentTimezone = "", currentTimeStamp = "";
    private boolean isRequiredUpdate = false;
    private int sessionCount = 3, targetHeartRate = 125, maximumHeartRate = 165, exerciseDuration = 30, stepCount = 10000;
    private ArrayList<String> durationList = new ArrayList<>();

    private SharedPreferencesManager sharedPrefObj;

    private User userObj;

    private TextView syncMessage;

    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefObj = new SharedPreferencesManager(getActivity());

        AndroidNetworking.initialize(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        findViewById(rootView);
        setData();
        updatePageData();
        srlStatus.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isAdded()) {
                    updatePageData();
                }
            }
        });


        return rootView;
    }

    private void findViewById(View rootView) {
        srlStatus = rootView.findViewById(R.id.home_refresh_layout);
        svStatus = rootView.findViewById(R.id.sv_status);
        pbStatus = rootView.findViewById(R.id.home_refresh_progress);


        // Average heart rate (card 1)
        tvTargetHeartRate = rootView.findViewById(R.id.card1_title);
        tvTargetHeartRateDesc = rootView.findViewById(R.id.card1_desc);

        // Maximum heart rate(not shown)
        tvMaximumHeartRate = rootView.findViewById(R.id.card4_title);
        tvMaximumHeartRateDesc = rootView.findViewById(R.id.card4_desc);

        // Exercise duration (card 2)
        tvExerciseDuration = rootView.findViewById(R.id.card2_title);
        tvExerciseDurationDesc = rootView.findViewById(R.id.card2_desc);

        // Num exercise sessions (card 3)
        tvExerciseSessions = rootView.findViewById(R.id.card3_title);
        tvExerciseSessionsDesc = rootView.findViewById(R.id.card3_desc);

        // last updated txt
        tvExerciseLastUpdated = rootView.findViewById(R.id.home_last_updated);

        // step counter widget
        tvSteps = rootView.findViewById(R.id.home_step_count);
        tvWorkoutLastUpdated = rootView.findViewById(R.id.home_last_updated);
        tvStepsDesc = rootView.findViewById(R.id.home_step_count_desc);
        pbStepCounter = rootView.findViewById(R.id.step_count_progress_bar);

        syncMessage = rootView.findViewById(R.id.sync_message);


        if (sharedPrefObj.getPref(KEY_PREF_USER) != null)
        {
            userObj = new User(sharedPrefObj.getPref(KEY_PREF_USER));
        }
        else
        {
            userObj = new User(sharedPrefObj.getPref(KEY_LOGIN));
        }

        commonUser = userObj;

        if (commonUser.IsUpdateTokenNeeded()){
            syncMessage.setVisibility(View.VISIBLE);
        }else{
            syncMessage.setVisibility(View.GONE);
        }

    }


    private void setData() {
        if(commonUser != null)
        {
            tvExerciseSessionsDesc.setText(getResources().getString(R.string.txt_exercise_session_msg).replace("[session_count]", String.valueOf(commonUser.getTargetExerciseFrequency())));
            tvTargetHeartRateDesc.setText(getResources().getString(R.string.txt_target_heart_rate_msg).replace("[heart_rate]", String.valueOf(commonUser.getTargetHeartRate())));
            tvStepsDesc.setText(getResources().getString(R.string.txt_daily_step_count_msg).replace("[step_count]", String.valueOf(commonUser.getActivityGoal())));
            tvMaximumHeartRateDesc.setText(getResources().getString(R.string.max_heart_rate_desc).replace("[heart_rate]", String.valueOf(commonUser.getTargetMaxHeartRate())));
            tvMaximumHeartRate.setText(getResources().getString(R.string.txt_heart_rate_bpm).replace("[heart_rate]", String.valueOf(commonUser.getMaxHeartRate())));
            tvExerciseSessions.setText(commonUser.getExerciseCount());
            tvTargetHeartRate.setText(getResources().getString(R.string.txt_heart_rate_bpm).replace("[heart_rate]", String.valueOf(commonUser.getAverageHeartRate())));
            tvExerciseDuration.setText(getResources().getString(R.string.txt_targetTotalExerciseDuration).replace("[duration]", String.valueOf(commonUser.getExerciseDuration())));
            tvExerciseDurationDesc.setText(getResources().getString(R.string.txt_exercise_duration_per_session_msg).replace("[minute]", String.valueOf(commonUser.getTargetExerciseDuration())));

            String steps = commonUser.getStepCount();

            tvSteps.setText(getResources().getString(R.string.txt_steps_count).replace("[step]", steps));

            int targetStepCount,stepsCount,progress;
            try {
                targetStepCount = Integer.parseInt(commonUser.getActivityGoal());
                stepsCount= Integer.parseInt(steps);
                progress = (int) (((double) stepsCount/ (double)targetStepCount) * 100);
                progress = progress > 5 ? progress : 5; // set minimum progress to 5
            } catch(Exception e) {
                progress = 5; // show some progress
            }

            pbStepCounter.setProgress(progress);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void updatePageData(){
        AndroidNetworking.get(hostUrl + KEY_USER)
                .addHeaders("Authorization", "Bearer " + userObj.getUserToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "api result: " + response.toString());
                        userObj.updateData(response);
                        try {
                            response.put("token", userObj.getUserToken());
                            sharedPrefObj.updatePref(KEY_LOGIN, response.toString());
                            sharedPrefObj.updatePref(KEY_PREF_USER, response.toString());
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                        setData();
                    }
                    @Override
                    public void onError(ANError error) {
                        if (error.getErrorCode() != 0) {
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail()+hostUrl + KEY_USER);

                        } else {
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail()+hostUrl +" "+KEY_USER);
                        }
                    }
                });
        srlStatus.setRefreshing(false);
    }


}
