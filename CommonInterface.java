package logistic.compare.comparelogistic;

/**
 * Created by Sony on 2/7/2016.
 */
public interface CommonInterface {
    //for refreshment of each enquiry
    public void RefreshEnquiry(String status, int maxid);

    public void ReceiveEnquiry(Object enquiry);
}
