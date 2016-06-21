package logistic.compare.comparelogistic.Truck;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.R;

/**
 * Created by Sony on 4/14/2016.
 */
public class TruckViewAdapter extends RecyclerView.Adapter<TruckViewAdapter.TruckHolder> {
    public ArrayList<String> trucklist;
    public Context context;
    CommonInterface commonInterface;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<String> getTrucklist() {
        return trucklist;
    }

    public void setTrucklist(ArrayList<String> trucklist) {
        this.trucklist = trucklist;
    }

    public TruckViewAdapter(Context context, ArrayList<String> trucks, CommonInterface commonInterface) {
        this.context = context;
        this.trucklist = trucks;
        this.commonInterface = commonInterface;
    }

    @Override
    public TruckHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TruckHolder th = new TruckHolder(LayoutInflater.from(context).inflate(R.layout.truckview, parent, false));
        return th;
    }

    @Override
    public void onBindViewHolder(TruckHolder holder, int position) {
        holder.trucktext.setText(trucklist.get(position));//set data
        holder.rootView.setTag(position);//set position
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundResource(R.drawable.citybackground2);
                sendBackToActivity(trucklist.get(Integer.parseInt(v.getTag().toString())));
            }
        });
    }

    public void sendBackToActivity(String city) {
        this.commonInterface.ReceiveEnquiry(city);
    }

    @Override
    public int getItemCount() {
        return trucklist.size();
    }

    public class TruckHolder extends RecyclerView.ViewHolder {
        TextView trucktext;
        View rootView;

        public TruckHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            trucktext = (TextView) itemView.findViewById(R.id.trucktext);
        }
    }
}
