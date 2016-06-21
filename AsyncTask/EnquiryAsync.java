package logistic.compare.comparelogistic.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;


import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.CommonUtility;
import logistic.compare.comparelogistic.Database.User;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EnquiryAsync extends AsyncTask<String, String, String> implements CommonUtility {

    OkHttpClient client;
    CommonInterface commonInterface;

    public String getEnquiryUrl() {
        return url;
    }

    public void setEnquiryUrl(String enquiryUrl) {
        this.url = enquiryUrl;
    }

    String url;

    public CommonInterface getCommonInterface() {
        return commonInterface;
    }

    public void setCommonInterface(CommonInterface commonInterface) {
        this.commonInterface = commonInterface;
    }

    public EnquiryAsync(CommonInterface commonInterface, String url) {
        this.commonInterface = commonInterface;
        client = new OkHttpClient();
        this.url = url;
    }

    @Override
    protected String doInBackground(String... params) {
        String response = "false";
        try {
            Log.d("Url", url);
            Request request = new Request.Builder()
                    .url(url) //spid/encid/spstatus
                    .build();
            Response res = client.newCall(request).execute();
            response = res.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
        return response;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        commonInterface.ReceiveEnquiry(s);
    }


}
