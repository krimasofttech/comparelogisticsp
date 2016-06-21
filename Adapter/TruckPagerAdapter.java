package logistic.compare.comparelogistic.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;

import logistic.compare.comparelogistic.TruckFragment;

/**
 * Created by Sony on 4/8/2016.
 */
public class TruckPagerAdapter extends FragmentStatePagerAdapter {
    public ArrayList<String> files = new ArrayList<>();

    public ArrayList<String> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }

    public TruckPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        TruckFragment tf = new TruckFragment();
        Bundle bund = new Bundle();
        bund.putString("file", files.get(position));
        tf.setArguments(bund);
        return tf;
    }

    @Override
    public int getCount() {
        Log.d("TruckCounting", this.files.size() + "");
        return this.files.size();
    }
}
