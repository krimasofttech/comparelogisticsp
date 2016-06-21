package logistic.compare.comparelogistic.Profile;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;

import org.json.JSONArray;
import org.json.JSONObject;

import logistic.compare.comparelogistic.AsyncTask.PostTruckAsync;
import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.Database.User;
import okhttp3.FormBody;

public class ProfileIntentService extends IntentService {
    IBinder binder = new MyBinder();
    CommonInterface commonInterface;
    PostTruckAsync postTruckAsync;
    DB db = new DB(this);
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public CommonInterface getCommonInterface() {
        return commonInterface;
    }

    public void setCommonInterface(CommonInterface commonInterface) {
        this.commonInterface = commonInterface;
    }

    public ProfileIntentService() {
        super("ProfileIntentService");
    }

    public void updateProfile(String column, String value) {
        postTruckAsync = new PostTruckAsync();
        postTruckAsync.setCommonInterface(this.getCommonInterface());
        postTruckAsync.setUrl(this.getUrl());
        postTruckAsync.setFormEncodingBuilder(getBuilder(column, value));
        postTruckAsync.execute();
    }

    public void getProfile() {
        postTruckAsync = new PostTruckAsync();
        postTruckAsync.setCommonInterface(this.getCommonInterface());
        postTruckAsync.setUrl(this.getUrl());
        postTruckAsync.setFormEncodingBuilder(getSpBuilder());
        postTruckAsync.execute();
    }

    public FormBody.Builder getSpBuilder() {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("SPID", User.getInstance().getID());
        return builder;
    }

    public FormBody.Builder getBuilder(String column, String value) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("COLUMN", column);
        builder.add("VALUE", value);
        builder.add("SPID", User.getInstance().getID());
        return builder;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
        }
    }

    public User getUser(String data) {
        try {
            JSONArray array = new JSONArray(data);
            JSONObject obj = (JSONObject) array.get(0);
            User.getInstance().setEmail(obj.getString("HOEMAIL"));
            User.getInstance().setMobileno(obj.getString("MOBILE"));
            User.getInstance().setArea(obj.getString("AREA"));
            User.getInstance().setCity(obj.getString("CITY"));
            User.getInstance().setLandamark(obj.getString("LANDMARK"));
            User.getInstance().setCountry(obj.getString("COUNTRY"));
            User.getInstance().setState(obj.getString("STATE"));
            User.getInstance().setPincode(obj.getString("PINCODE"));
            User.getInstance().setLOGOIMG(obj.getString("LOGOIMG"));
            User.getInstance().setStreet(obj.getString("STREET"));
            User.getInstance().setOwnername(obj.getString("OWNERNAME"));
            User.getInstance().setCompanyname(obj.getString("COMPANYNAME"));
            User.getInstance().setALTERCONTACT(obj.getString("ALTERCONTACT"));
            User.getInstance().setSERVICETYPE(obj.getString("SERVICETYPE"));
            User.getInstance().setAMOUNT(obj.getString("AMOUNT"));
            User.getInstance().setCREDIT(obj.getString("CREDIT"));
            User.getInstance().setPhone(obj.getString("PHONENO"));
            if (!array.isNull(1)) {
                User.getInstance().setTruckPostCount(array.get(1).toString());
            }
            if (!array.isNull(2)) {
                User.getInstance().setLoadPostCount(array.get(2).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return User.getInstance();
    }


    public class MyBinder extends Binder {
        public ProfileIntentService getService() {
            return ProfileIntentService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return binder;
    }
}
