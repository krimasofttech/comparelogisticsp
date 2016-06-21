package logistic.compare.comparelogistic.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.R;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyHolder> {
    public ArrayList<String> city = new ArrayList<>();
    Context context;
    CommonInterface commonInterface;
    public PlaceAPI mPlaceAPI = new PlaceAPI();

    public ArrayList<String> getState() {
        return state;
    }

    public void setState(ArrayList<String> state) {
        this.state = state;
    }

    public ArrayList<String> state;

    public void setCity(ArrayList<String> city) {
        this.city = city;
    }

    public ArrayList<String> getCity() {
        return city;
    }

    public CityAdapter(ArrayList<String> city, Context context) {
        this.city = city;
        this.context = context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder holder = new MyHolder(LayoutInflater.from(this.context).inflate(R.layout.autocompletetext, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        String data = city.get(position).trim();
//        this.state.add(data.split(",")[1]);
        holder.cityText.setText(data.split(",")[0]);
        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundResource(R.drawable.citybackground2);
                commonInterface.ReceiveEnquiry(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return city.size();
    }

    public CommonInterface getCommonInterface() {
        return commonInterface;
    }

    public void setCommonInterface(CommonInterface commonInterface) {
        this.commonInterface = commonInterface;
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
