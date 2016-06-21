package logistic.compare.comparelogistic.Database;

import java.io.Serializable;

import logistic.compare.comparelogistic.AsyncTask.EnquiryQuote;

public class EnquiryColumns implements Comparable<EnquiryColumns>, Serializable {
    public static EnquiryColumns ec;

    public static void setEnquiryColumns(EnquiryColumns enc) {
        ec = enc;
    }

    public static EnquiryColumns getEnquiryColumns() {
        return ec;
    }

    public EnquiryQuote enquiryQuote;


    public static String getServiceType(String arg) {
        String stringToReturn = "false";
        switch (arg) {
            case "1":
                stringToReturn = "Packers  Movers";
                break;
            case "2":
                stringToReturn = "Transport";
                break;
            case "3":
                stringToReturn = "Courier";
                break;
            case "4":
                stringToReturn = "Shipping";
                break;
            case "5":
                stringToReturn = "Cargo";
                break;
            case "6":
                stringToReturn = "Mini-Transport";
                break;
        }
        return stringToReturn;
    }

    public String SPSTATUS = "SPSTATUS";

    public String getSPSTATUS() {
        return SPSTATUS;
    }

    public void setSPSTATUS(String SPSTATUS) {
        this.SPSTATUS = SPSTATUS;
    }

    public String pickupcity = "PICKUPCITY";

    public String deliverycity = "DELIVERYCITY";

    public String pickpincode = "PICKPINCODE";

    public String CUSTOMERNAME = "FULLNAME";

    public String MOBILE = "MOBILE";

    public String EMAIL = "EMAIL";

    public String DETAILS = "DETAILS";

    public String getBOOKONLINESTATUS() {
        return BOOKONLINESTATUS;
    }

    public void setBOOKONLINESTATUS(String BOOKONLINESTATUS) {
        this.BOOKONLINESTATUS = BOOKONLINESTATUS;
    }

    public String BOOKONLINESTATUS = "BOOKONLINESTATUS";

    public String getCUSTOMERNAME() {
        return CUSTOMERNAME;
    }

    public void setCUSTOMERNAME(String CUSTOMERNAME) {
        this.CUSTOMERNAME = CUSTOMERNAME;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getDETAILS() {
        return DETAILS;
    }

    public void setDETAILS(String DETAILS) {
        this.DETAILS = DETAILS;
    }

    public String deliverypincode = "DELIVERYPINCODE";

    public String servicetype = "SERVICETYPE";

    public String c_date = "C_DATE";

    public String u_date = "U_DATE";

    public String ID = "ID";

    public String PRICE = "PRICE";

    public EnquiryQuote getEnquiryQuote() {
        return enquiryQuote;
    }

    public void setEnquiryQuote(EnquiryQuote enquiryQuote) {
        this.enquiryQuote = enquiryQuote;
    }

    public String DAYS = "DAYS";

    public String VALIDITY = "VALIDITY";

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getVALIDITY() {
        return VALIDITY;
    }


    public void setVALIDITY(String VALIDITY) {
        this.VALIDITY = VALIDITY;
    }

    public String getDAYS() {
        return DAYS;
    }

    public void setDAYS(String DAYS) {
        this.DAYS = DAYS;
    }

    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }

    public String REMARK = "COMMENT";

    public String extra = "extra";

    public String deliveryAddress = "DELIVERYADD";

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPickupcity() {
        return pickupcity;
    }

    public void setPickupcity(String pickupcity) {
        this.pickupcity = pickupcity;
    }

    public String getDeliverycity() {
        return deliverycity;
    }

    public void setDeliverycity(String deliverycity) {
        this.deliverycity = deliverycity;
    }

    public String getPickpincode() {
        return pickpincode;
    }

    public void setPickpincode(String pickpincode) {
        this.pickpincode = pickpincode;
    }

    public String getDeliverypincode() {
        return deliverypincode;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public void setDeliverypincode(String deliverypincode) {
        this.deliverypincode = deliverypincode;
    }

    public String getC_date() {
        return c_date;
    }

    public void setC_date(String c_date) {
        this.c_date = c_date;
    }

    public String getServicetype() {
        return servicetype;
    }

    public void setServicetype(String servicetype) {
        this.servicetype = servicetype;
    }


    public String getU_date() {
        return u_date;
    }

    public void setU_date(String u_date) {
        this.u_date = u_date;
    }

    @Override
    public String toString() {
        return "{" +
                "deliveryAddress:'" + deliveryAddress + '\'' +
                ", extra:'" + extra + '\'' +
                ", REMARK:'" + REMARK + '\'' +
                ", VALIDITY:'" + VALIDITY + '\'' +
                ", DAYS:'" + DAYS + '\'' +
                ", PRICE:'" + PRICE + '\'' +
                ", ID:'" + ID + '\'' +
                ", u_date:'" + u_date + '\'' +
                ", c_date:'" + c_date + '\'' +
                ", servicetype:'" + servicetype + '\'' +
                ", deliverypincode:'" + deliverypincode + '\'' +
                ", BOOKONLINESTATUS:'" + BOOKONLINESTATUS + '\'' +
                ", DETAILS:'" + DETAILS + '\'' +
                ", EMAIL:'" + EMAIL + '\'' +
                ", MOBILE:'" + MOBILE + '\'' +
                ", CUSTOMERNAME:'" + CUSTOMERNAME + '\'' +
                ", pickpincode:'" + pickpincode + '\'' +
                ", deliverycity:'" + deliverycity + '\'' +
                ", pickupcity:'" + pickupcity + '\'' +
                ", SPSTATUS:'" + SPSTATUS + '\'' +
                '}';
    }

    @Override
    public int compareTo(EnquiryColumns another) {
        return 0;
    }
}
