package logistic.compare.comparelogistic.Service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import logistic.compare.comparelogistic.AsyncTask.EnquiryAsync;
import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.Database.User;

public class CreditService extends IntentService implements CommonInterface {
    String url = "http://comparelogistic.in/appSender/getSPCredit";
    IBinder binder = new MyBinder();
    CommonInterface commonInterface;
    EnquiryAsync async;
    String logourl = "http://comparelogistic.in/appSender/getLogo";
    DB db = new DB(this);

    public CommonInterface getCommonInterface() {
        return commonInterface;
    }

    public void setCommonInterface(CommonInterface commonInterface) {
        this.commonInterface = commonInterface;
    }

    public CreditService() {
        super("CreditService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
        }
    }

    public void getUser() {
        try {
            String user; // JsonArray containing user data
            db.open();
            user = db.getUser();
            db.close();
            Log.d("user", user.toString());
            JSONArray array = new JSONArray(user);
            //Now Parse the user data into User Instance
            JSONObject object = array.getJSONObject(0);
            Log.d("objectTag", object.toString());
            //set data to User Object
            if (User.getInstance() != null) {
                User.getInstance().setID(object.getString(User.u.ID));
                User.getInstance().setGcmKey(object.getString(User.u.gcmKey));
                User.getInstance().setMobileno(object.getString(User.u.mobileno));
                User.getInstance().setEmail(object.getString(User.u.email));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCredit() {
        String myurl = url + "/" + User.getInstance().getID();
        async = new EnquiryAsync(this, myurl);
        async.execute();
    }

    public void getLogo() {
        String myurl = logourl + "/" + User.getInstance().getID();
        async = new EnquiryAsync(this, myurl);
        async.execute();
    }

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return binder;
    }

    @Override
    public void RefreshEnquiry(String status, int maxid) {

    }

    @Override
    public void ReceiveEnquiry(Object enquiry) {
        Log.d("Received", enquiry.toString());
        if (commonInterface != null)
            commonInterface.ReceiveEnquiry(enquiry);
    }

    public class MyBinder extends Binder {
        public CreditService getService() {
            return CreditService.this;
        }
    }
}
