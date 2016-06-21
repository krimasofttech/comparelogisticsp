package logistic.compare.comparelogistic.Declined;

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
import logistic.compare.comparelogistic.Lost.LostActivity;
import logistic.compare.comparelogistic.R;

/**
 * Created by Sony on 2/5/2016.
 */
public class DeclinedAdapter extends RecyclerView.Adapter<DeclinedAdapter.ViewHolder> implements CommonUtility {

   static ArrayList<EnquiryColumns> enquirylist;
    public Context context;
    DB db;

    public static ArrayList<EnquiryColumns> getEnquirylist() {
        return enquirylist;
    }

    public void setEnquirylist(ArrayList<EnquiryColumns> enquirylist) {
        this.enquirylist = enquirylist;
    }

    public void openFullEnquiry(String pos) {
        EnquiryColumns ec = this.enquirylist.get(Integer.parseInt(pos));
        EnquiryColumns.setEnquiryColumns(ec);
        Intent intent = new Intent(context, DeclinedActivity.class);
        Log.d("Position", pos);
        intent.putExtra("pos", pos);
        context.startActivity(intent);
    }


    public DeclinedAdapter(Context context, ArrayList<EnquiryColumns> enquirylist) {
        this.context = context;
        this.enquirylist = enquirylist;
        db = new DB(context);
        //  Collections.sort(this.enquirylist,Collections.reverseOrder());
    }

    public void updateAdapter(ArrayList<EnquiryColumns> enquiryList) {
        if (null == enquiryList) {
            db.open();
            enquiryList = db.getEnquiry(declinedstatus);
            db.close();
        }
        this.enquirylist = enquiryList;
        this.notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder mViewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_card, parent, false));
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.viewMore.setTag(position);
        holder.viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadEnquiry(v);
            }
        });
        holder.rootView.setTag(String.valueOf(position));
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFullEnquiry(v.getTag().toString());
            }
        });
        setDataOnView(holder, this.enquirylist.get(position));
    }

    //this will return Nagpur from Nagpur,Maharashtra,India
    private String getCity(String fromCity) {
        return fromCity.split(",")[0];
    }

    private void setDataOnView(ViewHolder mViewHolder, EnquiryColumns ec) {
        try {
            JSONObject enquiry = new JSONObject(ec.getExtra());
            JSONArray array = enquiry.getJSONArray(enquiry.keys().next());
            JSONObject obj1 = array.getJSONObject(1);
            mViewHolder.fromCity.setText(getCity(obj1.getString(PICKUPCITY)));
            mViewHolder.toCity.setText(getCity(obj1.getString(DELIVERYCITY)));
            mViewHolder.createOn.setText("Enquiry " + ec.getID());
            // Log.d("ServiceType", ec.getServicetype());
            mViewHolder.serviceType.setText(EnquiryColumns.getServiceType(ec.getServicetype()));
            mViewHolder.statusicon.setImageResource(R.drawable.wrong);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.enquirylist.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fromCity, toCity, serviceType, createOn, movingDate;
        Button viewMore;
        ImageView statusicon;
        View rootView;

        public ViewHolder(View convertView) {
            super(convertView);
            rootView = convertView;
            fromCity = (TextView) convertView.findViewById(R.id.fromCity);
            toCity = (TextView) convertView.findViewById(R.id.toCity);
            serviceType = (TextView) convertView.findViewById(R.id.serviceType);
            createOn = (TextView) convertView.findViewById(R.id.createdate);
            // movingDate = (TextView) convertView.findViewById(R.id.deliverydate);
            viewMore = (Button) convertView.findViewById(R.id.viewMore);
            statusicon = (ImageView) convertView.findViewById(R.id.statusicon);
        }

    }
}
