package logistic.compare.comparelogistic.Declined;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import logistic.compare.comparelogistic.Database.EnquiryColumns;
import logistic.compare.comparelogistic.Lost.LostFragmentDetail;

/**
 * Created by Sony on 3/22/2016.
 */
public class DeclinedViewPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<EnquiryColumns> enquirylist;

    public void refreshAdapter(ArrayList<EnquiryColumns> enquirylist) {
        this.enquirylist = enquirylist;
        notifyDataSetChanged();
    }

    public ArrayList<EnquiryColumns> getEnquirylist() {
        return enquirylist;
    }

    public void setEnquirylist(ArrayList<EnquiryColumns> enquirylist) {
        this.enquirylist = enquirylist;
    }


    public DeclinedViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        DeclinedFragmentDetail qf = new DeclinedFragmentDetail();
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        bundle.putSerializable("data", this.getEnquirylist().get(position));
        qf.setArguments(bundle);
        return qf;
    }

    @Override
    public int getCount() {
        return this.enquirylist.size();
    }
}
