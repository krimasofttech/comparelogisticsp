package logistic.compare.comparelogistic.Quoted;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import logistic.compare.comparelogistic.CommonUtility;
import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.Database.EnquiryColumns;
import logistic.compare.comparelogistic.EnquiryQuoteActivity;
import logistic.compare.comparelogistic.R;

/**
 * Created by Sony on 2/25/2016.
 */
public class QuotedAdapter extends RecyclerView.Adapter<QuotedAdapter.ViewHolder> implements CommonUtility {
    static ArrayList<EnquiryColumns> enquirylist;
    public Context context;
    DB db;

    public static ArrayList<EnquiryColumns> getEnquirylist() {
        return enquirylist;
    }

    public static void setEnquirylist(ArrayList<EnquiryColumns> enquirylist) {
        QuotedAdapter.enquirylist = enquirylist;
    }

    public QuotedAdapter(Context context, ArrayList<EnquiryColumns> enquirylist) {
        this.context = context;
        this.enquirylist = enquirylist;
        db = new DB(context);
    }

    public void updateAdapter(ArrayList<EnquiryColumns> enquiryList) {
        if (null == enquiryList) {
            db.open();
            enquiryList = db.getEnquiry(quotestatus);
            db.close();
        }
        this.enquirylist = enquiryList;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder mHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.quoted_card, parent, false));
        return mHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.viewMore.setTag(position);
        holder.rootView.setTag(String.valueOf(position));
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFullEnquiry(v.getTag().toString());
            }
        });
        setDataOnView(holder, enquirylist.get(position));
    }

    public void openFullEnquiry(String pos) {
        EnquiryColumns ec = this.enquirylist.get(Integer.parseInt(pos));
        EnquiryColumns.setEnquiryColumns(ec);
        Intent intent = new Intent(context, QuotedActivity.class);
        Log.d("Position", pos);
        intent.putExtra("pos", pos);
        context.startActivity(intent);
    }

    private void setDataOnView(ViewHolder mViewHolder, EnquiryColumns ec) {
        try {
            JSONObject enquiry = new JSONObject(ec.getExtra());
            JSONArray array = enquiry.getJSONArray(enquiry.keys().next());
            JSONObject obj1 = array.getJSONObject(1);
            // JSONObject obj2 = array.getJSONObject(0);
            mViewHolder.fromCity.setText(getCity(obj1.getString(PICKUPCITY)));
            mViewHolder.toCity.setText(getCity(obj1.getString(DELIVERYCITY)));
            mViewHolder.createOn.setText("Enquiry " + ec.getID());
            // Log.d("ServiceType", ec.getServicetype());
            mViewHolder.serviceType.setText(EnquiryColumns.getServiceType(ec.getServicetype()));
            mViewHolder.remark.setText(ec.getREMARK());
            mViewHolder.validity.setText(ec.getVALIDITY());
            mViewHolder.price.setText("₹" + ec.getPRICE());
            mViewHolder.days.setText(ec.getDAYS() + " days");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //this will return Nagpur from Nagpur,Maharashtra,India
    private String getCity(String fromCity) {
        return fromCity.split(",")[0];
    }

    @Override
    public int getItemCount() {
        return this.enquirylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fromCity, toCity, serviceType, createOn, movingDate;
        Button viewMore;
        TextView price, days, validity, remark;
        ImageView statusicon;
        View rootView;

        public ViewHolder(View convertView) {
            super(convertView);
            rootView = convertView;
            fromCity = (TextView) convertView.findViewById(R.id.fromCity);
            toCity = (TextView) convertView.findViewById(R.id.toCity);
            serviceType = (TextView) convertView.findViewById(R.id.serviceType);
            createOn = (TextView) convertView.findViewById(R.id.createdate);
            viewMore = (Button) convertView.findViewById(R.id.viewMore);
            price = (TextView) convertView.findViewById(R.id.price);
            days = (TextView) convertView.findViewById(R.id.days);
            validity = (TextView) convertView.findViewById(R.id.validity);
            remark = (TextView) convertView.findViewById(R.id.remark);
            statusicon = (ImageView) convertView.findViewById(R.id.statusicon);
            statusicon.setImageResource(android.R.drawable.btn_star_big_off);
        }
    }
}
