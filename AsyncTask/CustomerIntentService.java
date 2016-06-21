package logistic.compare.comparelogistic.AsyncTask;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Binder;

import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.Database.DB;

public class CustomerIntentService extends IntentService implements CommonInterface {
    public CustomerIntentService() {
        super("CustomerIntentService");
    }

    private String url = "http://comparelogistic.in/appSender/displayCp";
    public EnquiryAsync async;
    public String ENCID;
    private DB db;

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
        }
    }

    @Override
    public void RefreshEnquiry(String status, int maxid) {
        this.ENCID = status;
        if (async == null) {
            url = url + "/" + ENCID;
            async = new EnquiryAsync(this, url);
        }
        if (db == null) {
            db = new DB(this);
        }
        db.open();
        boolean b = false;
        db.close();
        if (b == false)//if customer not exist
            async.execute();
    }

    @Override
    public void ReceiveEnquiry(Object enquiry) {
        String customerdetail = enquiry.toString();
        updateCustomer(customerdetail);
    }

    public void updateCustomer(String customer) {
        if (db == null) {
            db = new DB(this);
        }
        db.open();
       // db.updateCustomerDetail(this.ENCID, customer);
        db.close();
    }

    public class MyBinder extends Binder {
        CustomerIntentService getService() {
            return CustomerIntentService.this;
        }
    }

}
