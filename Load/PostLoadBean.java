package logistic.compare.comparelogistic.Load;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;

import logistic.compare.comparelogistic.Database.DB;

/**
 * Created by Sony on 4/19/2016.
 */
public class PostLoadBean implements Serializable {
    String SPID = "SPID", FROMCITY = "FROMCITY", PICKUPCITY = "PICKUPCITY", TOCITY = "TOCITY", LOADCATEGORY = "LOADCATEGORY", FTLANDLTL = "FTLANDLTL",
            WEIGHT = "WEIGHT", LOADDISCRIPTION = "LOADDISCRIPTION", REMARK = "REMARK", STATUS = "STATUS";
    String ID = "ID";
    String VIEWCOUNT = "VIEWCOUNT";

    public String getVIEWCOUNT() {
        return VIEWCOUNT;
    }

    public void setVIEWCOUNT(String VIEWCOUNT) {
        this.VIEWCOUNT = VIEWCOUNT;
    }

    public static PostLoadBean plb;

    public static PostLoadBean getPlb() {
        return plb;
    }

    public static void setPlb(PostLoadBean plb) {
        PostLoadBean.plb = plb;
    }

    public String getPICKUPDATE() {
        return PICKUPDATE;
    }

    public void setPICKUPDATE(String PICKUPDATE) {
        this.PICKUPDATE = PICKUPDATE;
    }

    String PICKUPDATE = "PICKUPDATE";
    String viewed = "viewed";

    public String getViewed() {
        return viewed;
    }

    public void setViewed(String viewed) {
        this.viewed = viewed;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSPID() {

        return SPID;
    }

    public void setSPID(String SPID) {
        this.SPID = SPID;
    }

    public String getFROMCITY() {
        return FROMCITY;
    }

    public void setFROMCITY(String FROMCITY) {
        this.FROMCITY = FROMCITY;
    }

    public String getPICKUPCITY() {
        return PICKUPCITY;
    }

    public void setPICKUPCITY(String PICKUPCITY) {
        this.PICKUPCITY = PICKUPCITY;
    }

    public String getTOCITY() {
        return TOCITY;
    }

    public void setTOCITY(String TOCITY) {
        this.TOCITY = TOCITY;
    }

    public ArrayList<PostLoadBean> getLoadPost(Context mContext) {
        DB db = new DB(mContext);
        return null;
    }

    public String getLOADCATEGORY() {
        return LOADCATEGORY;
    }


    public void setLOADCATEGORY(String LOADCATEGORY) {
        this.LOADCATEGORY = LOADCATEGORY;
    }

    public String getFTLANDLTL() {
        return FTLANDLTL;
    }

    public void setFTLANDLTL(String FTLANDLTL) {
        this.FTLANDLTL = FTLANDLTL;
    }

    public String getWEIGHT() {
        return WEIGHT;
    }

    public void setWEIGHT(String WEIGHT) {
        this.WEIGHT = WEIGHT;
    }

    public String getLOADDISCRIPTION() {
        return LOADDISCRIPTION;
    }

    public void setLOADDISCRIPTION(String LOADDISCRIPTION) {
        this.LOADDISCRIPTION = LOADDISCRIPTION;
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
