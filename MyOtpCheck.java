package logistic.compare.comparelogistic;

import android.util.Log;

import logistic.compare.comparelogistic.AsyncTask.EnquiryAsync;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.*;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Sony on 5/4/2016.
 */
public class MyOtpCheck implements CommonInterface {
    String url, mobileNo;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient okClient;
    CommonInterface commonInterface;
    EnquiryAsync enquiryAsync;

    public CommonInterface getCommonInterface() {
        return commonInterface;
    }

    public void setCommonInterface(CommonInterface commonInterface) {
        this.commonInterface = commonInterface;
    }

    public MyOtpCheck(String url, String mobileNo) {
        this.url = url;
        this.mobileNo = mobileNo;
        okClient = new OkHttpClient();
        Log.d("url", this.url);
    }

    public MyOtpCheck() {
        //Empty Constructor
    }

    //This will contact server and get response from server.
    public void SendMobNoToServer() throws IOException {
        if (enquiryAsync == null) {
            enquiryAsync = new EnquiryAsync(this, url);
        }
        enquiryAsync.execute();
        /*
        //  this.otpVerificationFragment = otpVerificationFragment;
        RequestBody body = RequestBody.create(JSON, mobileNo);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = okClient.newCall(request).execute();*/
        // Log.d("Response from server", response.body().toString());
        //return getJsonData(response.body().string());
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

    @Override
    public void RefreshEnquiry(String status, int maxid) {

    }

    @Override
    public void ReceiveEnquiry(Object enquiry) {
        if (commonInterface != null) {
            commonInterface.ReceiveEnquiry(getJsonData(enquiry.toString()));
        }
    }
}
