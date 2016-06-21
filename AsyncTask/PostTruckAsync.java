package logistic.compare.comparelogistic.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.*;

import logistic.compare.comparelogistic.CommonInterface;

/**
 * Created by Sony on 4/15/2016.
 */
public class PostTruckAsync extends AsyncTask<String, String, String> {
    String url;
    CommonInterface commonInterface;
    OkHttpClient client;

    public PostTruckAsync(String url, CommonInterface commonInterface, FormBody.Builder formEncodingBuilder) {
        this.url = url;
        this.commonInterface = commonInterface;
        this.formEncodingBuilder = formEncodingBuilder;
        client = new OkHttpClient();
    }

    public CommonInterface getCommonInterface() {
        return commonInterface;
    }

    public PostTruckAsync() {
        client = new OkHttpClient();
    }

    public void setCommonInterface(CommonInterface commonInterface) {
        this.commonInterface = commonInterface;
    }

    public FormBody.Builder getFormEncodingBuilder() {
        return formEncodingBuilder;
    }

    public void setFormEncodingBuilder(FormBody.Builder formEncodingBuilder) {
        this.formEncodingBuilder = formEncodingBuilder;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    FormBody.Builder formEncodingBuilder;

    @Override
    protected String doInBackground(String... params) {
        String response = "false";
        try {
            Log.d("Url", url);
            Request request = new Request.Builder()
                    .url(url) //spid/encid/spstatus
                    .post(this.formEncodingBuilder.build())
                    .build();
            Response res = client.newCall(request).execute();
            response = res.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return response;
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
        Log.d("ResponseFromServer", "SERVERRESPONSE" + s);
        if (this.commonInterface != null) {
            this.commonInterface.ReceiveEnquiry(s);
        }
    }
}
