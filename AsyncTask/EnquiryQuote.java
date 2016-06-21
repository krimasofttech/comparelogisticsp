package logistic.compare.comparelogistic.AsyncTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;

import okhttp3.FormBody;


import org.json.JSONObject;

import logistic.compare.comparelogistic.CommonUtility;
import logistic.compare.comparelogistic.Database.EnquiryColumns;
import logistic.compare.comparelogistic.Database.User;
import logistic.compare.comparelogistic.verification.OkHttpExecute;

/**
 * Created by Sony on 10/30/2015.
 */
public class EnquiryQuote implements CommonUtility {
    JSONObject obj;
    String ENCID, SPID, SPSTATUS, PRICE, DAYS, VALIDITY, REMARK;
    CommunicateToEnquiryQuote frag;
    Handler handler;
    Context context;
    EnquiryColumns ec;
    String status;

    public void createHandler() {
        handler = new Handler() {
            public void handleMessage(Message msg) {
                Log.d("Received MSG", "Data " + msg.arg1);
                if (msg.arg1 == 1) {
                    //Post Requested Successfully
                    frag.sendAck(msg.obj.toString(), ec.getSPSTATUS());
                } else if (msg.arg1 == 2) {
                    //Error while Requesting
                    frag.sendAck(msg.obj.toString(), ec.getSPSTATUS());
                }
            }
        };
    }

    public interface CommunicateToEnquiryQuote {
        public void sendAck(String msg, String status);
    }

    public EnquiryQuote(CommunicateToEnquiryQuote frag, Context context, EnquiryColumns ec) {
        try {

            this.frag = (CommunicateToEnquiryQuote) frag;
            this.context = context;
            this.ec = ec;
            EnquiryColumns.setEnquiryColumns(this.ec);

            createHandler();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EnquiryQuote(Fragment frag, Context context) {
        try {
            this.frag = (CommunicateToEnquiryQuote) frag;
            this.context = context;
            this.ec = EnquiryColumns.getEnquiryColumns();
            createHandler();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EnquiryQuote(String ENCID, String SPID, String SPSTATUS, String PRICE, String DAYS, String VALIDITY, String REMARK, Fragment frag, Context context) {
        this.DAYS = DAYS;
        this.ENCID = ENCID;
        this.SPSTATUS = SPSTATUS;
        this.PRICE = PRICE;
        this.SPID = SPID;
        this.PRICE = PRICE;
        this.VALIDITY = VALIDITY;
        this.REMARK = REMARK;
        this.context = context;
        this.frag = (CommunicateToEnquiryQuote) frag;
        createHandler();
    }

    public void SendQuote() {
        Thread t = new Thread() {
            public void run() {
                try {
                    OkHttpExecute okexecute = new OkHttpExecute(url + "/updateQuote", context);
                    Log.d("dataSend", "ENCID:" + ec.getID() + "SPID:" + User.getInstance().getID() + "SPSTATUS:" + ec.getSPSTATUS() + "PRICE:" + ec.getPRICE() + "DAYS:" + ec.getDAYS() + "validity:" + ec.getVALIDITY() + "Remark:" + ec.getREMARK());
                    String response = okexecute.execute(getFormEncodingBuilder(quotestatus));
                    Log.d("responseTag", response);
                    int arg = 0;
                    if (response.contains("quote")) {
                        arg = 1;
                    } else if (response.equals("false")) {
                        arg = 2;
                    }
                    sendMessage(response, arg);
                } catch (Exception e) {
                    e.printStackTrace();
                    sendMessage("false", 2);
                }
            }
        };
        t.start();
    }

    public void sendCancelQuote() {
        Thread t = new Thread() {
            public void run() {
                try {
                    OkHttpExecute execute = new OkHttpExecute(url + "/cancelQuote", context);
                    String response = execute.execute(getFormEncodingBuilder(declinedstatus));
                    Log.d("ResponseCompare", "ResponseCompare" + response);
                    if (response.contains("ENQUIRYUPDATE")) {
                        ec.setSPSTATUS(declinedstatus);
                        EnquiryColumns.setEnquiryColumns(ec);
                        sendMessage(cancelenquiry, 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();


    }

    public void sendMessage(String message, int arg) {
        if (handler != null) {
            Message msg = new Message();
            msg.arg1 = arg;
            msg.obj = message;
            handler.sendMessage(msg);
        }
    }

    public FormBody.Builder getFormEncodingBuilder(String status) {
        FormBody.Builder encodingBuilder = null;
        //if (status.equals("all")) {
        encodingBuilder = new FormBody.Builder();
        encodingBuilder.add("ENCID", this.ec.getID());
        encodingBuilder.add("SPID", User.getInstance().getID());
        encodingBuilder.add("SPSTATUS", status);
        encodingBuilder.add("PRICE", this.ec.getPRICE());
        encodingBuilder.add("DAYS", this.ec.getDAYS());
        encodingBuilder.add("VALIDITY", this.ec.getVALIDITY());
        encodingBuilder.add("REMARK", this.ec.getREMARK());

        // } else if (status.equals("null")) {

        //   encodingBuilder = new FormEncodingBuilder()
        //         .add("ENCID", ec.getID())
        //       .add("SPID", User.getInstance().getID())
        //     .add("SPSTATUS", ec.getEnquiryStatus());
        // }
        return encodingBuilder;

    }

    public String getENCID() {
        return ENCID;
    }

    public void setENCID(String ENCID) {
        this.ENCID = ENCID;
    }

    public String getSPID() {
        return SPID;
    }

    public void setSPID(String SPID) {
        this.SPID = SPID;
    }

    public String getSPSTATUS() {
        return SPSTATUS;
    }

    public void setSPSTATUS(String SPSTATUS) {
        this.SPSTATUS = SPSTATUS;
    }

    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }

    public String getDAYS() {
        return DAYS;
    }

    public void setDAYS(String DAYS) {
        this.DAYS = DAYS;
    }

    public String getVALIDITY() {
        return VALIDITY;
    }

    public void setVALIDITY(String VALIDITY) {
        this.VALIDITY = VALIDITY;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }
}
