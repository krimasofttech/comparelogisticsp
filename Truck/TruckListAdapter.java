package logistic.compare.comparelogistic.Truck;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.R;

/**
 * Created by Sony on 4/17/2016.
 */
public class TruckListAdapter extends RecyclerView.Adapter<TruckListAdapter.TruckViewHolder> {

    LayoutInflater inflater;
    Context mContext;
    public ArrayList<PostTruckBean> trucklist;

    public static TruckListAdapter getmTruckListAdapter() {
        return mTruckListAdapter;
    }

    public static void setmTruckListAdapter(TruckListAdapter mTruckListAdapter) {
        TruckListAdapter.mTruckListAdapter = mTruckListAdapter;
    }

    CommonInterface commonInterface;
    DB db;
    public static TruckListAdapter mTruckListAdapter;

    public CommonInterface getCommonInterface() {
        return commonInterface;
    }

    public void setCommonInterface(CommonInterface commonInterface) {
        this.commonInterface = commonInterface;
    }

    public ArrayList<PostTruckBean> getTrucklist() {
        return trucklist;
    }

    public void setTrucklist(ArrayList<PostTruckBean> trucklist) {
        this.trucklist = trucklist;
    }

    public TruckListAdapter(Context mContext, ArrayList<PostTruckBean> trucklist, CommonInterface commonInterface) {
        this.inflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.trucklist = trucklist;
        this.commonInterface = commonInterface;
    }

    public TruckListAdapter(Context mContext, CommonInterface commonInterface) {
        this.mContext = mContext;
        this.commonInterface = commonInterface;
    }

    @Override
    public TruckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TruckViewHolder mViewholder = new TruckViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.truck_view, parent, false));
        return mViewholder;
    }

    public void updateTruckList() {
        if (db == null) {
            db = new DB(mContext);
        }
        db.open();
        this.trucklist = db.getPostTruck();
        db.close();
        notifyDataSetChanged();
    }

    public void updateTruckList(ArrayList<PostTruckBean> list) {
        this.trucklist = list;
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(TruckViewHolder holder, int position) {
        PostTruckBean ptb = this.trucklist.get(position);
        int src = R.drawable.arrowright;
        String textway = "Oneway";
        if (ptb.getWAY().toString().equals("twoway")) {
            src = R.drawable.arrowleft;
            textway = "Roundtrip";
        }

        Log.d("Way", ptb.getWAY());
        holder.mView.setTag(position);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // v.setBackgroundResource(R.drawable.citybackground2);
                sendMessage("pos:" + v.getTag().toString());
            }
        });
        setData(holder, ptb, src, textway);
    }

    public void sendMessage(String msg) {
        if (commonInterface != null) {
            commonInterface.ReceiveEnquiry(msg);
        }
    }

    public void setData(TruckViewHolder tvh, PostTruckBean ptb, int src, String text) {
        tvh.truckid.setText(ptb.getID());
        tvh.movingdate.setText(ptb.getPICKUPDATE());
        tvh.pickupcity.setText(ptb.getFROMCITY());
        tvh.deliverycity.setText(ptb.getTOCITY());
        tvh.truckid.setText(ptb.getID());
        tvh.typeofvehicle.setText(" " + ptb.getTYPEOFVEHICLE());
        tvh.way.setImageResource(src);
        tvh.textofway.setText(text);
    }

    @Override
    public int getItemCount() {
        return this.trucklist.size();
    }

    public class TruckViewHolder extends RecyclerView.ViewHolder {
        TextView truckid, typeofvehicle, pickupcity, textofway, deliverycity, movingdate;
        ImageView way;
        View mView;

        public TruckViewHolder(View rootView) {
            super(rootView);
            mView = rootView;
            truckid = (TextView) rootView.findViewById(R.id.truckid);
            typeofvehicle = (TextView) rootView.findViewById(R.id.typeofvehicle);
            pickupcity = (TextView) rootView.findViewById(R.id.pickupcitytext);
            deliverycity = (TextView) rootView.findViewById(R.id.deliverycity);
            way = (ImageView) rootView.findViewById(R.id.way);
            movingdate = (TextView) rootView.findViewById(R.id.movingdate);
            textofway = (TextView) rootView.findViewById(R.id.textofway);
        }
    }
}
