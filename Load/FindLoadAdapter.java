package logistic.compare.comparelogistic.Load;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.R;
import logistic.compare.comparelogistic.Truck.PostTruckBean;

/**
 * Created by Sony on 4/20/2016.
 */
public class FindLoadAdapter extends RecyclerView.Adapter<FindLoadAdapter.MyViewHolder> {

    LayoutInflater inflater;
    Context mContext;
    ArrayList<PostLoadBean> trucklist;
    CommonInterface commonInterface;

    public ArrayList<PostLoadBean> getTrucklist() {
        return trucklist;
    }

    public void setTrucklist(ArrayList<PostLoadBean> trucklist) {
        this.trucklist = trucklist;
    }

    public CommonInterface getCommonInterface() {
        return commonInterface;
    }

    public void setCommonInterface(CommonInterface commonInterface) {
        this.commonInterface = commonInterface;
    }

    DB db;

    public static FindLoadAdapter getmTruckListAdapter() {
        return mTruckListAdapter;
    }

    public void updateTruckList() {
        if (db == null) {
            db = new DB(mContext);
        }
        db.open();
        ArrayList<PostLoadBean> loadlist = db.getPostLoad();
        db.close();
        this.trucklist = loadlist;
        notifyDataSetChanged();
    }

    public void updateLoadList(ArrayList<PostLoadBean> loadlist) {
        this.trucklist = loadlist;
        notifyDataSetChanged();
    }

    public static void setmTruckListAdapter(FindLoadAdapter mTruckListAdapter) {
        FindLoadAdapter.mTruckListAdapter = mTruckListAdapter;
    }

    public FindLoadAdapter(Context mContext, ArrayList<PostLoadBean> trucklist, CommonInterface commonInterface) {
        this.inflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.trucklist = trucklist;
        db = new DB(mContext);
        this.commonInterface = commonInterface;
    }

    public FindLoadAdapter() {
    }

    public static FindLoadAdapter mTruckListAdapter;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder mvh = new MyViewHolder(inflater.inflate(R.layout.load_view, parent, false));
        return mvh;
    }

    public void sendMessage(String msg) {
        if (commonInterface != null) {
            commonInterface.ReceiveEnquiry(msg);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PostLoadBean plb = this.trucklist.get(position);
        holder.loadcategory.setText(plb.getLOADCATEGORY());
        holder.pickupcity.setText(plb.getFROMCITY());
        holder.movingdate.setText(plb.getPICKUPDATE());
        holder.deliverycity.setText(plb.getTOCITY());
        holder.truckid.setText(plb.getID());
        holder.mView.setTag(position);
        holder.mView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendMessage("pos:" + v.getTag().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.trucklist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView truckid, loadcategory, pickupcity, deliverycity;
        View mView;
        TextView movingdate;

        public MyViewHolder(View rootView) {
            super(rootView);
            mView = rootView;
            movingdate = (TextView) rootView.findViewById(R.id.movingdate);
            truckid = (TextView) rootView.findViewById(R.id.truckid);
            loadcategory = (TextView) rootView.findViewById(R.id.loadcategory);
            pickupcity = (TextView) rootView.findViewById(R.id.pickupcitytext);
            deliverycity = (TextView) rootView.findViewById(R.id.deliverycity);
        }
    }
}
