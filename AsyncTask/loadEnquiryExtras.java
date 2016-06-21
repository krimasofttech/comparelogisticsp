package logistic.compare.comparelogistic.AsyncTask;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Sony on 10/13/2015.
 */
public class loadEnquiryExtras {

    public String url = "http://comparelogistic.in/appSender/displayEnquiries";
    public String enquiryId;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient okClient;

    public loadEnquiryExtras() {
        //Empty Constructor.
    }

    public loadEnquiryExtras(String enquiryId) {
        this.enquiryId = enquiryId;
        okClient = new OkHttpClient();
    }

    public String sendEnquiryIdToServer() {
        try {
            if (this.enquiryId != null) {

                url = url + "/" + this.enquiryId;

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = okClient.newCall(request).execute();

                return getJsonData(response.body().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "false";
    }

    public String getJsonData(String data) {
        JSONArray array = null;
        try {
            if (data.contains("[")) {
                array = new JSONArray(data);
            }
            if (array != null) {
                //    PackersMovers p = new PackersMovers();
                Log.d("dataReceivedEnquiry", array.toString());
                JSONObject object = (JSONObject) array.get(0);
                return object.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }

        return "false";
    }
}
