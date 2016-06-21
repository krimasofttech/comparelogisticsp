package logistic.compare.comparelogistic.Load;

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
public class ViewLoadPageAdapter extends FragmentStatePagerAdapter {
    public ArrayList<PostLoadBean> trucklist;

    public ArrayList<PostLoadBean> getTrucklist() {
        return trucklist;
    }

    public void setTrucklist(ArrayList<PostLoadBean> trucklist) {
        this.trucklist = trucklist;
    }

    public ViewLoadPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        ViewloadFragment vtf = new ViewloadFragment();
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
