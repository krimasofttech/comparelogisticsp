package logistic.compare.comparelogistic.Won;

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

public class WonIntentService extends IntentService implements CommonInterface, CommonUtility, Table {
    IBinder binder = new MyBinder();
    CommonInterface commonInterface;
    EnquiryParser enquiryParser;
    DB db;
    private String url = "http://comparelogistic.in/appSender/displayCp";
    private EnquiryAsync async;
    public String ENCID;

    public CommonInterface getCommonInterface() {
        return commonInterface;
    }

    public void setCommonInterface(CommonInterface commonInterface) {
        this.commonInterface = commonInterface;
    }

    public WonIntentService() {
        super("WonIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
        }
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

    //get Customer Detail from server if not exist
    public void getCustomerDetail(String ENCID) {
        this.ENCID = ENCID;
        url = url + "/" + ENCID;
        async = new EnquiryAsync(this, url);
        if (db == null) {
            db = new DB(this);
        }

        db.open();
        boolean b = false;
        db.close();
        if (b == false)//if customer not exist
        {
            async.execute();
        } else {
            sendMessage(isExist + ":" + "true");
            Log.d("customer", "customer:" + getCust(ENCID));
            sendMessage("customer-" + getCust(ENCID));
        }
    }

    //get customer from local sqlite databsase
    public String getCust(String ENCID) {
        db.open();
      //  String customer = db.getCustomer(ENCID);
        db.close();
        return "false";
    }


    @Override
    public void RefreshEnquiry(String status, int maxid) {
        if (new ConnectionUtility(this).isConnectingToInternet()) { // here we check internet connectivity available.
            new EnquiryAsync(this, getEnquiryUrl(status, maxid)).execute();
        } else {
            //commonInterface.ReceiveEnquiry("message:No Internet Connection");
            sendMessage(message + ":" + nointerneterror); //message and nointerneterror variable from CommonUtils
        }
    }

    //modify url to url/spid/100/2 url declared in commonUtility interface
    private String getEnquiryUrl(String status, int maxid) {
        return enquiryurl + "/" + User.getInstance().getID() + "/" + maxid + "/" + status;
    }

    //this will send message to Live Fragment
    public void sendMessage(Object enquiry) {
        if (commonInterface != null) {
            commonInterface.ReceiveEnquiry(enquiry);
        }
    }


    @Override
    public void ReceiveEnquiry(Object enquiry) {

        Log.d("EnquiryReceived", enquiry.toString());
        /*if (commonInterface != null) {
            commonInterface.ReceiveEnquiry(enquiry); // pass enquiry from IntentService to LiveFragment
        }*/
        if (!enquiry.toString().contains("CUSTOMERID")) { //response is not from EnquiryAsync class
            ArrayList<EnquiryColumns> enquiryList = enquiryParser(enquiry.toString());

            for (int i = 0; i < enquiryList.size(); i++) {
                updateOrInsertEnquiry(enquiryList.get(i));
            }
            sendMessage(ENQUIRYCOUNT + ":" + enquiryList.size());
        } else {
            String customerdetail = enquiry.toString();
            updateCustomer(customerdetail);
        }
    }

    public void updateCustomer(String customer) {
        Log.d("UpdateCustomer", customer);
        if (db == null) {
            db = new DB(this);
        }
        db.open();
       // db.updateCustomerDetail(this.ENCID, customer);
        db.close();
        sendMessage("customer-" + customer);
    }

    private void updateOrInsertEnquiry(EnquiryColumns ec) {
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
            if (be == false) { //if enquiry not exist with spstatus
                updateEnquiry(ec);//update enquiry
            }
        } else {
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
        cv.put(enc.getID(), ec.getID());
        cv.put(enc.getSPSTATUS(), ec.getSPSTATUS());
        cv.put(enc.getExtra(), ec.getExtra());
        cv.put(enc.getPRICE(), ec.getPRICE());
        cv.put(enc.getDAYS(), ec.getDAYS());
        cv.put(enc.getVALIDITY(), ec.getVALIDITY());
      //  cv.put(enc.getCUSTOMER(), ec.getCUSTOMER());
        cv.put(enc.getServicetype(), ec.getServicetype());
        return cv;
    }


    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return binder;
    }


    public class MyBinder extends Binder {
        WonIntentService getService() {
            return WonIntentService.this;
        }
    }
}
