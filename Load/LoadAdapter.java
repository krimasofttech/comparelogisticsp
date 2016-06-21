package logistic.compare.comparelogistic.Load;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import logistic.compare.comparelogistic.Adapter.PlaceAPI;
import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.R;

/**
 * Created by Sony on 4/19/2016.
 */
public class LoadAdapter extends RecyclerView.Adapter<LoadAdapter.MyViewHolder> {
    Context mContext;
    public ArrayList<String> loadlist;
    LayoutInflater inflater;
    CommonInterface commonInterface;
    public PlaceAPI mPlaceAPI = new PlaceAPI();

    public CommonInterface getCommonInterface() {
        return commonInterface;
    }

    public void setCommonInterface(CommonInterface commonInterface) {
        this.commonInterface = commonInterface;
    }

    public LoadAdapter(Context mContext, ArrayList<String> loadlist) {
        this.mContext = mContext;
        this.loadlist = loadlist;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(inflater.inflate(R.layout.autocompletetext, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String load = this.loadlist.get(position);
        holder.text.setText(load);
        holder.rootView.setTag(load);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundResource(R.drawable.citybackground2);
                sendBacktoActivity(v.getTag().toString());
            }
        });
    }

    public void sendBacktoActivity(String text) {
        this.commonInterface.ReceiveEnquiry(text);
    }

    @Override
    public int getItemCount() {
        return this.loadlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView text;
        View rootView;

        public MyViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            icon = (ImageView) itemView.findViewById(R.id.icon);
            text = (TextView) itemView.findViewById(R.id.autocompleteText);
        }
    }
}
