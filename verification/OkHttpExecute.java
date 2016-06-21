package logistic.compare.comparelogistic.verification;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import logistic.compare.comparelogistic.ConnectionUtility;
import logistic.compare.comparelogistic.MainActivity;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Sony on 9/24/2015.
 */
public class OkHttpExecute {
    String url;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient okClient = new OkHttpClient();
    String json;
    Context context;

    public OkHttpExecute(String url, String json, Context context) {
        this.url = url;
        this.json = json;
        this.context = context;
    }

    public OkHttpExecute() {
        //Empty Constructor
    }

    public OkHttpExecute(Context context) {
        this.context = context;
    }

    public OkHttpExecute(String url, Context context) {
        this.url = url;
        this.context = context;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String execute() throws IOException {
        if (new ConnectionUtility(context).isConnectingToInternet()) {
            RequestBody body;

            Response response = null;

            Log.d("url", this.url);

            JSONObject obj;

            try {

                obj = new JSONObject(json);

                body = new FormBody.Builder()
                        .add("userid", obj.getString("userid"))
                        .add("otp", obj.getString("otp"))
                        .add("verified", obj.getString("verified"))
                        .build();

                Request request = new Request.Builder()
                        .url(this.url)
                        .post(body)
                        .build();

                response = okClient.newCall(request).execute();

            } catch (Exception e) {

                e.printStackTrace();

                return "false";
            }

            return getJsonData(response.body().string());
        }
        Toast.makeText(this.context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        return "false";
    }

    public String execute(FormBody.Builder FEB) throws IOException {
        Log.d("Post Requested", "true");
        if (new ConnectionUtility(context).isConnectingToInternet()) {
            RequestBody body;

            Response response = null;

            Log.d("url", this.url);

            try {

                body = FEB.build();

                Request request = new Request.Builder()
                        .url(this.url)
                        .post(body)
                        .build();

                response = okClient.newCall(request).execute();

            } catch (Exception e) {

                e.printStackTrace();

                return "false";
            }

            return response.body().string();
        }
        Toast.makeText(this.context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        return "false";

    }

    //This will convert string data from server into JSONObject.
    public String getJsonData(String response) {
        try {
            if (!response.equals("false") && !response.contains("Notice")) {

                if (response.contains("userid") && response.contains("verified")) {

                    JSONObject obj = new JSONObject(response);

                    Log.d("data", response.toString());

                    return obj.toString();
                }
            }
            return "false";
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }

}
