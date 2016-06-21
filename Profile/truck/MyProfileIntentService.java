package logistic.compare.comparelogistic.Profile.truck;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import logistic.compare.comparelogistic.AsyncTask.PostTruckAsync;
import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.Load.PostLoadBean;
import logistic.compare.comparelogistic.Truck.PostTruckBean;
import okhttp3.FormBody;

public class MyProfileIntentService extends IntentService implements CommonInterface {

    IBinder binder = new MyBinder();
    CommonInterface commonInterface;
    PostTruckAsync postTruckAsync;
    String type = "";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CommonInterface getCommonInterface() {
        return commonInterface;
    }

    public void setCommonInterface(CommonInterface commonInterface) {
        this.commonInterface = commonInterface;
    }

    public MyProfileIntentService() {
        super("MyProfileIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

        }
    }

    //execute the url and get back the data from server.
    public void getPost(String url, FormBody.Builder mBuilder) {
        postTruckAsync = new PostTruckAsync();
        postTruckAsync.setUrl(url);
        postTruckAsync.setCommonInterface(this);
        postTruckAsync.setFormEncodingBuilder(mBuilder);
        postTruckAsync.execute();
    }

    public FormBody.Builder getBuilder(String type, String SPID) {
        FormBody.Builder mBuilder = new FormBody.Builder();
        mBuilder.add("type", type);
        mBuilder.add("spid", SPID);
        return mBuilder;
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
        ArrayList myList = new ArrayList();
        if (this.getType().equals("truck")) {
            myList = getTruckList(enquiry);
        } else if (this.getType().equals("load")) {
            myList = getLoadList(enquiry.toString());
        }
        if (myList.size() > 0) {
            this.getCommonInterface().ReceiveEnquiry(myList);
        } else {
            this.getCommonInterface().ReceiveEnquiry("false");
        }
    }

    public ArrayList<PostTruckBean> getTruckList(Object enquiry) {
        ArrayList<PostTruckBean> truckList = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(enquiry.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = (JSONObject) array.get(i);
                PostTruckBean ptb = new PostTruckBean();
                ptb.setID(object.getString(ptb.getID()));
                ptb.setSPID(object.getString(ptb.getSPID()));
                ptb.setWAY(object.getString(ptb.getWAY()));
                ptb.setPICKUPDATE(object.getString(ptb.getPICKUPDATE()));
                ptb.setFTLLTL(object.getString(ptb.getFTLLTL()));
                ptb.setREMARK(object.getString(ptb.getREMARK()));
                ptb.setCAPACITY(object.getString(ptb.getCAPACITY()));
                ptb.setTYPEOFVEHICLE(object.getString(ptb.getTYPEOFVEHICLE()));
                ptb.setFREIGHT(object.getString(ptb.getFREIGHT()));
                ptb.setFROMCITY(object.getString(ptb.getFROMCITY()));
                ptb.setTOCITY(object.getString(ptb.getTOCITY()));
                ptb.setSTATUS(object.getString(ptb.getSTATUS()));
                ptb.setVIEWCOUNT(object.getString(ptb.getVIEWCOUNT()));
                truckList.add(ptb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return truckList;
    }

    public ArrayList<PostLoadBean> getLoadList(Object enquiry) {
        ArrayList<PostLoadBean> truckList = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(enquiry.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = (JSONObject) array.get(i);
                PostLoadBean ptb = new PostLoadBean();
                ptb.setID(object.getString(ptb.getID()));
                ptb.setSPID(object.getString(ptb.getSPID()));
                ptb.setPICKUPDATE(object.getString(ptb.getPICKUPDATE()));
                ptb.setFTLANDLTL(object.getString(ptb.getFTLANDLTL()));
                ptb.setREMARK(object.getString(ptb.getREMARK()));
                ptb.setLOADDISCRIPTION(object.getString(ptb.getLOADDISCRIPTION()));
                ptb.setLOADCATEGORY(object.getString(ptb.getLOADCATEGORY()));
                ptb.setWEIGHT(object.getString(ptb.getWEIGHT()));
                ptb.setFROMCITY(object.getString(ptb.getFROMCITY()));
                ptb.setTOCITY(object.getString(ptb.getTOCITY()));
                ptb.setSTATUS(object.getString(ptb.getSTATUS()));
                ptb.setVIEWCOUNT(object.getString(ptb.getVIEWCOUNT()));
                truckList.add(ptb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return truckList;
    }

    public class MyBinder extends Binder {
        public MyProfileIntentService getService() {
            return MyProfileIntentService.this;
        }
    }
}
