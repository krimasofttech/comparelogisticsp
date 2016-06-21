package logistic.compare.comparelogistic.Live;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import logistic.compare.comparelogistic.CommonUtility;

/**
 * Created by Sony on 3/1/2016.
 */
public class LiveEnquiryJsonParser implements CommonUtility {
    JSONObject obj;
    String[] quoteArray;
    String[] yesnomap;
    String[] serviceType;
    HashMap<String, String> keychanger;
    Iterator keyset;
    String[] status;

    public LiveEnquiryJsonParser(JSONObject obj) {
        this.obj = obj;
        Log.d("ObjectReceived", this.obj.toString());
        quoteArray = new String[]{
                PRICE, REMARK, VALIDITY, DAYS, ID, REASON, SPSTATUS, "U_DATE"};
        status = new String[]{SPSTATUS, "U_DATE"};
        keychanger = new HashMap<>();
        yesnomap = new String[]{"Yes", "No"};
        serviceType = new String[]{"Air", "Train", "Road"};
        addHashMap();
        keyset = this.obj.keys();
    }

    public void addKeyValue(String key, String value) {
        keychanger.put(key, value);
    }

    public void addHashMap() {
        addKeyValue("ENCID", "Enquiry No");
        addKeyValue("NOOFCOURIER", "TotalParcel");
        addKeyValue("COURIERINSURANCE", "CourierInsurance");
        addKeyValue("SERVICETYPE", "Send Through");
        addKeyValue("C_DATE", "CreationDate");
        addKeyValue("PICKADD", "PickUp Address");
        addKeyValue("DELIVERYADD", "Delivery Address");
        addKeyValue("PICKCOMMODITY", "Commodity");
        addKeyValue("COURIERTYPE", "COURIER");
        addKeyValue("PRESTYPE", "PickUp Residence");
        addKeyValue("DRESTYPE", "Delivery Residence");
        addKeyValue("PELEVATOR", "PickUp Elevator Available");
        addKeyValue("DELEVATOR", "Delivery Elevator Available");
        addKeyValue("PELEVATOR", "PickUp Elevator Available");
        addKeyValue("STORAGEREQUIRED", "Storage Required");
        addKeyValue("MOVE", "Customer Wants to Move");
        addKeyValue("COMMODITYTYPE", "Commodity");
        addKeyValue("COMMODITYDESCRIPTION", "Commodity Description");
        addKeyValue("FTLLTL", "Ftl/Ltl");
    }

    public JSONObject getObj() {
        return obj;
    }

    public void setObj(JSONObject obj) {
        this.obj = obj;
    }

    //parse 1 and 2 value into yes and no
    public String yesnomapper(String value) {
        try {
            value = yesnomap[Integer.parseInt(value) - 1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public String serviceTypeMapper(String value) {
        return serviceType[Integer.parseInt(value) - 1];
    }

    public JSONObject parse() {
        JSONObject obj = this.getObj();
        JSONObject newObj = new JSONObject();
        try {
            if (this.getObj() != null) {
                for (int i = 0; i < obj.length(); i++) {
                    String data = getJsonData(i);
                    if (validate(data)) {
                        String[] rowData = data.split(":");
                        try {
                            if (rowData.length > 1) {
                                if (rowData[0] != null && rowData[1] != null) {
                                    String key = caseChanger(changeKey(rowData[0]));
                                    String value = caseChanger(rowData[1]);
                                    if (!key.equalsIgnoreCase("Send Through") && value.matches("[1-2]")) {
                                        value = yesnomapper(value);
                                    }
                                    if (key.equalsIgnoreCase("Send Through")) { //here we change serviceType Air =1 ,Road = 3,Train = 2;
                                        value = serviceTypeMapper(value);
                                    }
                                    // Log.d("key/value", "keyvalue" + key + "/" + value);
                                    newObj.put(key, value);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("Object", newObj.toString());
        return newObj;
    }

    public JSONObject quotedParse() {
        JSONObject obj = this.getObj();
        JSONObject newObj = new JSONObject();
        try {
            if (this.getObj() != null) {
                for (int i = 0; i < obj.length(); i++) {
                    String data = getJsonData(i);
                    if (validateQuoted(data)) {
                        String[] rowData = data.split(":");
                        try {
                            if (rowData.length > 1) {
                                if (rowData[0] != null && rowData[1] != null) {
                                    //Log.d("Key", rowData[0]);
                                    // Log.d("Value", rowData[1]);
                                    String key = caseChanger(changeKey(rowData[0]));
                                    String value = caseChanger(rowData[1]);
                                    if (!key.equalsIgnoreCase("Send Through") && value.matches("[1-2]")) {
                                        value = yesnomapper(value);
                                    }
                                    if (key.equalsIgnoreCase("Send Through")) { //here we change serviceType Air =1 ,Road = 3,Train = 2;
                                        value = serviceTypeMapper(value);
                                    }
                                    // Log.d("key/value", "keyvalue" + key + "/" + value);
                                    newObj.put(key, value);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("Object", newObj.toString());
        return newObj;

    }

    //Change String case to HELLLO to Hello
    public String caseChanger(String key) {
        if (key != null)
            return key.substring(0, 1).toUpperCase() + key.substring(1, key.length()).toLowerCase();
        else {
            return key;
        }
    }

    public String changeKey(String key) {
        try {
            if (keychanger.containsKey(key)) {
                key = keychanger.get(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("KeyReturn", key);
        return key;
    }

    public boolean validateQuoted(String data) {
        if (data.equals("false")) {
            return false;
        } else if (data.split(":").length < 1) {
            return false;
        } else if (statusExist(data)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean validate(String data) {
        if (data.equals("false")) {
            return false;
        } else if (data.split(":").length < 1) {
            return false;
        } else if (rowExist(data.split(":")[0])) {
            return false;
        } else {
            return true;
        }
    }

    public boolean rowExist(String key) {
        boolean b = false;
        for (String s : quoteArray) {
            if (key.equalsIgnoreCase(s)) {
                b = true;
                break;
            }
        }
        return b;
    }

    public boolean statusExist(String key) {
        boolean b = false;
        for (String s : status) {
            if (key.equalsIgnoreCase(s)) {
                b = true;
            }
        }
        return b;
    }

    // this is called from getView and getItem in this class
    // for getting key and value in string format like key:value

    public String getJsonData(int pos) {
        String dataToReturn = "false";

        if (this.getObj() != null) {
            int i = 0;
            keyset = this.getObj().keys();
            while (keyset.hasNext()) {
                //   Log.d("pos==i", "pos" + pos + "i" + i);
                String key = keyset.next().toString();
                if (pos == i) {
                    try {
                        dataToReturn = key + ":" + this.getObj().getString(key);
                        //  Log.d("dataToReturn", dataToReturn);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                i++;

            }
        }
        Log.d("data", dataToReturn);
        return dataToReturn;
    }

}
