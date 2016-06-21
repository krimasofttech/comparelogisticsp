package logistic.compare.comparelogistic.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import logistic.compare.comparelogistic.Truck.PostTruckBean;
import logistic.compare.comparelogistic.Truck.ViewTruckFragment;

/**
 * Created by Sony on 4/20/2016.
 */
public class MyViewTruckAdapter extends FragmentStatePagerAdapter {
    public ArrayList<PostTruckBean> trucklist;

    public ArrayList<PostTruckBean> getTrucklist() {
        return trucklist;
    }

    public void setTrucklist(ArrayList<PostTruckBean> trucklist) {
        this.trucklist = trucklist;
    }

    public MyViewTruckAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        ViewTruckFragment vtf = new ViewTruckFragment();
        Bundle bund = new Bundle();
        bund.putSerializable("truck", this.trucklist.get(position));
        vtf.setArguments(bund);
        return vtf;
    }

    @Override
    public int getCount() {
        return this.trucklist.size();
    }
}
