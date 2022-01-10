package com.healthy.umfit;

import static com.healthy.umfit.TagName.KEY_LOGIN;
import static com.healthy.umfit.TagName.KEY_PREF_USER;
import static com.healthy.umfit.utils.CommonUtilities.commonUser;
import static com.healthy.umfit.utils.CommonUtilities.userPrescription;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.healthy.umfit.entity.User;
import com.healthy.umfit.utils.SharedPreferencesManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PatientSymptomsFragment extends Fragment {
    private EditText ETAge, ETRestHR, ETPeakHR, ETMet, ETSt, ETRestEjection;
    private Spinner SPAngina, SPHemodynamics, SPArrhythmias, SPDysrhythmias,SPCardiacHistory, SPComplicatedMi, SPChf, SPIschemia, SPClinicalDepression;
    private static final String TAG = FrequencyFragment.class.getSimpleName();

    private SharedPreferencesManager sharedPrefObj;

    private User userObj;

    public PatientSymptomsFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_patient_symptoms, container, false);
        findViewById(rootView);
        String presence[]={"Absent","Present"};
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,presence);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SPAngina.setAdapter(adapter);
        SPAngina.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        SPHemodynamics.setAdapter(adapter);
        SPHemodynamics.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        SPArrhythmias.setAdapter(adapter);
        SPArrhythmias.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        SPDysrhythmias.setAdapter(adapter);
        SPDysrhythmias.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        SPComplicatedMi.setAdapter(adapter);
        SPComplicatedMi.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        SPChf.setAdapter(adapter);
        SPChf.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        SPCardiacHistory.setAdapter(adapter);
        SPCardiacHistory.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        SPIschemia.setAdapter(adapter);
        SPIschemia.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        SPClinicalDepression.setAdapter(adapter);
        SPClinicalDepression.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        final Button btnPrescribeCancel = rootView.findViewById(R.id.btnPrescribeCancel);
        btnPrescribeCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadFragment(new StatsFragment());
            }
        });
        final Button btnPrescribe = rootView.findViewById(R.id.btnPrescribe);
        //Update RestHR
        btnPrescribe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ETAge.getText().toString().isEmpty()){
                    ETAge.setError("Field cannot be empty!");
                    return;
                }
                if (ETRestHR.getText().toString().isEmpty()){
                    ETRestHR.setError("Field cannot be empty!");
                    return;
                }
                if (ETPeakHR.getText().toString().isEmpty()){
                    ETPeakHR.setError("Field cannot be empty!");
                    return;
                }
                if (ETMet.getText().toString().isEmpty()){
                    ETMet.setError("Field cannot be empty!");
                    return;
                }
                if (ETSt.getText().toString().isEmpty()){
                    ETSt.setError("Field cannot be empty!");
                    return;
                }
                if (ETRestEjection.getText().toString().isEmpty()){
                    ETRestEjection.setError("Field cannot be empty!");
                    return;
                }
                AndroidNetworking.get("https://flask-umfit.herokuapp.com/prescription/" + ETAge.getText().toString()
                            + "," + ETPeakHR.getText().toString() + "," + ETMet.getText().toString() + "," + ETSt.getText().toString()
                            + "," + ETRestEjection.getText().toString() + "," + SPAngina.getSelectedItem().toString() + ","
                            + SPArrhythmias.getSelectedItem().toString() + "," + SPHemodynamics.getSelectedItem().toString() + ","
                            + SPCardiacHistory.getSelectedItem().toString() + "," + SPDysrhythmias.getSelectedItem().toString() + ","
                            + SPComplicatedMi.getSelectedItem().toString() + "," + SPChf.getSelectedItem().toString() + ","
                            + SPIschemia.getSelectedItem().toString() + "," + SPClinicalDepression.getSelectedItem().toString())
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d(TAG, "api result: " + response.toString());
                                    try {

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                public void onError(ANError error) {
                                    if (error.getErrorCode() != 0) {
                                        Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                        Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                    } else {
                                        Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                    }
                                }
                            });
                    loadFragment(new StatsFragment());

            }
        });
        return rootView;
    }

    private void findViewById(View rootView) {
        ETAge=rootView.findViewById(R.id.ETAge);
        ETRestHR=rootView.findViewById(R.id.ETRestHR);
        ETPeakHR=rootView.findViewById(R.id.ETPeakHR);
        ETMet=rootView.findViewById(R.id.ETMet);
        ETSt=rootView.findViewById(R.id.ETSt);
        ETRestEjection=rootView.findViewById(R.id.ETRestEjection);
        SPAngina=rootView.findViewById(R.id.SPAngina);
        SPHemodynamics=rootView.findViewById(R.id.SPHemodynamics);
        SPArrhythmias=rootView.findViewById(R.id.SPArrythmias);
        SPDysrhythmias=rootView.findViewById(R.id.SPDysrhythmias);
        SPCardiacHistory=rootView.findViewById(R.id.SPCardiacHistory);
        SPComplicatedMi=rootView.findViewById(R.id.SPComplicatedMi);
        SPChf=rootView.findViewById(R.id.SPChf);
        SPIschemia=rootView.findViewById(R.id.SPIschemia);
        SPClinicalDepression=rootView.findViewById(R.id.SPClinicalDepression);
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
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}