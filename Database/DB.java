package logistic.compare.comparelogistic.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import logistic.compare.comparelogistic.Load.PostLoadBean;
import logistic.compare.comparelogistic.Search.Filter;
import logistic.compare.comparelogistic.Search.FilterLoad;
import logistic.compare.comparelogistic.Truck.PostTruckBean;

/**
 * Created by Sony on 9/18/2015.
 */
public class DB implements Table {
    private static EnquiryColumns ec = new EnquiryColumns();
    private static PostTruckBean pt = new PostTruckBean();
    private static User user = new User();
    private static PostLoadBean plb = new PostLoadBean();
    public static String live = "live", quoted = "quoted", won = "won", lost = "lost", declined = "declined";

    private final static String createPostTruckTable =
            "CREATE TABLE  " + "'" + PostTruckTable + "'" +
                    "(" +
                    pt.getID() + " INTEGER PRIMARY KEY ," +
                    pt.getCAPACITY() + " TEXT  NULL, " +
                    pt.getFREIGHT() + "  TEXT NULL," +
                    pt.getFROMCITY() + " TEXT NULL ," +
                    pt.getTOCITY() + "   TEXT NULL , " +
                    pt.getFTLLTL() + "   TEXT NULL ," +
                    pt.getPICKUPDATE() + " TEXT NULL ," +
                    pt.getREMARK() + "     TEXT NULL ," +
                    pt.getSTATUS() + " TEXT NULL ," +
                    pt.getSPID() + " TEXT NULL," +
                    pt.getWAY() + " TEXT NULL , " +
                    pt.getViewed() + " TEXT NULL ," +
                    pt.getTYPEOFVEHICLE() + " TEXT NULL  " + ");";


    private final static String createEnquiryTable =
            "CREATE TABLE  " + "'" + EnquiryTable + "'" +
                    "(" +
                    ec.ID + " INTEGER PRIMARY KEY ," +
                    ec.pickupcity + " TEXT  NULL " + "," +
                    ec.pickpincode + " TEXT  NULL, " +
                    ec.deliverycity + " TEXT NULL," +
                    ec.deliverypincode + " TEXT NULL ," +
                    ec.deliveryAddress + " TEXT NULL , " +
                    ec.servicetype + " TEXT NULL ," +
                    ec.PRICE + " TEXT NULL ," +
                    ec.DAYS + " TEXT NULL ," +
                    ec.VALIDITY + " TEXT NULL , " +
                    ec.REMARK + " TEXT NULL ," +
                    ec.SPSTATUS + " TEXT NULL ," +
                    ec.extra + " TEXT NULL," +
                    ec.c_date + " TEXT NULL , " +
                    ec.u_date + " TEXT NULL , " +
                    ec.CUSTOMERNAME + " TEXT NULL , " +
                    ec.MOBILE + " TEXT NULL ," +
                    ec.EMAIL + " TEXT NULL ," +
                    ec.DETAILS + " TEXT NULL ," +
                    ec.BOOKONLINESTATUS + " TEXT NULL " +
                    ");";

    private final static String createMytruckTable = "CREATE TABLE  " + "'" + MyTruckTable + "'" +
            "(" +
            pt.getID() + " INTEGER PRIMARY KEY ," +
            pt.getCAPACITY() + " TEXT  NULL, " +
            pt.getFREIGHT() + " TEXT NULL," +
            pt.getFROMCITY() + " TEXT NULL ," +
            pt.getTOCITY() + " TEXT NULL , " +
            pt.getFTLLTL() + " TEXT NULL ," +
            pt.getPICKUPDATE() + " TEXT NULL ," +
            pt.getREMARK() + " TEXT NULL ," +
            pt.getSTATUS() + " TEXT NULL ," +
            pt.getSPID() + " TEXT NULL," +
            pt.getWAY() + " TEXT NULL , " +
            pt.getTYPEOFVEHICLE() + " TEXT NULL  " + ");";
    ;
    private final static String createMyLoadTable = "CREATE TABLE  " + "'" + MyLoadTable + "'" +
            "(" +
            plb.getID() + " INTEGER PRIMARY KEY ," +
            plb.getSPID() + " TEXT  NULL, " +
            plb.getWEIGHT() + " TEXT NULL," +
            plb.getFROMCITY() + " TEXT NULL ," +
            plb.getTOCITY() + " TEXT NULL , " +
            plb.getFTLANDLTL() + " TEXT NULL ," +
            plb.getLOADCATEGORY() + " TEXT NULL ," +
            plb.getREMARK() + " TEXT NULL ," +
            plb.getSTATUS() + " TEXT NULL ," +
            plb.getLOADDISCRIPTION() + " TEXT NULL," +
            plb.getPICKUPDATE() + " TEXT NULL , " +
            pt.getTYPEOFVEHICLE() + " TEXT NULL  " + ");";


    private final static String createPostLoadTable = "CREATE TABLE  " + "'" + PostLoadTable + "'" +
            "(" +
            plb.getID() + " INTEGER PRIMARY KEY ," +
            plb.getSPID() + " TEXT  NULL, " +
            plb.getWEIGHT() + " TEXT NULL," +
            plb.getFROMCITY() + " TEXT NULL ," +
            plb.getTOCITY() + " TEXT NULL , " +
            plb.getFTLANDLTL() + " TEXT NULL ," +
            plb.getLOADCATEGORY() + " TEXT NULL ," +
            plb.getREMARK() + " TEXT NULL ," +
            plb.getSTATUS() + " TEXT NULL ," +
            plb.getLOADDISCRIPTION() + " TEXT NULL," +
            plb.getPICKUPDATE() + " TEXT NULL , " +
            plb.getViewed() + " TEXT NULL ," +
            pt.getTYPEOFVEHICLE() + " TEXT NULL  " + ");";


    private final static String createUserTable =
            "CREATE TABLE  " + "'" + UserTable + "'" +
                    "(" +
                    user.ID + " INTEGER PRIMARY KEY ," +
                    user.email + " TEXT  NULL, " +
                    user.area + " TEXT  NULL " + "," +
                    user.city + " TEXT  NULL," +
                    user.companyname + " TEXT  NULL, " +
                    user.country + " TEXT NULL ," +
                    user.landamark + " TEXT NULL," +
                    user.mobileno + " TEXT NULL ," +
                    user.ownername + " TEXT NULL , " +
                    user.password + " TEXT NULL , " +
                    user.phone + " TEXT NULL ," +
                    user.pincode + " TEXT NULL , " +
                    user.state + " TEXT NULL , " +
                    user.isOtpSend + " TEXT NULL , " +
                    user.isOtpVerified + " TEXT NULL , " +
                    user.otpNo + " TEXT NULL , " +
                    user.gcmKey + " TEXT NULL" +
                    ");";

    private SQLiteDatabase sql_database;
    Databasehelper databasehelper;
    Context ourContext;

    public DB(Context ourContext) {
        this.ourContext = ourContext;
    }

    public DB open() throws SQLException {
        databasehelper = new Databasehelper(ourContext);
        sql_database = databasehelper.getWritableDatabase();
        return this;
    }

    //This is called from OTPVerification.java Fragment to check user existence.
    public String isUserExist() {
        Cursor c = sql_database.query(UserTable, null, null, null, null, null, null);
        c.moveToFirst();
        if (c.getCount() > 0) {
            return "true";
        }
        return "false";
    }

    public boolean nullValidate(String fields) {
        if (TextUtils.isEmpty(fields)) {
            return true;
        }
        return false;
    }

    public ArrayList<PostTruckBean> getTruckListByVehicle(String vehicle, String pickup, String dest) {
        ArrayList<PostTruckBean> truckList = new ArrayList<>();
        Cursor c = null;
        String selection = "false";
        if (nullValidate(pickup) && nullValidate(dest) && !nullValidate(vehicle)) { // if only vehicle selected by user
            selection = pt.getTYPEOFVEHICLE() + " = " + "'" + vehicle + "'";

        } else if (!nullValidate(pickup) && nullValidate(dest) && nullValidate(vehicle)) { // if only pick up selected by user
            selection = pt.getFROMCITY() + " = " + "'" + pickup + "'";

        } else if (!nullValidate(pickup) && !nullValidate(vehicle) && nullValidate(dest)) { // if user selected both pickup city and vehicle
            selection = pt.getFROMCITY() + " = " + "'" + pickup + "' AND "
                    + pt.getTYPEOFVEHICLE() + "=" + "'" + vehicle + "'";

        } else if (!nullValidate(pickup) && !nullValidate(vehicle) && !nullValidate(dest)) { // if user selected all the options
            selection = pt.getFROMCITY() + " = " + "'" + pickup + "' AND "
                    + pt.getTYPEOFVEHICLE().trim() + "=" + "'" + vehicle.trim() + "' AND " + pt.getTOCITY() + " = " + "'" + dest + "'";

        } else if (!nullValidate(pickup) && !nullValidate(dest) && nullValidate(vehicle)) { // if user select both pickup and delivery
            selection = pt.getFROMCITY() + " = " + "'" + pickup + "' AND " + pt.getTOCITY() + " = " + "'" + dest + "'";
        }
        selection = getTruckSelectionFilter(selection);
        c = sql_database.query(PostTruckTable, null, selection, null, null, null, "ID DESC");

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            PostTruckBean ptb = new PostTruckBean();
            ptb.setFROMCITY(c.getString(c.getColumnIndex(pt.getFROMCITY())));
            ptb.setTOCITY(c.getString(c.getColumnIndex(pt.getTOCITY())));
            ptb.setCAPACITY(c.getString(c.getColumnIndex(pt.getCAPACITY())));
            ptb.setFREIGHT(c.getString(c.getColumnIndex(pt.getFREIGHT())));
            ptb.setFTLLTL(c.getString(c.getColumnIndex(pt.getFTLLTL())));
            ptb.setID(c.getString(c.getColumnIndex(pt.getID())));
            ptb.setPICKUPDATE(c.getString(c.getColumnIndex(pt.getPICKUPDATE())));
            ptb.setWAY(c.getString(c.getColumnIndex(pt.getWAY())));
            ptb.setTYPEOFVEHICLE(c.getString(c.getColumnIndex(pt.getTYPEOFVEHICLE())));
            ptb.setREMARK(c.getString(c.getColumnIndex(pt.getREMARK())));
            ptb.setSTATUS(c.getString(c.getColumnIndex(pt.getSTATUS())));
            ptb.setSPID(c.getString(c.getColumnIndex(pt.getSPID())));
            truckList.add(ptb);
        }
        return truckList;
    }

    public ArrayList<PostTruckBean> getMyTruckList() {
        ArrayList<PostTruckBean> truckList = new ArrayList<>();
        Cursor c = sql_database.query(MyTruckTable, null, null, null, null, null, null);
        c.moveToFirst();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            PostTruckBean ptb = new PostTruckBean();
            truckList.add(getPostTruckBean(c, ptb));
        }
        return truckList;
    }

    //here vehicle == load
    public ArrayList<PostLoadBean> getLoadListByVehicle(String vehicle, String pickup, String dest) {
        ArrayList<PostLoadBean> truckList = new ArrayList<>();
        Cursor c = null;
        String selection = "false";
        if (nullValidate(pickup) && nullValidate(dest) && !nullValidate(vehicle)) { // if only vehicle selected by user
            selection = plb.getLOADCATEGORY() + " = " + "'" + vehicle + "'";

        } else if (!nullValidate(pickup) && nullValidate(dest) && nullValidate(vehicle)) { // if only pick up selected by user
            selection = plb.getFROMCITY() + " = " + "'" + pickup + "'";

        } else if (!nullValidate(pickup) && !nullValidate(vehicle) && nullValidate(dest)) { // if user selected both pickup city and vehicle
            selection = plb.getFROMCITY() + " = " + "'" + pickup + "' AND "
                    + plb.getLOADCATEGORY() + "=" + "'" + vehicle + "'";

        } else if (!nullValidate(pickup) && !nullValidate(vehicle) && !nullValidate(dest)) { // if user selected all the options
            selection = pt.getFROMCITY() + " = " + "'" + pickup + "' AND "
                    + plb.getLOADCATEGORY().trim() + "=" + "'" + vehicle.trim() + "' AND " + plb.getTOCITY() + " = " + "'" + dest + "'";

        } else if (!nullValidate(pickup) && !nullValidate(dest) && nullValidate(vehicle)) { // if user select both pickup and delivery
            selection = plb.getFROMCITY() + " = " + "'" + pickup + "' AND " + plb.getTOCITY() + " = " + "'" + dest + "'";
        }
        selection = getLoadSelectionFilter(selection);
        c = sql_database.query(PostLoadTable, null, selection, null, null, null, "ID DESC");

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            PostLoadBean ptb = new PostLoadBean();
            truckList.add(getPostLoadBean(c, ptb));
        }
        return truckList;
    }

    public PostLoadBean getPostLoadBean(Cursor c, PostLoadBean ptb) {
        ptb.setFROMCITY(c.getString(c.getColumnIndex(pt.getFROMCITY())));
        ptb.setTOCITY(c.getString(c.getColumnIndex(pt.getTOCITY())));
        ptb.setLOADDISCRIPTION(c.getString(c.getColumnIndex(plb.getLOADDISCRIPTION())));
        ptb.setLOADCATEGORY(c.getString(c.getColumnIndex(plb.getLOADCATEGORY())));
        ptb.setFTLANDLTL(c.getString(c.getColumnIndex(plb.getFTLANDLTL())));
        ptb.setID(c.getString(c.getColumnIndex(plb.getID())));
        ptb.setPICKUPDATE(c.getString(c.getColumnIndex(pt.getPICKUPDATE())));
        ptb.setREMARK(c.getString(c.getColumnIndex(plb.getREMARK())));
        ptb.setWEIGHT(c.getString(c.getColumnIndex(plb.getWEIGHT())));
        ptb.setREMARK(c.getString(c.getColumnIndex(plb.getREMARK())));
        ptb.setSTATUS(c.getString(c.getColumnIndex(plb.getSTATUS())));
        ptb.setSPID(c.getString(c.getColumnIndex(plb.getSPID())));
        return ptb;
    }


    public String getTruckSelectionFilter(String selection) {
        String mySelection = selection;
        Filter f = Filter.getFilter();
        //ftl and ltl section
        if (f.isFtl() && !f.isLtl()) { //if only ftl is selected
            selection = selection + " AND " + pt.getFTLLTL() + " = '0' ";
        }
        if (f.isLtl() && !f.isFtl()) { // if only ltl is selected
            selection = selection + " AND " + pt.getFTLLTL() + " = '1'";
        }
        if (f.isFtl() && f.isLtl()) { // if both selected
            selection = mySelection;
        }
        //Round trip and One way
        if (f.isRoundtrip() && !f.isOneway()) {
            selection = selection + " AND " + pt.getWAY() + " = 'twoway'";
        }
        if (f.isOneway() && !f.isRoundtrip()) {
            selection = selection + " AND " + pt.getWAY() + " = 'oneway'";
        }
        if (f.isOneway() && f.isRoundtrip()) {
            selection = selection + " AND " + pt.getWAY() + " = 'oneway'" + " OR " + pt.getWAY() + " = 'twoway'";
        }

        //date sections
        if (!f.getDate().contains("false") && f.getDate() != null) { //if date is selected
            selection = selection + " AND " + pt.getPICKUPDATE() + " = " + "'" + f.getDate() + "'";
        }
        Log.d("Selection", selection);
        return selection;
    }

    //called from getLoadFromVehicle
    public String getLoadSelectionFilter(String selection) {
        String mySelection = selection;
        FilterLoad f = FilterLoad.getFilterLoad();
        //ftl and ltl section
        if (f.isFtl() && !f.isLtl()) { //if only ftl is selected -> 0
            selection = selection + " AND " + plb.getFTLANDLTL() + " = '0' ";
        }
        if (f.isLtl() && !f.isFtl()) { // if only ltl is selected -> 1
            selection = selection + " AND " + plb.getFTLANDLTL() + " = '1'";
        }
        if (f.isFtl() && f.isLtl()) { // if both selected
            selection = mySelection;
        }
        //date sections
        if (!f.getDate().contains("false") && f.getDate() != null) { //if date is selected
            selection = selection + " AND " + plb.getPICKUPDATE() + " = " + "'" + f.getDate() + "'";
        }
        Log.d("Selection", selection);
        return selection;
    }


    public String isUserVerified() {
        Cursor c = sql_database.query(UserTable, null, user.isOtpVerified + "=" + "'" + true + "'", null, null, null, null);
        c.moveToFirst();
        if (c.getCount() > 0) {
            return "true";
        }
        return "false";
    }

    public ArrayList<PostTruckBean> getTruck(String pickUpCity) {

        ArrayList<PostTruckBean> posttrucklist = new ArrayList<>();
        Cursor c = sql_database.query(PostTruckTable, null, pt.getPICKUPCITY() + "=" + "'" + pickUpCity + "'", null, null, null, "ID DESC");
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            PostTruckBean ptb = new PostTruckBean();
            posttrucklist.add(getPostTruckBean(c, ptb));
        }
        return posttrucklist;
    }

    public PostTruckBean getPostTruckBean(Cursor c, PostTruckBean ptb) {
        ptb.setFROMCITY(c.getString(c.getColumnIndex(pt.getFROMCITY())));
        ptb.setTOCITY(c.getString(c.getColumnIndex(pt.getTOCITY())));
        ptb.setCAPACITY(c.getString(c.getColumnIndex(pt.getCAPACITY())));
        ptb.setFREIGHT(c.getString(c.getColumnIndex(pt.getFREIGHT())));
        ptb.setFTLLTL(c.getString(c.getColumnIndex(pt.getFTLLTL())));
        ptb.setID(c.getString(c.getColumnIndex(pt.getID())));
        ptb.setPICKUPDATE(c.getString(c.getColumnIndex(pt.getPICKUPDATE())));
        ptb.setWAY(c.getString(c.getColumnIndex(pt.getWAY())));
        ptb.setTYPEOFVEHICLE(c.getString(c.getColumnIndex(pt.getTYPEOFVEHICLE())));
        ptb.setREMARK(c.getString(c.getColumnIndex(pt.getREMARK())));
        ptb.setSTATUS(c.getString(c.getColumnIndex(pt.getSTATUS())));
        ptb.setSPID(c.getString(c.getColumnIndex(pt.getSPID())));
        return ptb;
    }

    public String isGcmKeyExist(User user) {
        String[] columns = new String[]{this.user.gcmKey};
        Cursor c = sql_database.query(UserTable, columns, user.ID + "=" + "'" + user.getID() + "'", null, null, null, null);
        c.moveToFirst();
        if (c.getCount() > 0) {
            return "true";
        }
        return "false";
    }


    public boolean isEnquiryExist(String id) {
        Cursor c = sql_database.query(EnquiryTable, null, ec.ID + "=" + "'" + id + "'", null, null, null, null);
        if (c.getCount() > 0) {
            return true;
        }
        return false;
    }

    //HERE WE CHECK THAT ENQUIRY IS LIVE, QUOTED,WON,LOST,DECLINED
    public ArrayList<EnquiryColumns> getAllEnquiry(int status) {
        //This is enquiryList which store all enquiry.
        ArrayList<EnquiryColumns> enquiryList = new ArrayList<>();
        //query to database for enquiry
        Cursor c = sql_database.query(EnquiryTable, null, ec.SPSTATUS + "=" + "'" + status + "'", null, null, null, null, null);

        c.moveToFirst();
        // Log.d("TotalEnquiry", "" + c.getCount());
        if (c.getCount() > 0) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                EnquiryColumns ec = new EnquiryColumns();
                // c.getString(c.getColumnIndex(this.ec.ID));
                ec.setID(c.getString(c.getColumnIndex(this.ec.ID)));
                ec.setDeliverycity(c.getString(c.getColumnIndex(this.ec.deliverycity)));
                ec.setDeliverypincode(c.getString(c.getColumnIndex(this.ec.deliverypincode)));
                ec.setPickpincode(c.getString(c.getColumnIndex(this.ec.pickpincode)));
                ec.setPickupcity(c.getString(c.getColumnIndex(this.ec.pickupcity)));
                ec.setSPSTATUS(c.getString(c.getColumnIndex(this.ec.SPSTATUS)));
                ec.setExtra(c.getString(c.getColumnIndex(this.ec.extra)));
                ec.setServicetype(c.getString(c.getColumnIndex(this.ec.servicetype)));
                ec.setU_date(c.getString(c.getColumnIndex(this.ec.u_date)));
                ec.setREMARK(c.getString(c.getColumnIndex(this.ec.REMARK)));
                ec.setPRICE(c.getString(c.getColumnIndex(this.ec.PRICE)));
                ec.setDAYS(c.getString(c.getColumnIndex(this.ec.DAYS)));
                ec.setVALIDITY(c.getString(c.getColumnIndex(this.ec.VALIDITY)));
                enquiryList.add(ec);
            }
        }
        return enquiryList;
    }

    //check for enquiry existence
    public boolean IsEnquiryExist(String ID) {
        Cursor c = sql_database.query(EnquiryTable, null, this.ec.getID() + " = " + "'" + ID + "'", null, null, null, null);
        if (c.getCount() > 0) {
            return true;
        }
        return false;
    }

    //check for enquiry existence with spstatus
    public boolean IsEnquiryWithSPStatusExist(String ID, String spstatus) {
        Cursor c = sql_database.query(EnquiryTable, null, this.ec.getID() + " = " + "'" + ID + "' AND " + this.ec.getSPSTATUS() + " = " + "'" + spstatus + "'", null, null, null, null);
        if (c.getCount() > 0) {
            return true;
        }
        return false;

    }

    public long UpdateEnquiry(String table, ContentValues cv, String where) {
        long w = sql_database.update(table, cv, where, null);
        Log.d("RowUpdated", "" + w);
        return w;
    }

    public long insertEnquiry(EnquiryColumns ec, ContentValues cv) {
        long w = sql_database.insert(EnquiryTable, null, cv);
        Log.d("rowid", "" + w);
        return w;
    }

/*
    public EnquiryColumns setEnquiryText(EnquiryColumns ec, String status) {
        switch (status) {
            case "1":
                ec.setEnquiryStatusText(DB.live);
                break;
            case "2":
                ec.setEnquiryStatusText(DB.quoted);
                break;
            case "3":
                ec.setEnquiryStatusText(DB.won);
                break;
            case "4":
                ec.setEnquiryStatusText(DB.lost);
                break;
            case "5":
                ec.setEnquiryStatusText(DB.declined);
                break;

        }
        return ec;
    }*/

    public int getEnquiryMaxId(String spstatus) {
        String sql = "SELECT COUNT(*) FROM " + EnquiryTable + " WHERE  " + this.ec.SPSTATUS + " = " + "'" + spstatus + "'";
        Cursor cursor = sql_database.rawQuery(sql, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            return cursor.getInt(0);
        }
        return 0;
    }

    public void SaveEnquiry(EnquiryColumns ec) {

    }

    //This is called from OTPVerification.java Fragment while otp is generated.
    public void createUser(User us) {
        ContentValues cv = new ContentValues();
        cv.put(user.ID, us.getID());
        cv.put(user.otpNo, us.getOtpNo());
        cv.put(user.isOtpSend, us.getIsOtpSend());
        cv.put(user.isOtpVerified, us.isOtpVerified);
        long id = sql_database.insert(UserTable, null, cv);
    }

    public void saveEnquiry(EnquiryColumns data) {
        JSONObject obj;
        try {
            if (data != null) {
                ContentValues cv = getUpdatedEnquiry(data); // Get the Updated Enquiry.
                long w = sql_database.insert(EnquiryTable, null, cv);
                Log.d("saveEnquiryRes", "" + w);
            }
        } catch (Exception e) {

        }
    }

    //here we check that enquiry contains extra data or not. if extra string present then return true else false.
    public boolean isExtraExist(String id) {
        Cursor c = sql_database.query(EnquiryTable, null, this.ec.ID + "=" + "'" + id + "'", null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            String data = c.getString(c.getColumnIndex(this.ec.extra));
            if (data.contains("extra")) { // data.contains("extra") means extra data not available in database
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public String getUser() {
        Cursor c = sql_database.query(UserTable, null, null, null, null, null, null, null);
        return cur2Json(c).toString();
    }

    //This will convert cursor into jsonArray
    public JSONArray cur2Json(Cursor cursor) {

        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        rowObject.put(cursor.getColumnName(i),
                                cursor.getString(i));
                    } catch (Exception e) {
                        Log.d("TAG", e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        return resultSet;

    }

    public void updateEnquiry(EnquiryColumns ec) {
        ContentValues cv = new ContentValues();
        cv.put(this.ec.SPSTATUS, ec.getSPSTATUS());
        cv.put(this.ec.REMARK, ec.getREMARK());
        cv.put(this.ec.PRICE, ec.getPRICE());
        cv.put(this.ec.VALIDITY, ec.getVALIDITY());
        cv.put(this.ec.DAYS, ec.getDAYS());
        cv.put(this.ec.SPSTATUS, ec.getSPSTATUS());
        long w = sql_database.update(EnquiryTable, cv, this.ec.ID + "=" + "'" + ec.getID() + "'", null);
        Log.d("Tag", "response" + w);
        Log.d("EnquiryId", ec.getID());
    }

    public void updateEnquiryStatus(String ENCID, String status) {
        ContentValues cv = new ContentValues();
        cv.put(this.ec.SPSTATUS, status);
        long w = sql_database.update(EnquiryTable, cv, this.ec.ID + "=" + "'" + ENCID + "'", null);
        Log.d("res", "" + w);
    }


    public void updateView(String table, Object obj) {
        if (table.equals(PostLoadTable)) {

        }
    }


    public ContentValues getUpdatedEnquiry(EnquiryColumns ecs) {
        ContentValues cv = new ContentValues();
        cv.put(this.ec.ID, ecs.getID());
        cv.put(this.ec.c_date, ecs.getC_date());
        cv.put(this.ec.deliverycity, ecs.getDeliverycity());
        cv.put(this.ec.deliverypincode, ecs.getDeliverypincode());
        cv.put(this.ec.pickpincode, ecs.getPickpincode());
        cv.put(this.ec.pickupcity, ecs.getPickupcity());
        cv.put(this.ec.SPSTATUS, ecs.getSPSTATUS());
        cv.put(this.ec.extra, ecs.getExtra());
        cv.put(this.ec.servicetype, ecs.getServicetype());
        cv.put(this.ec.u_date, ecs.getU_date());
        // cv.put(this.ec., ecs.getID());
        cv.put(this.ec.ID, ecs.getID());
        return cv;
    }

    public boolean updateEnquiry(String id, String extra) {
        ContentValues cv = new ContentValues();
        cv.put(this.ec.extra, extra);
        long w = sql_database.update(EnquiryTable, cv, this.ec.ID + "=" + "'" + id + "'", null);
        Log.d("updated", "" + w);
        if (w > 0) {
            return true;
        }
        return false;
    }

    public void getEnquiryDetail(String id) {
        Cursor c = sql_database.query(EnquiryTable, null, this.ec.ID + "=" + "'" + id + "'", null, null, null, null, null);
        EnquiryColumns ec = null;
        c.moveToFirst();
        if (c.getCount() > 0) {
            c.moveToFirst();
            if (ec == null) {
                ec = new EnquiryColumns();
            }
            ec.setID(c.getString(c.getColumnIndex(this.ec.ID)));
            ec.setServicetype(c.getString(c.getColumnIndex(this.ec.servicetype)));
            ec.setExtra(c.getString(c.getColumnIndex(this.ec.ID)));
            ec.setID(c.getString(c.getColumnIndex(this.ec.ID)));
            ec.setID(c.getString(c.getColumnIndex(this.ec.ID)));
            ec.setID(c.getString(c.getColumnIndex(this.ec.ID)));
            ec.setID(c.getString(c.getColumnIndex(this.ec.ID)));
            ec.setID(c.getString(c.getColumnIndex(this.ec.ID)));
            ec.setID(c.getString(c.getColumnIndex(this.ec.ID)));
            ec.setID(c.getString(c.getColumnIndex(this.ec.ID)));
            ec.setID(c.getString(c.getColumnIndex(this.ec.ID)));
        }

    }

    //This method return ContentValues for User.
    public ContentValues getUpdatedUser(User user) {
        ContentValues cv = new ContentValues();
        cv.put(this.user.ID, user.getID());
        cv.put(this.user.email, user.getEmail());
        cv.put(this.user.area, user.getArea());
        cv.put(this.user.city, user.getCity());
        cv.put(this.user.companyname, user.getCompanyname());
        cv.put(this.user.country, user.getCountry());
        cv.put(this.user.landamark, user.getLandamark());
        cv.put(this.user.mobileno, user.getMobileno());
        cv.put(this.user.ownername, user.getOwnername());
        cv.put(this.user.phone, user.getPhone());
        cv.put(this.user.pincode, user.getPincode());
        cv.put(this.user.state, user.getState());
        cv.put(this.user.isOtpSend, user.getIsOtpSend());
        cv.put(this.user.isOtpVerified, user.getIsOtpVerified());
        cv.put(this.user.otpNo, user.getOtpNo());
        cv.put(this.user.gcmKey, user.getGcmKey());
        return cv;
    }

    //fetch enquiry according to spstatus
    public ArrayList<EnquiryColumns> getEnquiry(String spstatus) {
        ArrayList<EnquiryColumns> enquiryList = new ArrayList<>();
        Cursor c = sql_database.query(EnquiryTable, null, this.ec.getSPSTATUS() + " = " + "'" + spstatus + "'", null, null, null, this.ec.getID() + " DESC");
        if (c.getCount() > 0) {
            c.moveToFirst();
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                EnquiryColumns ec = getEnquiryColumns(c);
                enquiryList.add(ec);
            }
        }
        return enquiryList;
    }

    //getEnquiryColumns from Cursor
    private EnquiryColumns getEnquiryColumns(Cursor obj) {
        EnquiryColumns ec = new EnquiryColumns();
        ec.setID(obj.getString(obj.getColumnIndex(ec.getID())));
        ec.setPickupcity(obj.getString(obj.getColumnIndex(ec.getPickupcity())));
        ec.setSPSTATUS(obj.getString(obj.getColumnIndex(ec.getSPSTATUS())));
        Log.d("serviceType", obj.getString(obj.getColumnIndex(ec.getServicetype())));
        ec.setServicetype(obj.getString(obj.getColumnIndex(ec.getServicetype())));
        ec.setREMARK(obj.getString(obj.getColumnIndex(ec.getREMARK())));
        ec.setDeliverycity(obj.getString(obj.getColumnIndex(ec.getDeliverycity())));
        ec.setPickpincode(obj.getString(obj.getColumnIndex(ec.getPickpincode())));
        ec.setDeliverypincode(obj.getString(obj.getColumnIndex(ec.getDeliverypincode())));
        ec.setBOOKONLINESTATUS(obj.getString(obj.getColumnIndex(ec.getBOOKONLINESTATUS())));
        ec.setC_date(obj.getString(obj.getColumnIndex(ec.getC_date())));
        ec.setCUSTOMERNAME(obj.getString(obj.getColumnIndex(ec.getCUSTOMERNAME())));
        ec.setMOBILE(obj.getString(obj.getColumnIndex(ec.getMOBILE())));
        ec.setEMAIL(obj.getString(obj.getColumnIndex(ec.getEMAIL())));
        ec.setDeliveryAddress(ec.getDeliveryAddress());
        ec.setPRICE(obj.getString(obj.getColumnIndex(ec.getPRICE())));
        ec.setDAYS(obj.getString(obj.getColumnIndex(ec.getDAYS())));
        ec.setDETAILS(obj.getString(obj.getColumnIndex("DETAILS")));
        ec.setExtra(obj.getString(obj.getColumnIndex(ec.getExtra())));
        ec.setVALIDITY(obj.getString(obj.getColumnIndex(ec.getVALIDITY())));
        return ec;
    }


    public void UpdateUser(User us) {
        long w = sql_database.update(UserTable, getUpdatedUser(us), user.ID + "=" + us.getID(), null);
        Log.d("data", "" + w);
    }

    public ArrayList<PostLoadBean> getPostLoad() {
        ArrayList<PostLoadBean> postloadlist = new ArrayList<>();
        Cursor c = sql_database.query(PostLoadTable, null, null, null, null, null, "ID DESC");
        if (c.getCount() > 0) {
            c.moveToFirst();
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                PostLoadBean ptb = new PostLoadBean();
                ptb.setFROMCITY(c.getString(c.getColumnIndex(plb.getFROMCITY())));
                ptb.setTOCITY(c.getString(c.getColumnIndex(plb.getTOCITY())));
                ptb.setWEIGHT(c.getString(c.getColumnIndex(plb.getWEIGHT())));
                ptb.setLOADCATEGORY(c.getString(c.getColumnIndex(plb.getLOADCATEGORY())));
                ptb.setFTLANDLTL(c.getString(c.getColumnIndex(plb.getFTLANDLTL())));
                ptb.setID(c.getString(c.getColumnIndex(plb.getID())));
                ptb.setLOADDISCRIPTION(c.getString(c.getColumnIndex(plb.getLOADDISCRIPTION())));
                ptb.setREMARK(c.getString(c.getColumnIndex(plb.getREMARK())));
                ptb.setSTATUS(c.getString(c.getColumnIndex(plb.getSTATUS())));
                ptb.setPICKUPDATE(c.getString(c.getColumnIndex(plb.getPICKUPDATE())));
                ptb.setSPID(c.getString(c.getColumnIndex(plb.getSPID())));
                postloadlist.add(ptb);
            }
        }
        return postloadlist;
    }

    public ArrayList<PostTruckBean> getPostTruck() {
        ArrayList<PostTruckBean> posttrucklist = new ArrayList<>();
        Cursor c = sql_database.query(PostTruckTable, null, null, null, null, null, "ID DESC");
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            PostTruckBean ptb = new PostTruckBean();
            ptb.setFROMCITY(c.getString(c.getColumnIndex(pt.getFROMCITY())));
            ptb.setTOCITY(c.getString(c.getColumnIndex(pt.getTOCITY())));
            ptb.setCAPACITY(c.getString(c.getColumnIndex(pt.getCAPACITY())));
            ptb.setFREIGHT(c.getString(c.getColumnIndex(pt.getFREIGHT())));
            ptb.setFTLLTL(c.getString(c.getColumnIndex(pt.getFTLLTL())));
            ptb.setID(c.getString(c.getColumnIndex(pt.getID())));
            ptb.setPICKUPDATE(c.getString(c.getColumnIndex(pt.getPICKUPDATE())));
            ptb.setWAY(c.getString(c.getColumnIndex(pt.getWAY())));
            ptb.setTYPEOFVEHICLE(c.getString(c.getColumnIndex(pt.getTYPEOFVEHICLE())));
            ptb.setREMARK(c.getString(c.getColumnIndex(pt.getREMARK())));
            ptb.setSTATUS(c.getString(c.getColumnIndex(pt.getSTATUS())));
            ptb.setSPID(c.getString(c.getColumnIndex(pt.getSPID())));
            posttrucklist.add(ptb);
            Log.d("PickUpCity", ptb.getPICKUPCITY());
        }
        return posttrucklist;
    }

    public boolean IsPostTruckExist(PostTruckBean ptb) {
        Cursor c = sql_database.query(PostTruckTable, null, pt.getID() + "=" + "'" + ptb.getID() + "'", null, null, null, null);
        if (c.getCount() > 0) {
            return true;
        }
        return false;
    }

    public boolean IsPostLoadExist(PostLoadBean ptb) {
        Cursor c = sql_database.query(PostLoadTable, null, pt.getID() + "=" + "'" + ptb.getID() + "'", null, null, null, null);
        if (c.getCount() > 0) {
            return true;
        }
        return false;
    }

    public long createPostTruck(PostTruckBean ptbean) {
        ContentValues cv = new ContentValues();
        cv.put(pt.getID(), ptbean.getID());
        cv.put(pt.getSPID(), ptbean.getSPID());
        cv.put(pt.getFROMCITY(), ptbean.getFROMCITY());
        cv.put(pt.getTOCITY(), ptbean.getTOCITY());
        cv.put(pt.getCAPACITY(), ptbean.getCAPACITY());
        cv.put(pt.getTYPEOFVEHICLE(), ptbean.getTYPEOFVEHICLE());
        cv.put(pt.getFREIGHT(), ptbean.getFREIGHT());
        cv.put(pt.getFTLLTL(), ptbean.getFTLLTL());
        cv.put(pt.getPICKUPDATE(), ptbean.getPICKUPDATE());
        cv.put(pt.getREMARK(), ptbean.getREMARK());
        cv.put(pt.getSTATUS(), ptbean.getSTATUS());
        cv.put(pt.getWAY(), ptbean.getWAY());
        long w = sql_database.insert(PostTruckTable, null, cv);
        Log.d("response", "" + w);
        return w;
    }


    public long createMyPostTruck(PostTruckBean ptbean) {
        ContentValues cv = new ContentValues();
        cv.put(pt.getID(), ptbean.getID());
        cv.put(pt.getSPID(), ptbean.getSPID());
        cv.put(pt.getFROMCITY(), ptbean.getFROMCITY());
        cv.put(pt.getTOCITY(), ptbean.getTOCITY());
        cv.put(pt.getCAPACITY(), ptbean.getCAPACITY());
        cv.put(pt.getTYPEOFVEHICLE(), ptbean.getTYPEOFVEHICLE());
        cv.put(pt.getFREIGHT(), ptbean.getFREIGHT());
        cv.put(pt.getFTLLTL(), ptbean.getFTLLTL());
        cv.put(pt.getPICKUPDATE(), ptbean.getPICKUPDATE());
        cv.put(pt.getREMARK(), ptbean.getREMARK());
        cv.put(pt.getSTATUS(), ptbean.getSTATUS());
        cv.put(pt.getWAY(), ptbean.getWAY());
        long w = sql_database.insert(MyTruckTable, null, cv);
        Log.d("response", "" + w);
        return w;
    }

    public void createPostLoad(PostLoadBean polb) {
        ContentValues cv = new ContentValues();
        cv.put(plb.getID(), polb.getID());
        cv.put(plb.getSPID(), polb.getSPID());
        cv.put(plb.getPICKUPDATE(), polb.getPICKUPDATE());
        cv.put(plb.getFROMCITY(), polb.getFROMCITY());
        cv.put(plb.getTOCITY(), polb.getTOCITY());
        cv.put(plb.getREMARK(), polb.getREMARK());
        cv.put(plb.getFTLANDLTL(), polb.getFTLANDLTL());
        cv.put(plb.getWEIGHT(), polb.getWEIGHT());
        cv.put(plb.getLOADCATEGORY(), polb.getLOADCATEGORY());
        cv.put(plb.getLOADDISCRIPTION(), polb.getLOADDISCRIPTION());
        cv.put(plb.getSTATUS(), polb.getSTATUS());
        long w = sql_database.insert(PostLoadTable, null, cv);
        Log.d("RecordInserted", "" + w);
    }

    public long createMyPostLoad(PostLoadBean polb) {
        ContentValues cv = new ContentValues();
        cv.put(plb.getID(), polb.getID());
        cv.put(plb.getSPID(), polb.getSPID());
        cv.put(plb.getPICKUPDATE(), polb.getPICKUPDATE());
        cv.put(plb.getFROMCITY(), polb.getFROMCITY());
        cv.put(plb.getTOCITY(), polb.getTOCITY());
        cv.put(plb.getREMARK(), polb.getREMARK());
        cv.put(plb.getFTLANDLTL(), polb.getFTLANDLTL());
        cv.put(plb.getLOADCATEGORY(), polb.getLOADCATEGORY());
        cv.put(plb.getLOADDISCRIPTION(), polb.getLOADDISCRIPTION());
        cv.put(plb.getSTATUS(), polb.getSTATUS());
        long w = sql_database.insert(MyLoadTable, null, cv);
        Log.d("RecordInserted", "" + w);
        return w;
    }

    public int getMaxPostTruckId(String table) {
        String sql = "SELECT MAX(ID) FROM " + table;
        Cursor cursor = sql_database.rawQuery(sql, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        return 0;
    }


    public void close() {
        databasehelper.close();
    }

    @SuppressWarnings("unused")
    private static final class Databasehelper extends SQLiteOpenHelper {

        public Databasehelper(Context context) {
            super(context, "comparelogistic", null, 4);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(createUserTable);
            db.execSQL(createEnquiryTable);
            db.execSQL(createPostTruckTable);
            db.execSQL(createMytruckTable);
            db.execSQL(createMyLoadTable);
            db.execSQL(createPostLoadTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS" + "'" + UserTable + "'");

            db.execSQL("DROP TABLE IF EXISTS" + "'" + Transport + "'");

            db.execSQL("DROP TABLE IF EXISTS" + "'" + Courier + "'");

            db.execSQL("DROP TABLE IF EXISTS" + "'" + MiniTransport + "'");

            db.execSQL("DROP TABLE IF EXISTS" + "'" + PackersMovers + "'");

            db.execSQL("DROP TABLE IF EXISTS" + "'" + Cargo + "'");

            db.execSQL("DROP TABLE IF EXISTS" + "'" + EnquiryTable + "'");

            db.execSQL("DROP TABLE IF EXISTS" + "'" + Shipment + "'");

            db.execSQL("DROP TABLE IF EXISTS" + "'" + PostTruckTable + "'");

            db.execSQL("DROP TABLE IF EXISTS" + "'" + MyTruckTable + "'");

            db.execSQL("DROP TABLE IF EXISTS" + "'" + MyLoadTable + "'");

            db.execSQL("DROP TABLE IF EXISTS" + "'" + PostLoadTable + "'");

            onCreate(db);
        }
    }

}
