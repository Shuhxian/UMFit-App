package com.healthy.umfit;

import static com.healthy.umfit.TagName.KEY_LOGIN;
import static com.healthy.umfit.TagName.KEY_PREF_USER;
import static com.healthy.umfit.TagName.KEY_USER;
import static com.healthy.umfit.TagName.hostUrl;
import static com.healthy.umfit.utils.CommonUtilities.commonUser;
import static com.healthy.umfit.utils.CommonUtilities.userPrescription;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.healthy.umfit.entity.User;
import com.healthy.umfit.utils.SharedPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;


public class ReviewFragment extends Fragment {
    private static final String TAG = FrequencyFragment.class.getSimpleName();

    private SharedPreferencesManager sharedPrefObj;

    private User userObj;

    private ConstraintLayout CLExericse1,CLExercise2,CLExercise3,CLExercise4, CLExercise5, CLExercise6;
    private CheckBox CBCompleted1,CBCompleted2,CBCompleted3,CBCompleted4,CBCompleted5,CBCompleted6;
    private SeekBar SBEnjoyment1, SBEnjoyment2, SBEnjoyment3, SBEnjoyment4, SBEnjoyment5, SBEnjoyment6;
    private SeekBar SBDifficulty1, SBDifficulty2, SBDifficulty3, SBDifficulty4, SBDifficulty5, SBDifficulty6;

    public ReviewFragment() {
        // Required empty public constructor
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
        View rootView = inflater.inflate(R.layout.fragment_review, container, false);
        findViewById(rootView);

        final Button btnSubmit = rootView.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                loadFragment(new StatsFragment());
            }
        });
        final Button btnCancelFeedback = rootView.findViewById(R.id.btnCancelFeedback);
        btnCancelFeedback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                loadFragment(new StatsFragment());
            }
        });

        return rootView;
    }

    private void findViewById(View rootView) {
        CLExericse1 = rootView.findViewById(R.id.CLExercise1);
        CLExercise2 = rootView.findViewById(R.id.CLExercise2);
        CLExercise3 = rootView.findViewById(R.id.CLExercise3);
        CLExercise4 = rootView.findViewById(R.id.CLExercise4);
        CLExercise5 = rootView.findViewById(R.id.CLExercise5);
        CLExercise6 = rootView.findViewById(R.id.CLExercise6);
        CBCompleted1=rootView.findViewById(R.id.CBCompleted1);
        CBCompleted2=rootView.findViewById(R.id.CBCompleted2);
        CBCompleted3=rootView.findViewById(R.id.CBCompleted3);
        CBCompleted4=rootView.findViewById(R.id.CBCompleted4);
        CBCompleted5=rootView.findViewById(R.id.CBCompleted5);
        CBCompleted6=rootView.findViewById(R.id.CBCompleted6);
        SBDifficulty1=rootView.findViewById(R.id.SBDifficulty1);
        SBDifficulty2=rootView.findViewById(R.id.SBDifficulty2);
        SBDifficulty3=rootView.findViewById(R.id.SBDifficulty3);
        SBDifficulty4=rootView.findViewById(R.id.SBDifficulty4);
        SBDifficulty5=rootView.findViewById(R.id.SBDifficulty5);
        SBDifficulty6=rootView.findViewById(R.id.SBDifficulty6);
        SBEnjoyment1=rootView.findViewById(R.id.SBEnjoyment1);
        SBEnjoyment2=rootView.findViewById(R.id.SBEnjoyment2);
        SBEnjoyment3=rootView.findViewById(R.id.SBEnjoyment3);
        SBEnjoyment4=rootView.findViewById(R.id.SBEnjoyment4);
        SBEnjoyment5=rootView.findViewById(R.id.SBEnjoyment5);
        SBEnjoyment6=rootView.findViewById(R.id.SBEnjoyment6);
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

    public void uploadFeedback(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email",commonUser.getEmail());
            jsonObject.put("phone_number",commonUser.getPhoneNumber());
            jsonObject.put("name", "Goy");
            jsonObject.put("birthday","11/10/1999");
            jsonObject.put("height", "177");
            jsonObject.put("weight","66.0");
            jsonObject.put("gender",0);
            jsonObject.put("is_update_token_need",1);
            jsonObject.put("target_step_count",10000);
            jsonObject.put("fcm_token",commonUser.getFcmToken());
            jsonObject.put("huami_token",null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //
        AndroidNetworking.put("https://flask-umfit.herokuapp.com/preference/8")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "api result: " + response.toString());
                        //userObj.updateData(response);
                        try {
                            // response.put("token", userObj.getUserToken());
                            // sharedPrefObj.updatePref(KEY_LOGIN, response.toString());
                            // sharedPrefObj.updatePref(KEY_PREF_USER, response.toString());
                        }catch(Exception e){
                            e.printStackTrace();
                        }

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
    }

    private void loadFragment(Fragment fragment) {
        //load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}