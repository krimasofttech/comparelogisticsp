package logistic.compare.comparelogistic.Profile.load;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import logistic.compare.comparelogistic.Load.PostLoadBean;
import logistic.compare.comparelogistic.Load.ViewloadFragment;

/**
 * Created by Sony on 6/13/2016.
 */
public class MyViewPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<PostLoadBean> loadList;

    public ArrayList<PostLoadBean> getLoadList() {
        return loadList;
    }

    public void setLoadList(ArrayList<PostLoadBean> loadList) {
        this.loadList = loadList;
    }

    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        MyViewLoadFragment vtf = new MyViewLoadFragment();
        Bundle bund = new Bundle();
        bund.putSerializable("truck", this.loadList.get(position));
        vtf.setArguments(bund);
        return vtf;
    }

    @Override
    public int getCount() {
        return this.loadList.size();
    }
}
