package logistic.compare.comparelogistic.Truck;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import logistic.compare.comparelogistic.AsyncTask.EnquiryAsync;
import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.CommonUtility;
import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.Database.Table;


public class MyIntentService extends IntentService implements CommonInterface, CommonUtility, Table {
    DB db = new DB(this);
    EnquiryAsync ensyc;
    String url;
    CommonInterface commonInterface;
    IBinder binder = new MyBinder();


    public CommonInterface getCommonInterface() {
        return commonInterface;
    }

    public void setCommonInterface(CommonInterface commonInterface) {
        this.commonInterface = commonInterface;
    }

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    public void updateNewTruck() {
        url = getPostTruckUrl;

        int max = 0;
        db.open();
        max = db.getMaxPostTruckId(PostTruckTable);
        db.close();
        ensyc = new EnquiryAsync(this, url + "/" + max);
        ensyc.execute();
    }

    @Override
    public void RefreshEnquiry(String status, int maxid) {

    }

    @Override
    public void ReceiveEnquiry(Object enquiry) {
        Log.d("DataReceived", enquiry.toString());
        try {
            JSONArray array = (new JSONObject(enquiry.toString())).getJSONArray("View Truck");
            Log.d("ParsedArray", array.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = (JSONObject) array.get(i);
                updatePostTruckBean(getPostTruckBean(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendMessage("error");
        }
        sendMessage("success");
    }

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return binder;
    }

    public void sendMessage(String msg) {
        if (commonInterface != null) {
            commonInterface.ReceiveEnquiry(msg);
        }
    }


    //parse from object;
    public PostTruckBean getPostTruckBean(JSONObject obj) {
        PostTruckBean ptb = new PostTruckBean();
        try {
            Log.d("JSONObjectReceived", obj.toString());
            ptb.setID(obj.getString(ID));
            ptb.setTYPEOFVEHICLE(obj.getString(TYPEOFVEHICLE));
            ptb.setSPID(obj.getString(SPID));
            ptb.setCAPACITY(obj.getString(CAPACITY));
            ptb.setFREIGHT(obj.getString(FREIGHT));
            ptb.setFROMCITY(obj.getString(FROMCITY));
            ptb.setTOCITY(obj.getString(TOCITY));
            ptb.setREMARK(obj.getString(REMARK));
            ptb.setFTLLTL(obj.getString(FTLLTL));
            ptb.setPICKUPDATE(obj.getString(PICKUPDATE));
            ptb.setWAY(obj.getString(WAY));
            ptb.setSTATUS(obj.getString(STATUS));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ptb;
    }

    private void updatePostTruckBean(PostTruckBean ptb) {
        db.open();
        boolean b = db.IsPostTruckExist(ptb);
        db.close();
        if (b == false) {
            db.open();
            db.createPostTruck(ptb);
            db.close();
        }
    }

    public class MyBinder extends Binder {
        MyIntentService getService() {
            return MyIntentService.this;
        }
    }
}
