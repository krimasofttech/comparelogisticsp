package logistic.compare.comparelogistic.Truck;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Sony on 4/15/2016.
 */
public class PostTruckBean implements Serializable {

    public static PostTruckBean getPtb() {
        return ptb;
    }

    public static void setPtb(PostTruckBean ptb) {
        PostTruckBean.ptb = ptb;
    }

    public static PostTruckBean ptb = new PostTruckBean();
    String ID = "ID";
    String SPID = "SPID";
    String PICKUPCITY = "FROMCITY";
    String FROMCITY = "FROMCITY";
    String TOCITY = "TOCITY";
    String TYPEOFVEHICLE = "TYPEOFVEHICLE";
    String FTLLTL = "FTLLTL";
    String CAPACITY = "CAPACITY";
    String FREIGHT = "FREIGHT";
    String REMARK = "REMARK";
    String STATUS = "STATUS";
    String WAY = "WAY";
    String viewed = "viewed";
    String VIEWCOUNT = "VIEWCOUNT";

    public String getVIEWCOUNT() {
        return VIEWCOUNT;
    }

    public void setVIEWCOUNT(String VIEWCOUNT) {
        this.VIEWCOUNT = VIEWCOUNT;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


    public String getViewed() {
        return viewed;
    }

    public void setViewed(String viewed) {
        this.viewed = viewed;
    }

    public String getPICKUPDATE() {
        return PICKUPDATE;
    }

    public void setPICKUPDATE(String PICKUPDATE) {
        this.PICKUPDATE = PICKUPDATE;
    }

    String PICKUPDATE = "PICKUPDATE";

    public String getSPID() {
        return SPID;
    }

    public String getWAY() {
        return WAY;
    }

    public void setWAY(String WAY) {
        this.WAY = WAY;
    }

    public void setSPID(String SPID) {

        this.SPID = SPID;
    }

    public String getPICKUPCITY() {
        return PICKUPCITY;
    }

    public void setPICKUPCITY(String PICKUPCITY) {
        this.PICKUPCITY = PICKUPCITY;
    }

    public String getFROMCITY() {
        return FROMCITY;
    }

    public void setFROMCITY(String FROMCITY) {
        this.FROMCITY = FROMCITY;
    }

    public String getTOCITY() {
        return TOCITY;
    }

    public void setTOCITY(String TOCITY) {
        this.TOCITY = TOCITY;
    }

    public String getTYPEOFVEHICLE() {
        return TYPEOFVEHICLE;
    }

    public void setTYPEOFVEHICLE(String TYPEOFVEHICLE) {
        this.TYPEOFVEHICLE = TYPEOFVEHICLE;
    }

    public String getFTLLTL() {
        return FTLLTL;
    }

    public void setFTLLTL(String FTLLTL) {
        this.FTLLTL = FTLLTL;
    }

    public String getCAPACITY() {
        return CAPACITY;
    }

    public void setCAPACITY(String CAPACITY) {
        this.CAPACITY = CAPACITY;
    }

    public String getFREIGHT() {
        return FREIGHT;
    }

    public void setFREIGHT(String FREIGHT) {
        this.FREIGHT = FREIGHT;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

}
