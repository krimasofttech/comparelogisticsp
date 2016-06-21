package logistic.compare.comparelogistic.Load;

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
import logistic.compare.comparelogistic.CommonUtility;
import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.Database.Table;
import logistic.compare.comparelogistic.Truck.PostTruckBean;

public class LoadIntentService extends IntentService implements CommonInterface, CommonUtility, Table {
    DB db = new DB(this);
    EnquiryAsync ensyc;
    String url;
    CommonInterface commonInterface;
    IBinder binder = new MyBinder();

    public LoadIntentService() {
        super("LoadIntentService");
    }

    public CommonInterface getCommonInterface() {
        return commonInterface;
    }

    public void setCommonInterface(CommonInterface commonInterface) {
        this.commonInterface = commonInterface;
    }

    //this will be called from fragment or activity.
    public void updateNewTruck() {
        url = getPostLoadUrl;
        int max = 0;
        db.open();
        max = db.getMaxPostTruckId(PostLoadTable);
        db.close();
        if (ensyc == null) {
            ensyc = new EnquiryAsync(this, url + "/" + max);
        }
        ensyc.execute();
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
    public PostLoadBean getPostTruckBean(JSONObject obj) {
        PostLoadBean ptb = new PostLoadBean();
        try {
            Log.d("JSONObjectReceived", obj.toString());
            ptb.setID(obj.getString(ID));
            ptb.setLOADCATEGORY(obj.getString("LOADCATEGORY"));
            ptb.setSPID(obj.getString(SPID));
            ptb.setWEIGHT(obj.getString("WEIGHT"));
            ptb.setLOADDISCRIPTION(obj.getString("LOADDISCRIPTION"));
            ptb.setFROMCITY(obj.getString(FROMCITY));
            ptb.setTOCITY(obj.getString(TOCITY));
            ptb.setREMARK(obj.getString(REMARK));
            ptb.setFTLANDLTL(obj.getString("FTLANDLTL"));
            ptb.setPICKUPDATE(obj.getString(PICKUPDATE));
            ptb.setSTATUS(obj.getString(STATUS));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ptb;
    }


    private void updatePostLoadBean(PostLoadBean plb) {
        db.open();
        boolean b = db.IsPostLoadExist(plb);
        db.close();
        if (b == false) {
            db.open();
            db.createPostLoad(plb);
            db.close();
        }
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
        }
    }

    @Override
    public void RefreshEnquiry(String status, int maxid) {

    }

    @Override
    public void ReceiveEnquiry(Object enquiry) {
        Log.d("DataReceived", enquiry.toString());
        try {
            JSONArray array = new JSONArray(enquiry.toString());
            Log.d("ParsedArray", array.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = (JSONObject) array.get(i);
                updatePostLoadBean(getPostTruckBean(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendMessage("error");
        }
        sendMessage("success");
    }

    public class MyBinder extends Binder {
        LoadIntentService getService() {
            return LoadIntentService.this;
        }
    }
}
