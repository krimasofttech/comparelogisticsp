package logistic.compare.comparelogistic.Truck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.MainActivity;
import logistic.compare.comparelogistic.R;

public class TruckActivity extends AppCompatActivity implements CommonInterface {

    TruckViewAdapter mAdapter;
    ArrayList<String> trucks;
    RecyclerView listview;
    LinearLayoutManager layoutmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck);
        listview = (RecyclerView) findViewById(R.id.trucklist);
        trucks = new ArrayList<>();
        String[] truck = getResources().getStringArray(R.array.truckarray);
        for (String s : truck) {
            trucks.add(s);
        }
        listview.setHasFixedSize(true);
        mAdapter = new TruckViewAdapter(this, trucks, this);
        listview.setAdapter(mAdapter);
        layoutmanager = new LinearLayoutManager(this);
        // layoutmanager.setMeasuredDimension(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        listview.setLayoutManager(layoutmanager);
        listview.setAdapter(mAdapter);
    }

    @Override
    public void RefreshEnquiry(String status, int maxid) {

    }

    @Override
    public void ReceiveEnquiry(Object enquiry) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("truck", enquiry.toString());
        setResult(3, i);
        finish();
    }
}
