package logistic.compare.comparelogistic.Database;

/**
 * Created by Sony on 9/18/2015.
 */
public class User {

    public static User u = new User();

    String LOGOIMG = "LOGOIMG", SERVICETYPE = "SERVICETYPE", AMOUNT = "AMOUNT", CREDIT = "CREDIT", ALTERCONTACT = "ALTERCONTACT";
    public String ownername = "ownername";
    public String truckPostCount;

    public String getLoadPostCount() {
        return loadPostCount;
    }

    public void setLoadPostCount(String loadPostCount) {
        this.loadPostCount = loadPostCount;
    }

    public String getTruckPostCount() {
        return truckPostCount;
    }

    public void setTruckPostCount(String truckPostCount) {
        this.truckPostCount = truckPostCount;
    }

    public String loadPostCount;

    public String getLOGOIMG() {
        return LOGOIMG;
    }


    public void setLOGOIMG(String LOGOIMG) {
        this.LOGOIMG = LOGOIMG;
    }

    public String getSERVICETYPE() {
        return SERVICETYPE;
    }

    public void setSERVICETYPE(String SERVICETYPE) {
        this.SERVICETYPE = SERVICETYPE;
    }

    public String getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(String AMOUNT) {
        this.AMOUNT = AMOUNT;
    }

    public String getCREDIT() {
        return CREDIT;
    }

    public void setCREDIT(String CREDIT) {
        this.CREDIT = CREDIT;
    }

    public String getALTERCONTACT() {
        return ALTERCONTACT;
    }

    public void setALTERCONTACT(String ALTERCONTACT) {
        this.ALTERCONTACT = ALTERCONTACT;
    }

    public String companyname = "companyname";

    public String street = "streen";

    public String landamark = "landmark";

    public String area = "area";

    public String country = "country";

    public String state = "state";

    public String city = "city";

    public String pincode = "pincode";

    public String mobileno = "mobileno";

    public String phone = "phone";

    public String email = "email";

    public String password = "password";

    public String ID = "id";

    public String isOtpVerified = "isOtpVerified";

    public String otpNo = "otpno";

    public String isOtpSend = "isOtpSend";

    public String gcmKey = "gcmKey";

    public String getGcmKey() {
        return gcmKey;
    }

    public void setGcmKey(String gcmKey) {
        this.gcmKey = gcmKey;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getPassword() {

        return password;
    }

    public static User getInstance() {
        return u;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLandamark() {
        return landamark;
    }

    public void setLandamark(String landamark) {
        this.landamark = landamark;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getIsOtpVerified() {
        return isOtpVerified;
    }

    public void setIsOtpVerified(String isOtpVerified) {
        this.isOtpVerified = isOtpVerified;
    }

    public String getOtpNo() {
        return otpNo;
    }

    public void setOtpNo(String otpNo) {
        this.otpNo = otpNo;
    }

    public String getIsOtpSend() {
        return isOtpSend;
    }

    public void setIsOtpSend(String isOtpSend) {
        this.isOtpSend = isOtpSend;
    }
}
