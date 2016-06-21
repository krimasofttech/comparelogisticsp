package logistic.compare.comparelogistic.Search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import logistic.compare.comparelogistic.Adapter.PlaceAPI;
import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.R;

/**
 * Created by Sony on 5/20/2016.
 */
public class TruckAdapter extends RecyclerView.Adapter<TruckAdapter.TruckHolder> {

    ArrayList<String> cityList;
    CommonInterface commonInterface;

    public ArrayList<String> getCityList() {
        return cityList;
    }

    public void setCityList(ArrayList<String> cityList) {
        this.cityList = cityList;
    }

    public CommonInterface getCommonInterface() {
        return commonInterface;
    }

    public void setCommonInterface(CommonInterface commonInterface) {
        this.commonInterface = commonInterface;
    }

    public TruckAdapter(ArrayList<String> cityList, CommonInterface commonInterface) {
        this.cityList = cityList;
        this.commonInterface = commonInterface;
    }

    public TruckAdapter() {
    }

    public ArrayList<String> search(String value) {
        ArrayList<String> myTruck = new ArrayList<>();
        for (String truck : this.getCityList()) {
            if (truck.contains(value) || truck.startsWith(value)) {
                myTruck.add(truck);
            }
        }
        return myTruck;
    }

    @Override
    public TruckHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TruckHolder holder = new TruckHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.truckview, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(TruckHolder holder, int position) {
        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData("pos:" + v.getTag());
            }
        });
        holder.truckText.setText(this.getCityList().get(position));
    }

    public void sendData(Object data) {
        if (commonInterface != null) {
            commonInterface.ReceiveEnquiry(data);
        }
    }

    @Override
    public int getItemCount() {
        return this.cityList.size();
    }

    public class TruckHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView truckText;

        public TruckHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            truckText = (TextView) itemView.findViewById(R.id.trucktext);
        }
    }
}
