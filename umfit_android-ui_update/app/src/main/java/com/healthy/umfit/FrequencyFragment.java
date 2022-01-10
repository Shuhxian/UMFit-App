package com.healthy.umfit;

import static com.healthy.umfit.TagName.KEY_LOGIN;
import static com.healthy.umfit.TagName.KEY_PREF_USER;
import static com.healthy.umfit.TagName.KEY_UPDATE;
import static com.healthy.umfit.TagName.KEY_USER;
import static com.healthy.umfit.TagName.hostUrl;
import static com.healthy.umfit.utils.CommonUtilities.commonUser;
import static com.healthy.umfit.utils.CommonUtilities.userPrescription;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.entity.UrlEncodedFormEntity;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPut;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;
import com.healthy.umfit.entity.User;
import com.healthy.umfit.utils.SharedPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FrequencyFragment extends Fragment {
    private static final String TAG = FrequencyFragment.class.getSimpleName();

    private SharedPreferencesManager sharedPrefObj;

    private User userObj;

    public FrequencyFragment() {
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
        final View rootView = inflater.inflate(R.layout.fragment_frequency, container, false);
        findViewById(rootView);
        final RadioGroup RGFreq=rootView.findViewById(R.id.RGFreq);
        RGFreq.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = rootView.findViewById(checkedId);
                switch(checkedId) {
                    case R.id.RB5times:
                        userPrescription.setTargetTotalExerciseDuration("30");
                        commonUser.setTargetExerciseFrequency("5");
                        editPageData();
                    case R.id.RB3times:
                        userPrescription.setTargetTotalExerciseDuration("50");
                        commonUser.setTargetExerciseDuration("3");
                        editPageData();
                        break;
                }

            }
        });

        final Button btnUpdateFreq = rootView.findViewById(R.id.btnUpdateFreq);
        btnUpdateFreq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                loadFragment(new StatsFragment());
            }
        });
        final Button btnUpdateFreqCancel = rootView.findViewById(R.id.btnUpdateFreqCancel);
        btnUpdateFreqCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                loadFragment(new StatsFragment());
            }
        });
        return rootView;
    }
    private void findViewById(View rootView) {

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

    private void loadFragment(Fragment fragment) {
        //load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    public void editPageData(){
        JSONObject prescription = new JSONObject();
        try {
            prescription.put("average_heart_rate",0);
            prescription.put("max_heart_rate",0);
            prescription.put("exercise_frequency", 0);
            prescription.put("exercise_duration",0);
            prescription.put("target_heart_rate", 100);
            prescription.put("target_max_heart_rate",140);
            prescription.put("target_exercise_frequency",3);
            prescription.put("target_warmup_duration",5);
            prescription.put("target_cooldown_duration",5);
            prescription.put("target_exercise_duration",10);
            prescription.put("target_total_exercise_duration",30);
            prescription.put("step_count",0);
            prescription.put("start_time","2021-11-27T17:08:59.000000Z");
            prescription.put("end_time","2021-12-11T17:08:59.000000Z");
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
            jsonObject.put("prescription",prescription);
            jsonObject.put("huami_token",null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

                //
        AndroidNetworking.put(hostUrl + KEY_UPDATE)
                .addHeaders("Authorization", "Bearer " + userObj.getUserToken())
                .addJSONObjectBody(jsonObject)
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}