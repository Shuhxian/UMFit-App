package com.healthy.umfit;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.healthy.umfit.entity.Profile;
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
import java.util.Objects;

import okhttp3.Response;

import static com.healthy.umfit.MainActivity.clearCurrentBMIndex;
import static com.healthy.umfit.TagName.IS_LOGGED_IN;
import static com.healthy.umfit.TagName.KEY_HUAMI_SYNC_URL;
import static com.healthy.umfit.TagName.KEY_LOGIN;
import static com.healthy.umfit.TagName.KEY_LOG_OUT;
import static com.healthy.umfit.TagName.KEY_PREF_USER;
import static com.healthy.umfit.TagName.KEY_PROFILE;
import static com.healthy.umfit.TagName.KEY_UPDATE_TOKEN;
import static com.healthy.umfit.TagName.KEY_USER;
import static com.healthy.umfit.TagName.hostUrl;
import static com.healthy.umfit.TagName.userUrl;

public class ProfileFragment extends Fragment
{
    private static final String TAG = ProfileFragment.class.getSimpleName();

    private Profile profileObj;
    private TextView tvUserId, tvName, tvGender, tvBirthday, tvHeight, tvWeight, tvActivityGoal, tvVersionCode, tvVersionName;
    private Button rlLogOut, syncHuamiTokenBtn;
    private ProgressBar pbProfile;
    private ScrollView svProfile;
    private Spinner spinnerLanguage;

    private SharedPreferencesManager sharedPrefObj;
    private Handler handlerObj;
    private Runnable runnableObj;

    private User userObj;

    public ProfileFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        findViewById(rootView);
        setOnClick();

        if (sharedPrefObj.hasKey(KEY_LOGIN)) {
            svProfile.setVisibility(View.VISIBLE);
            pbProfile.setVisibility(View.GONE);

            userObj = new User(sharedPrefObj.getPref(KEY_LOGIN));
            userObj.getProfile();
            initializeView(userObj);
        } else {
            svProfile.setVisibility(View.GONE);
            pbProfile.setVisibility(View.VISIBLE);
            updatePageData();
        }

        return rootView;
    }

    private void findViewById(View rootView) {
        tvUserId = rootView.findViewById(R.id.tv_user_id);
        tvName = rootView.findViewById(R.id.tv_name);
        tvGender = rootView.findViewById(R.id.tv_gender);
        tvBirthday = rootView.findViewById(R.id.tv_birthday);
        tvHeight = rootView.findViewById(R.id.tv_height);
        tvWeight = rootView.findViewById(R.id.tv_weight);
        tvActivityGoal = rootView.findViewById(R.id.tv_activity_goal);

        rlLogOut = rootView.findViewById(R.id.rl_log_out);
        syncHuamiTokenBtn = rootView.findViewById(R.id.sync_huami_btn);

        pbProfile = rootView.findViewById(R.id.pb_profile);
        svProfile = rootView.findViewById(R.id.sv_profile);
        tvVersionCode = rootView.findViewById(R.id.tv_version_code);
    }

    private void setOnClick() {


        rlLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "logout", Toast.LENGTH_SHORT).show();
//                sharedPrefObj.updatePref(IS_LOGGED_IN, false);
                sharedPrefObj.clearAllPref(true);
                clearCurrentBMIndex();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        syncHuamiTokenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleHuamiSync();
            }
        });
    }

    private void initializeView(User userObj) {

        Profile profile = userObj.getProfile();

        if (profile.getUserId() != null) {
            tvUserId.setText(getResources().getString(R.string.txt_user_id).replace("[user_id]", profile.getUserId()));
        }

        if (userObj.getUserName() != null) {
            tvName.setText(userObj.getUserName());
        }

        if (profile.getGender() != null) {
            if (profile.getGender().equalsIgnoreCase("0")) {
                tvGender.setText(getResources().getString(R.string.txt_female));
            } else {
                tvGender.setText(getResources().getString(R.string.txt_male));
            }
        }

        if (profile.getBirthday() != null) {
            tvBirthday.setText(profile.getBirthday());
        }

        if (profile.getHeightInCm() != null) {
            tvHeight.setText(profile.getHeight());
        }

        if (profile.getWeight() != null) {
            tvWeight.setText(String.format("%.1f", Double.parseDouble(profile.getWeight())));
        }

        if (userObj.getActivityGoal() != null) {
            tvActivityGoal.setText(getResources().getString(R.string.txt_steps_count).replace("[step]", userObj.getActivityGoal()));
        }

        try {
            String versionName = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
            tvVersionCode.setText("UMFIT " + versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private String logoutBody(){
        String jsonString = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("isNotificationEnabled", false);
            jsonString = jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonString;
    }


    public void handleHuamiSync(){
        getHuamiSyncURL();
    }

    public void openHuamiSyncPage(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    private void getHuamiSyncURL(){
        AndroidNetworking.get(hostUrl + KEY_HUAMI_SYNC_URL)
                .addHeaders("Authorization", "Bearer " + userObj.getUserToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "api result: " + response.toString());
                        String url;
                        try {
                            url = response.getString("sync_url");
                        }catch(Exception e){
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Unable to sync with Huami. Please make sure you have active internet connection and try again.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        openHuamiSyncPage(url);
                    }
                    @Override
                    public void onError(ANError error) {
                        if (error.getErrorCode() != 0) {
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        } else {
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }

                        Toast.makeText(getContext(), "Unable to sync with Huami. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
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

                        initializeView(userObj);
                    }
                    @Override
                    public void onError(ANError error) {
                        if (error.getErrorCode() != 0) {
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                        } else {
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

        pbProfile.setVisibility(View.GONE);
    }

}
