package com.healthy.umfit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;

import com.bugsnag.android.Bugsnag;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.healthy.umfit.entity.User;
import com.healthy.umfit.utils.CommonUtilities;
import com.healthy.umfit.utils.FormatterUtil;
import com.healthy.umfit.utils.OkHttpClientConnection;
import com.healthy.umfit.utils.SharedHandler;
import com.healthy.umfit.utils.SharedPreferencesManager;
import com.huami.android.oauth.AuthResults;
import com.huami.android.oauth.Callback;
import com.huami.android.oauth.OpenAuthorize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Trace;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.Response;

import static com.healthy.umfit.TagName.*;
import static com.healthy.umfit.utils.CommonUtilities.APP_ID;
import static com.healthy.umfit.utils.CommonUtilities.hideKeyboard;
import static com.healthy.umfit.utils.CommonUtilities.showPositiveDialog;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, OkHttpClientConnection.ServiceResponseCallBack {
    public static final String TAG = LoginActivity.class.getSimpleName();

    private final int MODE_PHONE = 1;
    private final int MODE_CODE = 2;
    private final int CODE_AUTO_VERIFY_WAIT_TIME_MS = 10000;
    private final int RESEND_TIME_SECOND = 60;
    private final String PHONE_FORM = "PHONE";
    private final String CODE_FORM = "CODE";

    // UI references.
    private AutoCompleteTextView actvMobile, actvPassword, actvSignUpUsername, actvSignUpEmail, actvSignUpMobile, actvSignUpPassword, actvCode;
    private ProgressBar pbLogin;
    //    private ScrollView svLogin;
    private Button btnSignIn, btnSignUp, btnClear, btnSendCode, btnResendCode;
    private CheckBox cbRememberMe;
    private LinearLayout llSignIn, llSignUp, llRoot, llLogin, llVerifyPhone, llVerifyCode;
    private RelativeLayout rlSignUp, rlSignIn;
    private OpenAuthorize mOpenAuthorize;
    private ActionBar toolbar;
    private TextView tvTNC;

    private SharedPreferencesManager sharedPrefObj;
    private OkHttpClientConnection okHttpClientConnectionObj;
    private Handler handlerObj;
    private Runnable runnableObj;

    //for checkbox remember me
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    private String mRefreshToken = "";
    private String fcmToken = "";
    private String signUpUsername = "";
    private String signUpMobile;
    private String signUpEmail;

    private TimerTask showCodeFormTask;
    private Timer resendCodeTimer;
    private PhoneAuthProvider.ForceResendingToken authForceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks onVerificationStateChangedCallbacks;
    private FirebaseAuth firebaseAuth;
    private String authVerificationId;
    private int ui_mode = MODE_PHONE;

    private String mobile;
    private boolean isUpdateHuamiToken = false;
    private User user;
    private String request_type;
    private String signUpBody;
    private String signUpUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        sharedPrefObj = new SharedPreferencesManager(LoginActivity.this);

        toolbar = getSupportActionBar();
        toolbar.hide();

        findView();
        setOnClick();
    }

    private void findView() {
        llRoot = findViewById(R.id.ll_root);
        llLogin = findViewById(R.id.ll_login);
        pbLogin = findViewById(R.id.pb_login);
        btnSignIn = findViewById(R.id.btn_sign_in);

        llSignIn = findViewById(R.id.ll_sign_in);
        rlSignUp = findViewById(R.id.rl_sign_up);
        llSignUp = findViewById(R.id.ll_sign_up);

        actvMobile = findViewById(R.id.actv_mobile);
        actvPassword = findViewById(R.id.actv_password);
//        actvSignUpUsername = findViewById(R.id.actv_sign_up_?username);
        actvSignUpMobile = findViewById(R.id.actv_sign_up_mobile);
        actvSignUpEmail = findViewById(R.id.actv_sign_up_email);
        actvSignUpPassword = findViewById(R.id.actv_sign_up_password);
        btnSignUp = findViewById(R.id.btn_sign_up);
        rlSignIn = findViewById(R.id.rl_sign_in);
        cbRememberMe = findViewById(R.id.cb_remember_me);
        btnClear = findViewById(R.id.btn_clear);
        actvCode = findViewById(R.id.actv_code);
        btnSendCode = findViewById(R.id.btn_send_code);
        btnResendCode = findViewById(R.id.btn_resend_code);
        llVerifyPhone = findViewById(R.id.ll_verify_phone);
        llVerifyCode = findViewById(R.id.ll_verify_code);
        tvTNC = findViewById(R.id.tv_tnc);

        llSignIn.setVisibility(View.VISIBLE);
        llSignUp.setVisibility(View.GONE);

        setupUi();

        //for presentation
        btnClear.setVisibility(View.GONE);

        getFcmToken();

        //for checkbox remember me
        getRememberMe();

    }

    private void setOnClick() {
        btnClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefObj.clearAllPref(true);
            }
        });

        llRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(LoginActivity.this);
                return false;
            }
        });

        rlSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                llSignIn.setVisibility(View.GONE);
                llSignUp.setVisibility(View.VISIBLE);
            }
        });

        rlSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                llSignIn.setVisibility(View.VISIBLE);
                llSignUp.setVisibility(View.GONE);
            }
        });

        tvTNC.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/open?id=1kJBpGi7VLHo5-6m_Yq2mh6oH3T63xNF6"));
                startActivity(browserIntent);
            }
        });

        btnSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(LoginActivity.this);
                attemptLogin();
            }
        });

        btnSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(LoginActivity.this);
                boolean cancel = false;
                View focusView = null;

//                actvSignUpUsername.setError(null);
                actvSignUpMobile.setError(null);
                actvSignUpEmail.setError(null);
                actvSignUpPassword.setError(null);

//                signUpUsername = actvSignUpUsername.getText().toString();
                signUpMobile = actvSignUpMobile.getText().toString();
                signUpEmail = actvSignUpEmail.getText().toString();
                String pass = actvSignUpPassword.getText().toString();
//                if (TextUtils.isEmpty(signUpUsername)) {
//                    cancel = true;
//                    focusView = actvSignUpUsername;
//                    actvSignUpUsername.setError(getResources().getString(R.string.error_field_required));
//                }
                if(TextUtils.isEmpty(signUpMobile)){
                    cancel = true;
                    focusView = actvSignUpMobile;
                    actvSignUpMobile.setError(getResources().getString(R.string.error_field_required));
                }
                if(TextUtils.isEmpty(signUpEmail)){
                    cancel = true;
                    focusView = actvSignUpEmail;
                    actvSignUpEmail.setError(getResources().getString(R.string.error_field_required));
                }
                if(TextUtils.isEmpty(pass)){
                    cancel = true;
                    focusView = actvSignUpPassword;
                    actvSignUpPassword.setError(getResources().getString(R.string.error_field_required));
                }
                if(signUpMobile.length()<10 || signUpMobile.matches("^([0-9\\s\\-\\+\\(\\)]*)$")==false  ){
                    cancel = true;
                    focusView = actvSignUpMobile;
                    actvSignUpMobile.setError(getResources().getString(R.string.error_field_required));
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {
                    showProgress(true);
//                    onGetToken();
                    request_type = KEY_SIGNUP;
                    signUpBody = getJSONString(KEY_SIGNUP, signUpUsername, signUpEmail, signUpMobile, "-", fcmToken, pass);
                    signUpUrl = hostUrl + KEY_REGISTER;
                    try{
                        runAPI("POST", signUpUrl, signUpBody, "");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        // setup phone auth callbacks
        onVerificationStateChangedCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted: " + phoneAuthCredential);
                if (showCodeFormTask != null) {
                    showCodeFormTask.cancel();
                }
//                signInWithPhoneAuthCredential(phoneAuthCredential);

                showCodeFormTask = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showProgress(false);
                                showForm(CODE_FORM);
                                enableForm(CODE_FORM, true);
                            }
                        });
                    }
                };
                new Timer().schedule(showCodeFormTask, CODE_AUTO_VERIFY_WAIT_TIME_MS);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed: ", e);
                if (showCodeFormTask != null) {
                    showCodeFormTask.cancel();
                }
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                showProgress(false);
                showForm(PHONE_FORM);
                enableForm(PHONE_FORM, true);
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                Log.d(TAG, "onCodeSent: " + verificationId);
                authVerificationId = verificationId;
                authForceResendingToken = forceResendingToken;
                // update ui
                showCodeFormTask = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showProgress(false);
                                showForm(CODE_FORM);
                                enableForm(CODE_FORM, true);
                            }
                        });
                    }
                };
                new Timer().schedule(showCodeFormTask, CODE_AUTO_VERIFY_WAIT_TIME_MS);
            }
        };
    }

    private void getRememberMe() {
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        boolean saveLogin = loginPreferences.getBoolean("saveLogin", false);
        Log.d(TAG, "loginpref: " + loginPreferences.getAll());
        if (saveLogin) {
            actvMobile.setText(loginPreferences.getString("mobile", ""));
            cbRememberMe.setChecked(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 接收返回数据
        mOpenAuthorize.onActivityResult(requestCode, resultCode, data);
    }

    public void onGetToken() {
        // 注：为了测试使用，三方客户接入忽略
//        String targetApp = mGetPkgEdit.getText().toString();
        //mOpenAuthorize.targetApp("com.healthy.umfit");
        // 点击获取Accesstoken
        mOpenAuthorize.startGetAccessToken(this);
    }

    private String getJSONString(String type, String userName, String userEmail, String userMobile, String huamiToken, String fcmToken, String password) {
        String jsonString = "";
        try {
            JSONObject jsonObject = new JSONObject();
            if (type.equalsIgnoreCase("signup")) {
                jsonObject.put("email", userEmail);
                jsonObject.put("phone_number", userMobile);
                jsonObject.put("huami_token", huamiToken);
                jsonObject.put("fcm_token", fcmToken);
                jsonObject.put("device_name", android.os.Build.MODEL);
                jsonObject.put("password", password);
                jsonObject.put("password_confirmation", password);
            } else {
                jsonObject.put("phone_number", userMobile);
                jsonObject.put("device_name", android.os.Build.MODEL);
                jsonObject.put("password", password);
            }

            jsonString = jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    private String updtJSONString( String newHuamiToken){
        String jsonString = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("huami_token", newHuamiToken);
//            jsonObject.put("userId", userId);
            jsonString = jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    private void runAPI(String type, String url, String body, String bearerToken) throws IOException {
        Log.d(TAG, "url: " + url + " body: " + body);

        okHttpClientConnectionObj = new OkHttpClientConnection(this);

        if (type.equalsIgnoreCase("POST")) {
            okHttpClientConnectionObj.postRequestWithHeader(url, body, bearerToken);
        } else {
            okHttpClientConnectionObj.getRequest(url);
        }
//        callHandler(type, url, body, requestType, finish);

    }

    private void callHandler(final String type, final String url, final String body,final String requestType, final boolean finish) {
        handlerObj = new Handler();

        runnableObj = new Runnable() {
            public void run() {
                try {
                    Log.d(TAG, "res: " + okHttpClientConnectionObj.getResult());
                    if (okHttpClientConnectionObj.getResult().equalsIgnoreCase("")) {
                        handlerObj.postDelayed(runnableObj, 3000);
                    } else {
                        String code = okHttpClientConnectionObj.getStatusCodeString();
                        String result = okHttpClientConnectionObj.getResult();
                        Log.d(TAG, "result: " + result);

                        if (code.startsWith("2", 0)) {
                            if (finish) {
                                showProgress(false);
                            }
                            if (requestType.equals(KEY_UPDATE_TOKEN)){
                                callMainActivity("","");
                            }
                            else{
                                //store in shared pref*F
                                if (type.equalsIgnoreCase("POST")) {
                                    sharedPrefObj.updatePref(KEY_LOGIN, result);
                                    sharedPrefObj.updatePref(IS_LOGGED_IN, true);
                                }
                                if (requestType.equalsIgnoreCase("signup"))
                                {
                                    callMainActivity(url, body);
                                }
                                else
                                {
                                    user = new User(sharedPrefObj.getPref(KEY_LOGIN));
                                   if (!user.getFcmToken().equalsIgnoreCase(fcmToken)){
                                        sendRegistrationToServer(fcmToken);
                                    }
                                    else
                                    {
                                        callMainActivity("", "");
                                    }
                                }
                            }

                        } else {
                            showProgress(false);

                            String message = "";
                            if (okHttpClientConnectionObj.getStatusMessage().equalsIgnoreCase("")) {
                                message = okHttpClientConnectionObj.getResult();
                            } else {
                                message = okHttpClientConnectionObj.getStatusMessage();
                            }
                            showPositiveDialog(LoginActivity.this, "Login Failed", message);
                            enableForm(PHONE_FORM, true);
                        }
                        handlerObj.removeCallbacks(runnableObj);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        handlerObj.postDelayed(runnableObj, 3000);
    }

    private void sendRegistrationToServer(String token)
    {
//        showProgress(true);
        okHttpClientConnectionObj = new OkHttpClientConnection(this);
        sharedPrefObj = new SharedPreferencesManager(getApplicationContext());
        user = new User(sharedPrefObj.getPref(KEY_LOGIN));
        String url = hostUrl + KEY_UPDATE;
        String body = updtFCMJSONString(token);
        okHttpClientConnectionObj.postRequestWithHeader(url,body,user.getUserToken());
        request_type = KEY_UPDATE_FCM_TOKEN;

    }

    private String updtFCMJSONString( String newFCMToken){
        String jsonString = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fcm_token", newFCMToken);
            jsonString = jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    private void callMainActivity(String url, String body) {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        if (url.contains(KEY_SIGNUP)) {
            i.putExtra("signup", body);
        }
        startActivity(i);
        finish();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        actvMobile.setError(null);
        actvPassword.setError(null);

        // Store values at the time of the login attempt.
        mobile = actvMobile.getText().toString();
        String pasword = actvPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(mobile)) {
            actvMobile.setError(getString(R.string.error_field_required));
            focusView = actvMobile;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //save login
            if (cbRememberMe.isChecked()) {
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("mobile", mobile);
                loginPrefsEditor.commit();
            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
            }

            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            try {
                enableForm(PHONE_FORM, false);
                //checkUserRegistration(FormatterUtil.formatPhoneNumber(mobile));
                request_type = KEY_LOGIN_MOBILE;
                runAPI("POST", hostUrl + KEY_LOGIN , getJSONString(KEY_LOGIN_MOBILE, "", "", mobile, "", fcmToken, pasword), "");
           } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void setupUi() {
        switch (ui_mode) {
            case MODE_PHONE:
                showForm(PHONE_FORM);
                break;
            case MODE_CODE:
                showForm(CODE_FORM);
                break;
        }
    }

    private void enableForm(String formName, boolean isEnabled){
        if(formName.equalsIgnoreCase(PHONE_FORM)) {
            if (isEnabled) {
                actvMobile.setEnabled(true);
                btnSignIn.setEnabled(true);
            } else {
                actvMobile.setEnabled(false);
                btnSignIn.setEnabled(false);
            }
        }else{
            if(isEnabled){
                actvCode.setEnabled(true);
                btnSendCode.setEnabled(true);
            }else{
                actvCode.setEnabled(false);
                btnSendCode.setEnabled(false);
            }
        }
    }

    private void showForm(String formName){
        if(formName.equalsIgnoreCase(PHONE_FORM)){
            llVerifyPhone.setVisibility(View.VISIBLE);
            llVerifyCode.setVisibility(View.GONE);
            ui_mode = MODE_PHONE;
        }else{
            llVerifyPhone.setVisibility(View.GONE);
            llVerifyCode.setVisibility(View.VISIBLE);
            ui_mode = MODE_CODE;
        }
    }

    private void showBtnSendCode(boolean isShown){
        if(isShown){
            btnResendCode.setVisibility(View.GONE);
            btnSendCode.setVisibility(View.VISIBLE);
        }else{
            btnResendCode.setVisibility(View.VISIBLE);
            btnSendCode.setVisibility(View.GONE);
        }
    }

    private boolean isValidEmail(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        llLogin.setVisibility(show ? View.GONE : View.VISIBLE);
        llLogin.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                llLogin.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        pbLogin.setVisibility(show ? View.VISIBLE : View.GONE);
        pbLogin.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                pbLogin.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

//        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

//    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
//        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
//        ArrayAdapter<String> adapter =
//                new ArrayAdapter<>(LoginActivity.this,
//                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
//
//        actvEmail.setAdapter(adapter);
//    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

//    private boolean isValidPassword(final String password) {
//
//        Pattern pattern;
//        Matcher matcher;
//        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!*])(?=\\S+$).{4,}$";
//        pattern = Pattern.compile(PASSWORD_PATTERN);
//        matcher = pattern.matcher(password);
//
//        return matcher.matches();
//
//    }

    private void getFcmToken() {
        final String[] token = {""};
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token[0] = Objects.requireNonNull(task.getResult()).getToken();

                        fcmToken = token[0];
                        // Log and toast
                        Log.d(TAG, "fcm token: " + token[0]);
//                        Toast.makeText(LoginActivity.this, "Token: " + token[0], Toast.LENGTH_SHORT).show();
                    }
                });

    }

    //=========================================================================
    //region // Helper Methods
    //=========================================================================
    public void startTimer() {
        resendCodeTimer = new Timer();
        resendCodeTimer.schedule(new TimerTask() {

            int second = RESEND_TIME_SECOND;

            @Override
            public void run() {
                if (second <= 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnResendCode.setEnabled(true);
                            btnResendCode.setText(getResources().getString(R.string.txt_resend_code));
                            resendCodeTimer.cancel();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnResendCode.setEnabled(false);
                            btnResendCode.setText(getResources().getString(R.string.txt_resend_code) + " (" + second + " seconds)");
                        }
                    });
                }
                second -= 1;

            }
        }, 0, 1000);
    }
    //endregion

    @Override
    protected void onStart() {
        Log.v(TAG, "Activity STARTED");
        super.onStart();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.v(TAG, "Activity RESTORED");
        super.onRestoreInstanceState(savedInstanceState);
        ui_mode = savedInstanceState.getInt("MODE");
        setupUi();
    }

    @Override
    protected void onResume() {
        Log.v(TAG, "Activity RESUMED");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.v(TAG, "Activity PAUSED");
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "Activity SAVED");
        outState.putInt("MODE", ui_mode);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        Log.v(TAG, "Activity STOPPED");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.v(TAG, "Activity RESTARTED");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.v(TAG, "Activity DESTROYED");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        if(ui_mode == MODE_CODE){
            ui_mode = MODE_PHONE;
            setupUi();
            enableForm(PHONE_FORM, true);
        }else{
            super.onBackPressed();

        }
    }

    @Override
    public void callBackFail() {
        SharedHandler.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgress(false);

                String message = "";
                if (okHttpClientConnectionObj.getStatusMessage().equalsIgnoreCase("")) {
                    message = okHttpClientConnectionObj.getResult();
                } else {
                    message = okHttpClientConnectionObj.getStatusMessage();
                }

                showPositiveDialog(LoginActivity.this, "Login Failed", message);
                enableForm(PHONE_FORM, true);
            }
        });
    }

    @Override
    public void callBackSuccess(Response response) {

        SharedHandler.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgress(false);
            }
        });

        if (okHttpClientConnectionObj.getStatusCodeString().startsWith("2", 0))
        {

            if (request_type != KEY_UPDATE_FCM_TOKEN && request_type != KEY_UPDATE_TOKEN)
            {
                sharedPrefObj.updatePref(KEY_LOGIN, okHttpClientConnectionObj.getResult());
                sharedPrefObj.updatePref(IS_LOGGED_IN, true);
            }

            user = new User(sharedPrefObj.getPref(KEY_LOGIN));
            if (user != null)
            {
                CommonUtilities.commonUser = user;
                CommonUtilities.userPrescription = user.getUserPrescription();
            }
            if (request_type.equalsIgnoreCase(KEY_LOGIN_MOBILE))
            {
                if (!user.getFcmToken().equalsIgnoreCase(fcmToken)){
                    sendRegistrationToServer(fcmToken);
                    return ;
                }
                else
                {
                    callMainActivity("", "");
                }

            }
            else if (request_type.equalsIgnoreCase(KEY_SIGNUP))
            {

                callMainActivity(signUpUrl,signUpBody);
            }
            else if (request_type.equalsIgnoreCase(KEY_UPDATE_TOKEN))
            {
                callMainActivity("","");
            }
            else if (request_type.equalsIgnoreCase(KEY_UPDATE_FCM_TOKEN))
            {
                callMainActivity("","");
            }

            //reset request type
            request_type = "";
        }
        else
        {
            Thread thread = new Thread(){
                public void run(){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            showProgress(false);
                            String message = "";
                            if (okHttpClientConnectionObj.getStatusMessage().equalsIgnoreCase("")) {
                                message = okHttpClientConnectionObj.getResult();
                            } else {
                                message = okHttpClientConnectionObj.getStatusMessage();
                            }
//                            message = okHttpClientConnectionObj.getResult();
//                            Log.v(TAG, message.toString());
                            showPositiveDialog(LoginActivity.this, "Login Failed", "Invalid phone number or password");
                            Bugsnag.notify(new RuntimeException(message));
                            enableForm(PHONE_FORM, true);
                        }
                    });
                }
            };
            thread.start();

        }

    }
}

