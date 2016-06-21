package logistic.compare.comparelogistic.Profile.truck;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import logistic.compare.comparelogistic.Truck.PostTruckBean;

/**
 * Created by Sony on 6/12/2016.
 */
public class MyTruckAdapter extends FragmentStatePagerAdapter {
    public ArrayList<PostTruckBean> trucklist;

    public ArrayList<PostTruckBean> getTrucklist() {
        return trucklist;
    }

    public void setTrucklist(ArrayList<PostTruckBean> trucklist) {
        this.trucklist = trucklist;
    }

    public MyTruckAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        MyViewTruckFragment vtf = new MyViewTruckFragment();
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
