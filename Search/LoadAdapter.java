package logistic.compare.comparelogistic.Search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.R;

/**
 * Created by Sony on 5/28/2016.
 */
public class LoadAdapter extends RecyclerView.Adapter<LoadAdapter.MyHolder> {


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

    public LoadAdapter(ArrayList<String> cityList, CommonInterface commonInterface) {
        this.cityList = cityList;
        this.commonInterface = commonInterface;
    }

    public LoadAdapter() {

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
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder holder = new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.truckview, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
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
        return this.getCityList().size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView truckText;

        public MyHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            truckText = (TextView) itemView.findViewById(R.id.trucktext);
        }
    }
}
