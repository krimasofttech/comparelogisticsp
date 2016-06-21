package logistic.compare.comparelogistic.Quoted;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import logistic.compare.comparelogistic.Database.EnquiryColumns;
 
public class QuotedViewPagerAdapter extends FragmentStatePagerAdapter {

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

    public QuotedViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        QuotedFragmentDetail qf = new QuotedFragmentDetail();
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        bundle.putSerializable("data", this.getEnquirylist().get(position));
        qf.setArguments(bundle);
        return qf;
    }

    @Override
    public int getCount() {
        return this.getEnquirylist().size();
    }
}
