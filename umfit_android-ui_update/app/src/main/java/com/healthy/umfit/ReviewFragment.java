package com.healthy.umfit;

import static com.healthy.umfit.TagName.KEY_LOGIN;
import static com.healthy.umfit.TagName.KEY_PREF_USER;
import static com.healthy.umfit.TagName.KEY_USER;
import static com.healthy.umfit.TagName.hostUrl;
import static com.healthy.umfit.utils.CommonUtilities.commonUser;
import static com.healthy.umfit.utils.CommonUtilities.userPrescription;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;

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
    private CheckBox CBCompleted1,CBCompleted2,CBCompleted3,CBCompleted4,CBCompleted5,CBCompleted6, CBJointPain,CBShortBreath,CBChestPain;
    private SeekBar SBEnjoyment1, SBEnjoyment2, SBEnjoyment3, SBEnjoyment4, SBEnjoyment5, SBEnjoyment6;
    private SeekBar SBDifficulty1, SBDifficulty2, SBDifficulty3, SBDifficulty4, SBDifficulty5, SBDifficulty6;
    private TextView TVExercise2, TVExercise3, TVExercise4, TVExercise5;
    private String ex2,ex3,ex4,ex5;
    private int numExercise;
    public ReviewFragment() {
        // Required empty public constructor
    }

    public ReviewFragment(String ex2, String ex3, String ex4, String ex5, int numExercise){
        this.ex2=ex2;
        this.ex3=ex3;
        this.ex4=ex4;
        this.ex5=ex5;
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
        View rootView = inflater.inflate(R.layout.fragment_review, container, false);
        findViewById(rootView);

        final Button btnSubmit = rootView.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                uploadFeedback();
                if (CBChestPain.isChecked()) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setMessage("Please contact your doctor regarding your chest pain.");

                    builder1.setPositiveButton(
                            "Proceed",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    loadFragment(new StatsFragment());
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setMessage("Congratulations on completing the exercise.");

                    builder1.setPositiveButton(
                            "Proceed",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    loadFragment(new StatsFragment());
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        });
        final Button btnCancelFeedback = rootView.findViewById(R.id.btnCancelFeedback);
        btnCancelFeedback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadFragment(new StatsFragment());
            }
        });
        TVExercise2.setText(ex2);
        TVExercise3.setText(ex3);
        TVExercise4.setText(ex4);
        TVExercise5.setText(ex5);
        if (numExercise<6){
            CLExercise5.setVisibility(View.GONE);
        }
        if (numExercise<5){
            CLExercise4.setVisibility(View.GONE);
        }
        if (numExercise<4){
            CLExercise3.setVisibility(View.GONE);
        }
        return rootView;
    }

    private void findViewById(View rootView) {
        CBJointPain=rootView.findViewById(R.id.CBJointPain);
        CBChestPain=rootView.findViewById(R.id.CBChestPain);
        CBShortBreath=rootView.findViewById(R.id.CBShortBreath);
        TVExercise2= rootView.findViewById(R.id.TVExercise2);
        TVExercise3= rootView.findViewById(R.id.TVExercise3);
        TVExercise4= rootView.findViewById(R.id.TVExercise4);
        TVExercise5= rootView.findViewById(R.id.TVExercise5);
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
        String wuComplete=Boolean.toString(CBCompleted1.isChecked())+",";
        String wuDiff=Integer.toString(SBDifficulty1.getProgress())+",";
        String wuEnjoy=Integer.toString(SBEnjoyment1.getProgress())+",";
        String ex2Type=TVExercise2.getText().toString().replace(" ","+")+",";
        String ex2Complete=Boolean.toString(CBCompleted2.isChecked())+",";
        String ex2Diff=Integer.toString(SBDifficulty2.getProgress())+",";
        String ex2Enjoy=Integer.toString(SBEnjoyment2.getProgress())+",";
        String ex3Type=TVExercise3.getText().toString().replace(" ","+")+",";
        String ex3Complete=Boolean.toString(CBCompleted3.isChecked())+",";
        String ex3Diff=Integer.toString(SBDifficulty3.getProgress())+",";
        String ex3Enjoy=Integer.toString(SBEnjoyment3.getProgress())+",";
        String ex4Type=TVExercise4.getText().toString().replace(" ","+")+",";
        String ex4Complete=Boolean.toString(CBCompleted4.isChecked())+",";
        String ex4Diff=Integer.toString(SBDifficulty4.getProgress())+",";
        String ex4Enjoy=Integer.toString(SBEnjoyment4.getProgress())+",";
        String ex5Type=TVExercise5.getText().toString().replace(" ","+")+",";
        String ex5Complete=Boolean.toString(CBCompleted5.isChecked())+",";
        String ex5Diff=Integer.toString(SBDifficulty5.getProgress())+",";
        String ex5Enjoy=Integer.toString(SBEnjoyment5.getProgress())+",";
        String cdComplete=Boolean.toString(CBCompleted6.isChecked())+",";
        String cdDiff=Integer.toString(SBDifficulty6.getProgress())+",";
        String cdEnjoy=Integer.toString(SBEnjoyment6.getProgress())+",";
        String chestPain=Boolean.toString(CBChestPain.isChecked())+",";
        String shortBreath=Boolean.toString(CBShortBreath.isChecked())+",";
        String jointPain=Boolean.toString(CBJointPain.isChecked());
        if (numExercise<6){
            ex5Type="Null,";
        }
        if (numExercise<5){
            ex4Type="Null,";
        }
        if (numExercise<4){
            ex3Type="Null,";
        }
        String url="https://flask-umfit.herokuapp.com/feedback/"+commonUser.getEmail()+","+wuComplete+wuDiff+wuEnjoy+
                ex2Type+ex2Complete+ex2Diff+ex2Enjoy+ex3Type+ex3Complete+ex3Diff+ex3Enjoy+ex4Type+ex4Complete+ex4Diff+ex4Enjoy+
                ex5Type+ex5Complete+ex5Diff+ex5Enjoy+cdComplete+cdDiff+cdEnjoy+chestPain+shortBreath+jointPain;
        Log.d("Test",url);
        AndroidNetworking.get(url)
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
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}