package logistic.compare.comparelogistic.Adapter;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Sony on 2/29/2016.
 */
public class EnquiryJsonCreator {
    String enquiryExtra;
    JSONObject objectToReturn;

    public EnquiryJsonCreator(String enquiryExtra) {
        this.enquiryExtra = enquiryExtra;
    }

    public String getEnquiryExtra() {
        return enquiryExtra;
    }

    public void setEnquiryExtra(String enquiryExtra) {
        this.enquiryExtra = enquiryExtra;
    }

    public JSONObject RaiseEnquiryJsonObject(String data) {
        if (objectToReturn == null) {
            objectToReturn = new JSONObject();
        }
        try {
            JSONObject obj = new JSONObject(data);
            Iterator keyset = obj.keys();
            while (keyset.hasNext()) {
                String key = keyset.next().toString();
                if (obj.get(key) instanceof JSONArray) {
                    JSONArray array = (JSONArray) obj.get(key);
                    for (int i = 0; i < array.length(); i++) {
                        if (array.get(i) instanceof JSONObject) {
                            JSONObject jobj = (JSONObject) array.get(i);
                            RaiseEnquiryJsonObject(jobj.toString());
                        }
                    }
                } else if (obj.get(key) instanceof JSONObject) {
                    JSONObject object = (JSONObject) obj.get(key);
                    RaiseEnquiryJsonObject(object.toString());
                } else if (obj.get(key) instanceof String) {
                    if (!key.equals("ID")) {
                        objectToReturn.put(key, obj.get(key));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("jsonobject", objectToReturn.toString());
        return objectToReturn;
    }


}
