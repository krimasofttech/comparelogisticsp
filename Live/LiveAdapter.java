package logistic.compare.comparelogistic.Live;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import logistic.compare.comparelogistic.AsyncTask.EnquiryQuote;
import logistic.compare.comparelogistic.CommonUtility;
import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.Database.EnquiryColumns;
import logistic.compare.comparelogistic.Enquiry;
import logistic.compare.comparelogistic.EnquiryQuoteActivity;
import logistic.compare.comparelogistic.R;


public class LiveAdapter extends RecyclerView.Adapter<LiveAdapter.ViewHolder> implements CommonUtility {

    public static ArrayList<EnquiryColumns> enquirylist;
    public Context context;
    DB db;

    public ArrayList<EnquiryColumns> getEnquiryList() {
        return enquirylist;
    }

    public static ArrayList<EnquiryColumns> getEnquirylist() {
        return enquirylist;
    }

    public void setEnquirylist(ArrayList<EnquiryColumns> enquirylist) {
        this.enquirylist = enquirylist;
    }


    public LiveAdapter(Context context, ArrayList<EnquiryColumns> enquirylist) {
        this.context = context;
        this.enquirylist = enquirylist;
        db = new DB(context);
    }

    public void updateAdapter(ArrayList<EnquiryColumns> enquiryList) {
        if (enquiryList == null) {
            db.open();
            enquiryList = db.getEnquiry(livestatus);
            db.close();
        }
        this.enquirylist = enquiryList;
        this.notifyDataSetChanged();
    }

    @Override
    public LiveAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder mViewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_card, parent, false));
        return mViewHolder;
    }


    @Override
    public void onBindViewHolder(LiveAdapter.ViewHolder holder, int position) {
        holder.viewMore.setTag(position);
        holder.viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadEnquiry(v);
            }
        });
        holder.view.setTag(position);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFullEnquiry(v.getTag().toString());
            }
        });
        setDataOnView(holder, this.enquirylist.get(position));
    }

    public void openFullEnquiry(String pos) {
        Log.d("PositionFrag", pos);
        EnquiryColumns ec = this.enquirylist.get(Integer.parseInt(pos));
        EnquiryColumns.setEnquiryColumns(ec);
        Intent intent = new Intent(context, EnquiryQuoteActivity.class);
        Log.d("Position", pos);
        intent.putExtra("pos", pos);
        context.startActivity(intent);
    }

    private void setDataOnView(ViewHolder mViewHolder, EnquiryColumns ec) {
        try {
            mViewHolder.fromCity.setText(getCity(ec.getPickupcity()));
            mViewHolder.toCity.setText(getCity(ec.getDeliverycity()));
            mViewHolder.createOn.setText("Enquiry " + ec.getID());
            mViewHolder.serviceType.setText(EnquiryColumns.getServiceType(ec.getServicetype()));
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fromCity, toCity, serviceType, createOn, movingDate;
        Button viewMore;
        View view;

        public ViewHolder(View convertView) {
            super(convertView);
            view = convertView;
            fromCity = (TextView) convertView.findViewById(R.id.fromCity);
            toCity = (TextView) convertView.findViewById(R.id.toCity);
            serviceType = (TextView) convertView.findViewById(R.id.serviceType);
            createOn = (TextView) convertView.findViewById(R.id.createdate);
            // movingDate = (TextView) convertView.findViewById(R.id.deliverydate);
            viewMore = (Button) convertView.findViewById(R.id.viewMore);
        }

    }
}
