package com.healthy.umfit.utils;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.healthy.umfit.utils.CommonUtilities.JSON;
import static com.healthy.umfit.TagName.*;

public class OkHttpClientConnection {
    public static final String TAG = OkHttpClientConnection.class.getSimpleName();

    private String huamiToken;
    private String result = "";
    private int statusCode = 0;
    private String statusMessage = "";

    public interface ServiceResponseCallBack
    {
        void callBackFail();
        void callBackSuccess(Response response) throws IOException;
    }

    private ServiceResponseCallBack mServiceResponseCallBack;

    public OkHttpClientConnection() {

    }

    public OkHttpClientConnection(ServiceResponseCallBack serviceResponseCallBack) {
        mServiceResponseCallBack = serviceResponseCallBack;
    }

    public OkHttpClientConnection(String huamiToken, ServiceResponseCallBack serviceResponseCallBack) {
        this.huamiToken = huamiToken;
        mServiceResponseCallBack = serviceResponseCallBack;
    }

    public void postRequest(String url, String postBody) {
        OkHttpClient client = new OkHttpClient();
        client.newBuilder().connectTimeout(15, TimeUnit.SECONDS).callTimeout(25, TimeUnit.SECONDS);

        RequestBody body = RequestBody.create(JSON, postBody);

        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "error: " + e.getMessage());
                result = e.getMessage();
                call.cancel();
                if(mServiceResponseCallBack != null)
                {
                    mServiceResponseCallBack.callBackFail();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, response.body().toString());

                try {
                    statusCode = response.code();
                    statusMessage = response.message();
                    result = response.body().string();
                    setStatusCode(statusCode);
                    setResult(result);
                    if (mServiceResponseCallBack != null)
                    {
                        mServiceResponseCallBack.callBackSuccess(response);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }



    public void getRequest(String url) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result = e.getMessage();
                if(mServiceResponseCallBack != null)
                {
                    mServiceResponseCallBack.callBackFail();
                }
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.d(TAG, response.body().string());

                try {
                    statusCode = response.code();
                    statusMessage = response.message();
                    result = response.body().string();
                    setResult(result);
                    if (mServiceResponseCallBack != null)
                    {
                        mServiceResponseCallBack.callBackSuccess(response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void postRequestWithHeader(String url, String postBody, String token) {
        OkHttpClient client = new OkHttpClient();
        client.newBuilder().connectTimeout(15, TimeUnit.SECONDS).callTimeout(25, TimeUnit.SECONDS);

        RequestBody body = RequestBody.create(JSON, postBody);

        final Request request = new Request.Builder()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "error: " + e.getMessage());
                result = e.getMessage();
                call.cancel();
                if(mServiceResponseCallBack != null)
                {
                    mServiceResponseCallBack.callBackFail();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, response.body().toString());

                try {
                    statusCode = response.code();
                    statusMessage = response.message();
                    result = response.body().string();
                    setStatusCode(statusCode);
                    setResult(result);
                    if (mServiceResponseCallBack != null)
                    {
                        mServiceResponseCallBack.callBackSuccess(response);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getRequestWithHeader(String url, String token) {

        OkHttpClient client = new okhttp3.OkHttpClient();

        final Request request = new Request.Builder()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result = e.getMessage();
                call.cancel();
                if (mServiceResponseCallBack != null)
                {
                    mServiceResponseCallBack.callBackFail();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "call: " + call);
                Log.d(TAG, "response: " + response);

                try {
                    statusCode = response.code();
                    statusMessage = response.message();
                    result = response.body().string();
                    setResult(result);
                    if (mServiceResponseCallBack != null)
                    {
                        mServiceResponseCallBack.callBackSuccess(response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public void getRequestWithHeader(String url) {

        OkHttpClient client = new okhttp3.OkHttpClient();

        final Request request = new Request.Builder()
                .header("Authorization", "Bearer " + huamiToken)
                .header("Content-Type", "application/json")
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result = e.getMessage();
                call.cancel();
                if (mServiceResponseCallBack != null)
                {
                    mServiceResponseCallBack.callBackFail();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "call: " + call);
                Log.d(TAG, "response: " + response);

                try {
                    statusCode = response.code();
                    statusMessage = response.message();
                    result = response.body().string();
                    setResult(result);
                    if (mServiceResponseCallBack != null)
                    {
                        mServiceResponseCallBack.callBackSuccess(response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void patchRequest(String url, String postBody) {
        OkHttpClient client = new OkHttpClient();
        client.newBuilder().connectTimeout(15, TimeUnit.SECONDS).callTimeout(25, TimeUnit.SECONDS);

        RequestBody body = RequestBody.create(JSON, postBody);

        final Request request = new Request.Builder()
                .url(url)
                .patch(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "error: " + e.getMessage());
                result = e.getMessage();
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, response.body().toString());

                try {
                    statusCode = response.code();
                    statusMessage = response.message();
                    result = response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusCodeString(){
        return String.valueOf(statusCode);
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
