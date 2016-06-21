package logistic.compare.comparelogistic.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;

import logistic.compare.comparelogistic.Database.EnquiryColumns;
import logistic.compare.comparelogistic.Enquiry;
import logistic.compare.comparelogistic.EnquiryDetailFragment;

/**
 * Created by Sony on 3/3/2016.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<EnquiryColumns> enquirylist;

    public void refreshAdapter(ArrayList<EnquiryColumns> enquirylist) {
        this.enquirylist = enquirylist;
        notifyDataSetChanged();
    }

    public boolean updateAdapter(ArrayList<EnquiryColumns> enquirylist) {
        this.enquirylist = enquirylist;
        notifyDataSetChanged();
        return true;
    }

    public ArrayList<EnquiryColumns> getEnquirylist() {
        return enquirylist;
    }

    public void setEnquirylist(ArrayList<EnquiryColumns> enquirylist) {
        this.enquirylist = enquirylist;
    }

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        EnquiryDetailFragment edf = new EnquiryDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        bundle.putSerializable("data", this.getEnquirylist().get(position));
        edf.setArguments(bundle);
        return edf;
    }

    @Override
    public int getCount() {
        return this.getEnquirylist().size();
    }
}
