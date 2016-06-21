package logistic.compare.comparelogistic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import logistic.compare.comparelogistic.Declined.DeclinedFragment;
import logistic.compare.comparelogistic.Live.LiveFragment;
import logistic.compare.comparelogistic.Load.FindLoad;
import logistic.compare.comparelogistic.Load.PostLoad;
import logistic.compare.comparelogistic.Lost.LostFragment;
import logistic.compare.comparelogistic.Quoted.QuotedFragment;
import logistic.compare.comparelogistic.Truck.*;
import logistic.compare.comparelogistic.Truck.TruckFragment;
import logistic.compare.comparelogistic.Won.WonFragment;

/**
 * Created by Sony on 9/11/2015.
 */
public class MyPageAdapter extends FragmentPagerAdapter {
    public static final String[] TITLES = {"Find Truck", "Find Load", "Live Enquiry", "Quoted Enquiry", "Won Enquiry", "Lost Enquiry", "Declined Enquiry"};

    public MyPageAdapter(FragmentManager fm) {
        super(fm);
    }


    public Fragment getFragment(int pos) {
        Fragment fragment = null;
        switch (pos) {
            case 0:
                fragment = new TruckFragment();
                break;
            case 1:
                fragment = new FindLoad();
                break;
            case 2:
                fragment = new LiveFragment();
                break;
            case 3:
                fragment = new QuotedFragment();
                break;
            case 4:
                fragment = new WonFragment();
                break;
            case 5:
                fragment = new LostFragment();
                break;
            case 6:
                fragment = new DeclinedFragment();
                break;
            case 7:
                fragment = new TruckFragment();
                break;
            case 8:
                fragment = new FindLoad();
                break;
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return getFragment(position);
    }


}
