package logistic.compare.comparelogistic.Live;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import logistic.compare.comparelogistic.AsyncTask.EnquiryAsync;
import logistic.compare.comparelogistic.AsyncTask.EnquiryParser;
import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.CommonUtility;
import logistic.compare.comparelogistic.ConnectionUtility;
import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.Database.EnquiryColumns;
import logistic.compare.comparelogistic.Database.Table;
import logistic.compare.comparelogistic.Database.User;

public class LiveIntentService extends IntentService implements CommonUtility, CommonInterface, Table {

    CommonInterface commonInterface;
    EnquiryParser enquiryParser;
    DB db;
    IBinder Ibinder = new MyBinder();

    public CommonInterface getCommonInterface() {
        return commonInterface;
    }

    public void setCommonInterface(CommonInterface commonInterface) {
        this.commonInterface = commonInterface;
    }

    public LiveIntentService() {
        super("LiveIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return Ibinder;
    }

    //this will refresh and update enquiry from server to local sqlite database
    @Override
    public void RefreshEnquiry(String status, int maxid) {
        Log.d("ServiceRefresh", "true");
        if (new ConnectionUtility(this).isConnectingToInternet()) { // here we check internet connectivity available.
            new EnquiryAsync(this, getEnquiryUrl(status, maxid)).execute();
        } else {
            sendMessage(message + ":" + nointerneterror); //message and nointerneterror variable from CommonUtils
        }
    }

    //Modify url to url/spid/100/2 url declared in commonUtility interface
    private String getEnquiryUrl(String status, int maxid) {
        return newenquiryurl + "/" + User.getInstance().getID() + "/" + maxid + "/" + status;
    }

    //here we send message from Intent Service to LiveFragment
    public void sendMessage(Object enquiry) {
        if (commonInterface != null) {
            commonInterface.ReceiveEnquiry(enquiry);
        }
    }

    //   Here we will receive enquiry Object from EnquiryAsync Class in form of jsonObject.
    @Override
    public void ReceiveEnquiry(Object enquiry) {
        Log.d("EnquiryReceived", enquiry.toString());
        ArrayList<EnquiryColumns> enquiryList = enquiryParser(enquiry.toString());
        for (int i = 0; i < enquiryList.size(); i++) {
            updateOrInsertEnquiry(enquiryList.get(i));
        }
        sendMessage(ENQUIRYCOUNT + ":" + enquiryList.size());
    }

    private void updateOrInsertEnquiry(EnquiryColumns ec) {
        if (db == null) {
            db = new DB(this);
        }
        db.open();
        boolean b = db.isEnquiryExist(ec.getID());
        db.close();
        if (b == true) { // if enquiry exist
            Log.d("EnquiryExist", "true");
        } else { //if enquiry not exist
            insertEnquiry(ec);//Insert New  Enquiry
        }
    }

    private void updateEnquiry(EnquiryColumns ec) {
        db.open();
        db.UpdateEnquiry(EnquiryTable, getContentValues(ec), "ID = " + "'" + ec.getID() + "'");
        db.close();
    }

    private void insertEnquiry(EnquiryColumns ec) {
        db.open();
        db.insertEnquiry(ec, getContentValues(ec));
        db.close();
    }

    public ContentValues getContentValues(EnquiryColumns ec) {
        EnquiryColumns enc = new EnquiryColumns();
        ContentValues cv = new ContentValues();
        try {
            cv.put(enc.getID(), ec.getID());
            cv.put(enc.getCUSTOMERNAME(), ec.getCUSTOMERNAME());
            cv.put(enc.getVALIDITY(), ec.getVALIDITY());
            cv.put(enc.getMOBILE(), ec.getMOBILE());
            cv.put(enc.getDAYS(), ec.getDAYS());
            cv.put(enc.getEMAIL(), ec.getEMAIL());
            cv.put(enc.getBOOKONLINESTATUS(), ec.getBOOKONLINESTATUS());
            cv.put(enc.getDETAILS(), ec.getDETAILS());
            cv.put(enc.getExtra(), ec.getExtra());
            cv.put(enc.getC_date(), ec.getC_date());
            cv.put(enc.getPickupcity(), ec.getPickupcity());
            cv.put(enc.getPickpincode(), ec.getPickpincode());
            cv.put(enc.getDeliverycity(), ec.getDeliverycity());
            cv.put(enc.getDeliverypincode(), ec.getDeliverypincode());
            cv.put(enc.getDeliveryAddress(), ec.getDeliveryAddress());
            cv.put(enc.getServicetype(), ec.getServicetype());
            cv.put(enc.getSPSTATUS(), ec.getSPSTATUS());
            cv.put(enc.getPRICE(), ec.getPRICE());
            cv.put(enc.getU_date(), ec.getU_date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cv;

    }

    //Parse enquiry for conversion into ArrayList<EnquiryColumns> object
    private ArrayList<EnquiryColumns> enquiryParser(String enquiry) {
        if (!enquiry.equals("false")) {
            if (enquiryParser == null) {
                enquiryParser = new EnquiryParser(enquiry);
            }//create EnquiryParser object with set of enquiry data
            return enquiryParser.parse();//get enquiry into EnquiryColumns object array list
        }
        return new ArrayList<>();
    }

    public class MyBinder extends Binder {
        LiveIntentService getService() {
            return LiveIntentService.this;
        }
    }

}
