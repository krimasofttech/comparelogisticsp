package logistic.compare.comparelogistic.verification;

import android.os.AsyncTask;
import android.util.Log;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.*;


import org.json.JSONObject;

import java.io.IOException;

import logistic.compare.comparelogistic.OTPVerification;

/**
 * We use OkHttp Library
 * This class used to send user mobile number to server.Server will response with {'userid:5','otp:1056'}
 * Here we will save this is in userTable will contains only single record.
 */
public class OtpBackground {
    String url, mobileNo;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient okClient;
    OTPVerification otpVerificationFragment;

    public OtpBackground(String url, String mobileNo) {
        this.url = url;
        this.mobileNo = mobileNo;
        okClient = new OkHttpClient();
        Log.d("url", this.url);
    }

    public OtpBackground() {
        //Empty Constructor
    }

    //This will contact server and get response from server.
    public String SendMobNoToServer() throws IOException {
        //  this.otpVerificationFragment = otpVerificationFragment;
        RequestBody body = RequestBody.create(JSON, mobileNo);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = okClient.newCall(request).execute();
        // Log.d("Response from server", response.body().toString());
        return getJsonData(response.body().string());
    }

    //This will convert string data from server into JSONObject.
    public String getJsonData(String response) {
        try {
            if (!response.equals("false") && !response.contains("Notice")) {

                if (response.contains("otp") && response.contains("userid")) {

                    JSONObject obj = new JSONObject(response);

                    Log.d("data", response.toString());

                    return obj.toString();
                } else {

                    JSONObject obj = new JSONObject(response);

                    return obj.toString();
                }
            }
            return "false";
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
