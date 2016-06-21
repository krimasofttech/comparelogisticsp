package logistic.compare.comparelogistic;


public interface CommonUtility {
    public static final String projectNo = "537881686390";
    public static final String url = "http://www.comparelogistic.in/appSender";
    public String enquiryurl = "http://comparelogistic.in/appSender/disStatus";//1-args = spid, 2-args=encid,3-args=spstatus
    public String newenquiryurl = "http://comparelogistic.in/appSender/getEnquiriesSpwise";//1-args = spid,2 = offset, 3 = spstatus
    public String getallenquiryurl = "http://comparelogistic.in/appSender/displayEnquirySp";//1-args = spid
    public String nointerneterror = "No Internet Connection";
    public String message = "message";
    public String updateProfile = "http://comparelogistic.in/appSender/UpdateProfile";
    public String SUCCESS = "SUCCESS";
    String livestatus = "1";
    String getPostTruckUrl = "http://comparelogistic.in/appSender/getpostTruckData";//1==maxid
    String getPostLoadUrl = "http://comparelogistic.in/appSender/getpostLoadData";
    String browserKey = "AIzaSyBvIqfJ6Cghr9bR4IA4GQxYZgk2lO7gqsg";
    String google_api_url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=ban&key=" + browserKey;
    String cancelenquiry = "cancelenquiry";
    String getprofile = "http://www.comparelogistic.in/appSender/getProfile";
    public String posttruckurl = "http://www.comparelogistic.in/appSender/inserttruckLoad";
    String quotestatus = "2";
    String insertLoadUrl = "http://www.comparelogistic.in/appSender/postLoadData";
    String wonstatus = "3";
    String isExist = "isExist";
    String app_id = "202319";
    String key = "3a4acf593c245e9190e0";
    String secret = "f199412bc764a5d6b87a";
    String declinedstatus = "5";
    String loststatus = "4";
    String pos = "position";
    public String ID = "ID";
    public String ENQUIRYCOUNT = "ENQUIRYCOUNT";
    public String SERVICETYPE = "SERVICETYPE";
    public String PICKUPCITY = "PICKUPCITY";
    public String DELIVERYCITY = "DELIVERYCITY";
    public String PICKPINCODE = "PICKPINCODE";
    public String DELIVERYPINCODE = "DELIVERYPINCODE";
    public String SPSTATUS = "SPSTATUS";
    public String PRICE = "PRICE";
    public String DAYS = "DAYS";
    public String VALIDITY = "VALIDITY";
    public String REMARK = "REMARK";
    public String REASON = "REASON";
    String CAPACITY = "CAPACITY";
    String PICKUPDATE = "PICKUPDATE";
    String STATUS = "STATUS";
    String FROMCITY = "FROMCITY";
    String TOCITY = "TOCITY";
    String FTLLTL = "FTLLTL";
    String SPID = "SPID";
    String TYPEOFVEHICLE = "TYPEOFVEHICLE";
    String WAY = "WAY";
    String FREIGHT = "FREIGHT";
    public String CUSTOMERID = "CUSTOMERID";
    public String FIRSTNAME = "FIRSTNAME";
    public String LASTNAME = "LASTNAME";
    public String FULLNAME = "FULLNAME";
    public String MOBILE = "MOBILE";
    public String EMAIL = "EMAIL";
}
