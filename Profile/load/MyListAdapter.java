package logistic.compare.comparelogistic.Profile.load;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.Load.PostLoadBean;
import logistic.compare.comparelogistic.R;

/**
 * Created by Sony on 6/13/2016.
 */
public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.MyListHolder> {

    LayoutInflater inflater;
    CommonInterface commonInterface;

    ArrayList<PostLoadBean> myList;
    Context mContext;

    public ArrayList<PostLoadBean> getMyList() {
        return myList;
    }

    public MyListAdapter(Context mContext, ArrayList<PostLoadBean> myList, CommonInterface commonInterface) {
        this.mContext = mContext;
        this.myList = myList;
        this.commonInterface = commonInterface;
    }

    public MyListAdapter() {

    }

    public void setMyList(ArrayList<PostLoadBean> myList) {
        this.myList = myList;
    }

    public static void setmTruckListAdapter(MyListAdapter mTruckListAdapter) {
        MyListAdapter.mTruckListAdapter = mTruckListAdapter;
    }

    public static MyListAdapter mTruckListAdapter;

    public CommonInterface getCommonInterface() {
        return commonInterface;
    }

    public void setCommonInterface(CommonInterface commonInterface) {
        this.commonInterface = commonInterface;
    }

    DB db;

    public static MyListAdapter getmTruckListAdapter() {
        return mTruckListAdapter;
    }


    public MyListAdapter(ArrayList<PostLoadBean> myList, Context mContext) {
        this.myList = myList;
        this.mContext = mContext;
        this.db = new DB(mContext);
    }

    @Override
    public MyListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyListHolder mViewholder = new MyListHolder(LayoutInflater.from(this.mContext).inflate(R.layout.load_view_count, parent, false));
        return mViewholder;
    }

    @Override
    public void onBindViewHolder(MyListHolder holder, int position) {
        PostLoadBean plb = this.myList.get(position);
        holder.loadcategory.setText(plb.getLOADCATEGORY());
        holder.pickupcity.setText(plb.getFROMCITY());
        holder.movingdate.setText(plb.getPICKUPDATE());
        holder.deliverycity.setText(plb.getTOCITY());
        holder.truckid.setText(plb.getID());
        holder.mView.setTag(position);
        holder.mTruckView.setText(plb.getVIEWCOUNT());
        holder.mView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendMessage("pos:" + v.getTag().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.myList.size();
    }

    public void sendMessage(String msg) {
        if (commonInterface != null) {
            commonInterface.ReceiveEnquiry(msg);
        }
    }

    public class MyListHolder extends RecyclerView.ViewHolder {
        TextView truckid, loadcategory, pickupcity, deliverycity;
        View mView;
        TextView movingdate;
        TextView mTruckView;

        public MyListHolder(View rootView) {
            super(rootView);
            mView = rootView;
            movingdate = (TextView) rootView.findViewById(R.id.movingdate);
            truckid = (TextView) rootView.findViewById(R.id.truckid);
            mTruckView = (TextView) rootView.findViewById(R.id.viewText);
            loadcategory = (TextView) rootView.findViewById(R.id.loadcategory);
            pickupcity = (TextView) rootView.findViewById(R.id.pickupcitytext);
            deliverycity = (TextView) rootView.findViewById(R.id.deliverycity);
        }
    }
}
