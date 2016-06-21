package logistic.compare.comparelogistic.Search;

import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import logistic.compare.comparelogistic.Adapter.PlaceAPI;
import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.ConnectionUtility;
import logistic.compare.comparelogistic.R;

/**
 * Created by Sony on 5/19/2016.
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyHolder> {

    ArrayList<String> cityList;
    CommonInterface commonInterface;
    PlaceAPI placeAPI = new PlaceAPI();

    public CommonInterface getCommonInterface() {
        return commonInterface;
    }

    public void setCommonInterface(CommonInterface commonInterface) {
        this.commonInterface = commonInterface;
    }

    public CityAdapter(CommonInterface commonInterface, ArrayList<String> cityList) {
        this.commonInterface = commonInterface;
        this.cityList = cityList;
    }

    public CityAdapter() {

    }

    public ArrayList<String> getCityList() {
        return cityList;
    }

    public void setCityList(ArrayList<String> cityList) {
        this.cityList = cityList;
    }

    public CityAdapter(ArrayList<String> cityList) {
        this.cityList = cityList;
    }

    @Override
    public CityAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder holder = new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.autocompletetext, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CityAdapter.MyHolder holder, int position) {
        holder.cityText.setText(this.getCityList().get(position).split(",")[0]);
        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundResource(R.drawable.citybackground2);
                sendData(v.getTag().toString());
            }
        });
    }

    public void sendData(String data) {
        if (commonInterface != null) {
            commonInterface.ReceiveEnquiry(data);
        }
    }

    @Override
    public int getItemCount() {
        return this.cityList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView cityText;

        public MyHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            cityText = (TextView) itemView.findViewById(R.id.autocompleteText);
        }
    }
}
