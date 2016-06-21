package logistic.compare.comparelogistic.Database;

/**
 * Created by Sony on 9/18/2015.
 */
public interface Table {
    public static final String Database_Name = "comparelogistic";
    public static final int Database_version = 1;

    String UserTable = "usertable";
    String EnquiryTable = "EnquiryTable";
    String Transport = "transport";
    String Courier = "courier";
    String Cargo = "cargo";
    String Shipment = "shipment";
    String PostTruckTable = "posttrucktable";
    String PackersMovers = "packersmover";
    String MiniTransport = "minitransport";
    String MyTruckTable = "MyTruckTable";
    String MyLoadTable = "MyLoadTable";
    String PostLoadTable = "PostLoadTable";
}
