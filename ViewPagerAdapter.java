package logistic.compare.comparelogistic;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.ArrayList;

import logistic.compare.comparelogistic.Database.EnquiryColumns;

/**
 * Created by Sony on 11/3/2015.
 */
public class ViewPagerAdapter extends PagerAdapter {

    ArrayList<EnquiryColumns> enquiry;
    Context context;

    public ViewPagerAdapter(Context context, ArrayList<EnquiryColumns> data) {
        this.context = context;
        this.enquiry = data;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
}
