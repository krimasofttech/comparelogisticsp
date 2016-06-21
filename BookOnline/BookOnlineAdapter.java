package logistic.compare.comparelogistic.BookOnline;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import logistic.compare.comparelogistic.Database.EnquiryColumns;
import logistic.compare.comparelogistic.R;

/**
 * Created by Sony on 6/15/2016.
 */
public class BookOnlineAdapter extends RecyclerView.Adapter<BookOnlineAdapter.BookOnlineHolder> {
    public ArrayList<EnquiryColumns> enquiryList;

    public ArrayList<EnquiryColumns> getEnquiryList() {
        return enquiryList;
    }

    public void setEnquiryList(ArrayList<EnquiryColumns> enquiryList) {
        this.enquiryList = enquiryList;
    }

    public BookOnlineAdapter(ArrayList<EnquiryColumns> enquiryList, Context mContext) {
        this.enquiryList = enquiryList;
        this.mContext = mContext;
    }

    public Context mContext;

    @Override
    public BookOnlineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BookOnlineHolder mHolder = new BookOnlineHolder(LayoutInflater.from(mContext).inflate(R.layout.bookonline, parent, false));
        return mHolder;
    }

    @Override
    public void onBindViewHolder(BookOnlineHolder holder, int position) {
        EnquiryColumns ec = this.enquiryList.get(position);
        holder.pickuptext.setText(ec.getPickupcity());
        holder.createddate.setText("Enquiry No " + ec.getID());
        holder.serviceType.setText(ec.getServicetype());
        
    }

    @Override
    public int getItemCount() {
        return this.enquiryList.size();
    }

    public class BookOnlineHolder extends RecyclerView.ViewHolder {
        CardView header, bottom;
        TextView serviceType, pickuptext, createddate;
        LinearLayout bottomLinearicon;

        public BookOnlineHolder(View mView) {
            super(mView);
            header = (CardView) mView.findViewById(R.id.cardview);
            bottom = (CardView) mView.findViewById(R.id.cardview2);
            serviceType = (TextView) mView.findViewById(R.id.serviceType);
            pickuptext = (TextView) mView.findViewById(R.id.fromCityText);
            createddate = (TextView) mView.findViewById(R.id.createdate);
            bottomLinearicon = (LinearLayout) mView.findViewById(R.id.bottomiconview);

        }
    }
}
