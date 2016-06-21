package logistic.compare.comparelogistic.Service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

import logistic.compare.comparelogistic.AsyncTask.EnquiryAsync;
import logistic.compare.comparelogistic.AsyncTask.EnquiryParser;
import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.CommonUtility;
import logistic.compare.comparelogistic.ConnectionUtility;
import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.Database.EnquiryColumns;
import logistic.compare.comparelogistic.Database.Table;
import logistic.compare.comparelogistic.Database.User;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RefreshEnquiryIntentService extends IntentService implements CommonUtility, CommonInterface, Table {
    IBinder binder = new MyBinder();
    EnquiryParser enquiryParser;
    DB db;

    public CommonInterface getCommonInterface() {
        return commonInterface;
    }

    public void setCommonInterface(CommonInterface commonInterface) {
        this.commonInterface = commonInterface;
    }

    CommonInterface commonInterface;

    public RefreshEnquiryIntentService() {
        super("RefreshEnquiryIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return binder;
    }

    @Override
    public void RefreshEnquiry(String status, int maxid) {
        if (new ConnectionUtility(this).isConnectingToInternet()) {
            Log.d("EnquiryAsync", "true");
            new EnquiryAsync(this, getallenquiryurl + "/" + User.getInstance().getID()).execute();
        } else {
            Log.d("NoInternet", "true");
            sendMessage(message + ":" + nointerneterror);
        }
    }

    //update or insert enquiry into database
    private int updateOrInsertEnquiry(EnquiryColumns ec) {
        int count = 0;
        if (db == null) {
            db = new DB(this);
        }
        db.open();
        boolean b = db.isEnquiryExist(ec.getID());
        db.close();
        if (b == true) { // if enquiry exist
            db.open();
            boolean be = db.IsEnquiryWithSPStatusExist(ec.getID(), ec.getSPSTATUS());
            db.close();
            // if (be == false) { //if enquiry not exist with spstatus
            long w = updateEnquiry(ec);// update enquiry
            if (w > 0) {
                count = count + 1;
            }
            //}
        } else {
            long w = insertEnquiry(ec);//Insert New  Enquiry
            if (w > 0) {
                count = count + 1;
            }
        }
        return count;
    }

    private long updateEnquiry(EnquiryColumns ec) {
        db.open();
        long w = db.UpdateEnquiry(EnquiryTable, getContentValues(ec), "ID = " + "'" + ec.getID() + "'");
        db.close();
        return w;
    }

    private long insertEnquiry(EnquiryColumns ec) {
        db.open();
        long w = db.insertEnquiry(ec, getContentValues(ec));
        db.close();
        return w;
    }

    public ContentValues getContentValues(EnquiryColumns ec) {
        EnquiryColumns enc = new EnquiryColumns();
        ContentValues cv = new ContentValues();
        cv.put(enc.getID(), ec.getID());
        cv.put(enc.getSPSTATUS(), ec.getSPSTATUS());
        cv.put(enc.getExtra(), ec.getExtra());
        cv.put(enc.getPRICE(), ec.getPRICE());
        cv.put(enc.getDAYS(), ec.getDAYS());
        cv.put(enc.getVALIDITY(), ec.getVALIDITY());
        // cv.put(enc.getCUSTOMER(), ec.getCUSTOMER());
        cv.put(enc.getServicetype(), ec.getServicetype());
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

    //here we send message from Intent Service to LiveFragment
    public void sendMessage(Object enquiry) {
        if (commonInterface != null) {
            commonInterface.ReceiveEnquiry(enquiry);
        }
    }

    @Override
    public void ReceiveEnquiry(Object enquiry) {
        Log.d("EnquiryReceived", enquiry.toString()); //it will return arraylist of enquirycolumns
        ArrayList<EnquiryColumns> enquiryList = enquiryParser(enquiry.toString());
        int count = 0;
        for (int i = 0; i < enquiryList.size(); i++) {
            count = count + updateOrInsertEnquiry(enquiryList.get(i));
        }
        sendMessage(ENQUIRYCOUNT + ":" + count);
    }

    public class MyBinder extends Binder {
        public RefreshEnquiryIntentService getService() {
            return RefreshEnquiryIntentService.this;
        }
    }
}