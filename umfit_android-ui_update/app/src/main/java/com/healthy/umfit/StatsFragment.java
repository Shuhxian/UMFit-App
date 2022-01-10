package com.healthy.umfit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.healthy.umfit.entity.Activity;
import com.healthy.umfit.entity.HeartRate;
import com.healthy.umfit.entity.SportSummary;
import com.healthy.umfit.entity.User;
import com.healthy.umfit.utils.OkHttpClientConnection;
import com.healthy.umfit.utils.SharedPreferencesManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.healthy.umfit.TagName.*;
import static com.healthy.umfit.utils.CommonUtilities.*;


public class StatsFragment extends Fragment{
    private static final String TAG = StatsFragment.class.getSimpleName();

    private String urlHeartRate = "", urlActivities = "", urlSports = "";
    private ArrayList<HeartRate> heartRateList = new ArrayList<>();
    private ArrayList<Activity> activityList = new ArrayList<>();
    private ArrayList<Activity> workoutList = new ArrayList<>();
    private TextView tvExerciseSessions, tvTargetHeartRate, tvMaximumHeartRate, tvExerciseDuration, tvExerciseLastUpdated, tvSteps, tvWorkoutLastUpdated;
    private TextView tvExerciseSessionsDesc, tvTargetHeartRateDesc, tvMaximumHeartRateDesc, tvExerciseDurationDesc, tvStepsDesc;
    private Spinner SPNumExercise, SPEx1, SPEx2, SPEx3, SPEx4;
    private AppCompatEditText edtInputDate;
    //    private LinearLayout llWorkout;
    private ProgressBar pbStatus;
    private ScrollView svStatus;
    private SwipeRefreshLayout srlStatus;

    private String interval = "daily";
    private String inputDate = "";
    private String currentDate = "", currentTimezone = "", currentTimeStamp = "";
    private boolean isRequiredUpdate = false;
    private int sessionCount = 3, targetHeartRate = 125, maximumHeartRate = 165, exerciseDuration = 30, stepCount = 10000;
    private ArrayList<String> durationList = new ArrayList<>();

    private SharedPreferencesManager sharedPrefObj;
    private OkHttpClientConnection okHttpClientConnectionObj;
    private Handler handlerActivityObj, handlerHeartRateObj, handlerSportObj;
    private Runnable runnableActivityObj, runnableHeartRateObj, runnableSportObj;

    private User userObj;
    private SportSummary sportSummaryObj;

    private TextView tvWarmUp, tvFirstExercise, tvFirstRest, tvSecondExercise, tvSecondRest, tvCoolDown, tvTotalExercise, TVCd;
    private LinearLayout LLStats1,LLStats2,LLStats3,LLStats4,LLStats5,LLStats6;
    private int numExercise;
    public StatsFragment() {
        // Required empty public constructor
        this.numExercise=3;
    }
    public StatsFragment(int numExercise) {
        this.numExercise=numExercise;
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
        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);
        findViewById(rootView);

        setData();

        final Button btnEditFreq = rootView.findViewById(R.id.btnChangeFreq);
        btnEditFreq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                loadFragment(new FrequencyFragment());
            }
        });
        final Button btnEditTime = rootView.findViewById(R.id.btnChangeTime);
        btnEditTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                loadFragment(new FrequencyFragment());
            }
        });
        final Button btnSubmitFeedback = rootView.findViewById(R.id.btnSubmitFeedback);
        btnSubmitFeedback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                loadFragment(new ReviewFragment());
            }
        });
        final Button btnPrescribeMain = rootView.findViewById(R.id.btnPrescribeMain);
        btnPrescribeMain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                loadFragment(new PatientSymptomsFragment());
            }
        });
        srlStatus.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isAdded()) {
                    updatePageData();
                }
            }
        });
        final String numExerciseSelection[]={"3","4","5","6"};
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,numExerciseSelection);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SPNumExercise.setAdapter(adapter);
        SPNumExercise.setSelection(0,false);
        SPNumExercise.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        loadFragment(new StatsFragment(Integer.valueOf(numExerciseSelection[pos])));
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        if (this.numExercise<6){
            LLStats5.setVisibility(View.GONE);
        }
        if (this.numExercise<5){
            LLStats4.setVisibility(View.GONE);
        }
        if (this.numExercise<4){
            LLStats3.setVisibility(View.GONE);
        }
        TVCd.setText(Integer.toString(this.numExercise));
        AndroidNetworking.get("https://flask-umfit.herokuapp.com/preference/"+KEY_PREF_USER)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "api result: " + response.toString());
                        try {
                            ArrayList<String> preferenceList = new ArrayList<String>();
                            JSONArray jArray = response.getJSONArray("preference");
                            if (response != null) {
                                for (int i=0;i<jArray.length();i++){
                                    preferenceList.add(jArray.getString(i));
                                }
                                ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,preferenceList);
                                // Specify the layout to use when the list of choices appears
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                // Apply the adapter to the spinner
                                SPEx1.setAdapter(adapter);
                                SPEx1.setOnItemSelectedListener(
                                        new AdapterView.OnItemSelectedListener() {
                                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                                            }
                                            public void onNothingSelected(AdapterView<?> parent) {
                                            }
                                        });
                                SPEx2.setAdapter(adapter);
                                SPEx2.setOnItemSelectedListener(
                                        new AdapterView.OnItemSelectedListener() {
                                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                                            }
                                            public void onNothingSelected(AdapterView<?> parent) {
                                            }
                                        });
                                SPEx3.setAdapter(adapter);
                                SPEx3.setOnItemSelectedListener(
                                        new AdapterView.OnItemSelectedListener() {
                                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                                            }
                                            public void onNothingSelected(AdapterView<?> parent) {
                                            }
                                        });
                                SPEx4.setAdapter(adapter);
                                SPEx4.setOnItemSelectedListener(
                                        new AdapterView.OnItemSelectedListener() {
                                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                                            }
                                            public void onNothingSelected(AdapterView<?> parent) {
                                            }
                                        });
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                        //setData();
                    }
                    @Override
                    public void onError(ANError error) {
                        if (error.getErrorCode() != 0) {
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                        } else {
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });
        return rootView;
    }

    private void findViewById(View rootView) {
        srlStatus = rootView.findViewById(R.id.srl_status);
        svStatus = rootView.findViewById(R.id.sv_status);
        pbStatus = rootView.findViewById(R.id.pb_status);

        tvExerciseLastUpdated = rootView.findViewById(R.id.tv_exercise_last_updated);
        tvSteps = rootView.findViewById(R.id.tv_steps);
        tvWorkoutLastUpdated = rootView.findViewById(R.id.tv_workout_last_updated);

        tvStepsDesc = rootView.findViewById(R.id.tv_steps_desc);
        edtInputDate = rootView.findViewById(R.id.edt_input_date);
        tvWarmUp = rootView.findViewById(R.id.tv_targetWarmUpDuration);
        tvFirstExercise = rootView.findViewById(R.id.tv_targetFirstExercise);
        tvFirstRest = rootView.findViewById(R.id.tv_targetFirstRest);
        tvSecondExercise = rootView.findViewById(R.id.tv_targetSecondExercise);
        tvSecondRest = rootView.findViewById(R.id.tv_targetSecondRest);
        tvCoolDown = rootView.findViewById(R.id.tv_targetCoolDown);
        tvTotalExercise = rootView.findViewById(R.id.tv_targetTotalExercise);

        // Average heart rate (card 1)
        tvTargetHeartRate = rootView.findViewById(R.id.card1_title);
//        tvTargetHeartRateDesc = rootView.findViewById(R.id.card1_desc);

        // Maximum heart rate(not shown)
        tvMaximumHeartRate = rootView.findViewById(R.id.card4_title);
        tvMaximumHeartRateDesc = rootView.findViewById(R.id.card4_desc);

        // Exercise duration (card 2)
        tvExerciseDuration = rootView.findViewById(R.id.card2_title);
//        tvExerciseDurationDesc = rootView.findViewById(R.id.card2_desc);

        // Num exercise sessions (card 3)
        tvExerciseSessions = rootView.findViewById(R.id.card3_title);
//        tvExerciseSessionsDesc = rootView.findViewById(R.id.card3_desc);

        LLStats1=rootView.findViewById(R.id.LLStats1);
        LLStats2=rootView.findViewById(R.id.LLStats2);
        LLStats3=rootView.findViewById(R.id.LLStats3);
        LLStats4=rootView.findViewById(R.id.LLStats4);
        LLStats5=rootView.findViewById(R.id.LLStats5);
        LLStats6=rootView.findViewById(R.id.LLStats6);

        SPEx1=rootView.findViewById(R.id.SPEx1);
        SPEx2=rootView.findViewById(R.id.SPEx2);
        SPEx3=rootView.findViewById(R.id.SPEx3);
        SPEx4=rootView.findViewById(R.id.SPEx4);
        SPNumExercise=rootView.findViewById(R.id.SPNumExercise);

        TVCd=rootView.findViewById(R.id.TVCd);
        if (sharedPrefObj.getPref(KEY_PREF_USER) != null)
        {
            userObj = new User(sharedPrefObj.getPref(KEY_PREF_USER));
        }
        else
        {
            userObj = new User(sharedPrefObj.getPref(KEY_LOGIN));
        }

        commonUser = userObj;
        userPrescription = commonUser.getUserPrescription();
    }

    private void setData() {

        if(commonUser != null)
        {
            tvExerciseSessions.setText(getResources().getString(R.string.txt_sessions_per_week).replace("[session_count]", String.valueOf(commonUser.getTargetExerciseFrequency())));
            tvTargetHeartRate.setText(getResources().getString(R.string.txt_heart_rate_bpm).replace("[heart_rate]", String.valueOf(commonUser.getTargetHeartRate())));
//            tvStepsDesc.setText(getResources().getString(R.string.txt_daily_step_count_msg).replace("[step_count]", String.valueOf(commonUser.getActivityGoal())));
            tvMaximumHeartRate.setText(getResources().getString(R.string.txt_heart_rate_bpm).replace("[heart_rate]", String.valueOf(commonUser.getTargetMaxHeartRate())));
        }
//        tvMaximumHeartRateDesc.setText(getResources().getString(R.string.txt_maximum_heart_rate_msg).replace("[heart_rate]", String.valueOf(maximumHeartRate)));

        if (userPrescription != null)
        {
            //tvExerciseDuration.setText(getResources().getString(R.string.txt_minute).replace("[minute]", String.valueOf(userPrescription.getTargetTotalExerciseDuration())));
            //tvWarmUp.setText(getResources().getString(R.string.txt_targetWarmUpDuration).replace("[duration]", String.valueOf(userPrescription.getTargetWarmUpDuration())));
            //tvFirstExercise.setText(getResources().getString(R.string.txt_targetFirstMainExerciseDuration).replace("[duration]", String.valueOf(userPrescription.getTargetFirstMainExerciseDuration())));
            //tvFirstRest.setText(getResources().getString(R.string.txt_targetFirstRestDuration).replace("[duration]", String.valueOf(userPrescription.getTargetFirstRestDuration())));
            //tvSecondExercise.setText(getResources().getString(R.string.txt_targetSecondMainExerciseDuration).replace("[duration]", String.valueOf(userPrescription.getTargetSecondMainExerciseDuration())));
//            tvSecondRest.setText(getResources().getString(R.string.txt_targetSecondRestDuration).replace("[duration]", String.valueOf(userPrescription.getTargetSecondRestDuration())));
            //tvCoolDown.setText(getResources().getString(R.string.txt_targetCooldownDuration).replace("[duration]", String.valueOf(userPrescription.getTargetCoolDownDuration())));
//            tvTotalExercise.setText(getResources().getString(R.string.txt_targetTotalExerciseDuration).replace("[duration]", String.valueOf(userPrescription.getTargetTotalExerciseDuration())));
        }
        else
        {
            tvExerciseDuration.setText(getResources().getString(R.string.txt_minute).replace("[minute]", String.valueOf(commonUser.getTargetExerciseDuration())));
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
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail()+hostUrl + KEY_USER);
                        }
                    }
                });
        srlStatus.setRefreshing(false);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private int getNumExercise(){
        return this.numExercise;
    }
    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }
}
