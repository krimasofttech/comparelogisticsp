package logistic.compare.comparelogistic.AsyncTask;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import logistic.compare.comparelogistic.CommonUtility;
import logistic.compare.comparelogistic.Database.EnquiryColumns;

/**
 * Created by Sony on 2/22/2016.
 */
public class EnquiryParser implements CommonUtility {
    String enquiryArray;

    public EnquiryParser(String enquiryArray) {
        this.enquiryArray = enquiryArray;
    }

    public EnquiryParser() {
        //Empty Constructor
    }

    public String getEnquiryArray() {

        return enquiryArray;
    }

    public void setEnquiryArray(String enquiryArray) {
        this.enquiryArray = enquiryArray;
    }

    public ArrayList<EnquiryColumns> parse() {
        ArrayList<EnquiryColumns> enquiryList = new ArrayList<>();
        try {
            JSONArray enquiryarray = new JSONObject(this.enquiryArray).getJSONArray("enquiry"); //get JSONArray of enquiry
            for (int i = 0; i < enquiryarray.length(); i++) {
                if (enquiryarray.get(i) instanceof JSONObject) {
                    try {
                        JSONObject enquiryobj = (JSONObject) enquiryarray.get(i);
                        EnquiryColumns ec = new EnquiryColumns();
                        ec = getEnquiryColumns((enquiryobj), ec); //array contains  2 JsonObject 1 == data of enquiry_for_sp table
                        ec.setExtra(enquiryobj.toString());
                        enquiryList.add(ec);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return enquiryList;
    }

    //Add ID,SPSTATUS,SERVICETYPE to EnquiryColumns object
    private EnquiryColumns getEnquiryColumns(JSONObject obj, EnquiryColumns ec) {
        try {
            ec.setID(obj.getString("ENCID"));
            ec.setPickupcity(obj.getString(ec.getPickupcity()));
            ec.setSPSTATUS(obj.getString(ec.getSPSTATUS()));
            Log.d("serviceType", obj.getString(ec.getServicetype()));
            ec.setServicetype(obj.getString(ec.getServicetype()));
            ec.setREMARK(obj.getString(REMARK));
            ec.setDeliverycity(obj.getString(ec.getDeliverycity()));
            ec.setPickpincode(obj.getString(ec.getPickpincode()));
            ec.setDeliverypincode(obj.getString(ec.getDeliverypincode()));
            ec.setBOOKONLINESTATUS(obj.getString(ec.getBOOKONLINESTATUS()));
            ec.setC_date(obj.getString(ec.getC_date()));
            ec.setCUSTOMERNAME(obj.getString(ec.getCUSTOMERNAME()));
            ec.setMOBILE(obj.getString(ec.getMOBILE()));
            ec.setEMAIL(obj.getString(ec.getEMAIL()));
            ec.setDeliveryAddress(ec.getDeliveryAddress());
            ec.setPRICE(obj.getString(PRICE));
            ec.setDAYS(obj.getString(DAYS));
            ec.setDETAILS(obj.getString("DETAILS"));
            ec.setVALIDITY(obj.getString(VALIDITY));
            // ec.setC_date(obj.getString("C_DATE"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ec;
    }

}
