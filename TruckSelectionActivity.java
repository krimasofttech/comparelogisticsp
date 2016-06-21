package logistic.compare.comparelogistic;

import android.content.res.AssetManager;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.File;
import java.util.ArrayList;

import logistic.compare.comparelogistic.Adapter.TruckPagerAdapter;
import logistic.compare.comparelogistic.Truck.ZoomOutPageTransformer;

public class TruckSelectionActivity extends AppCompatActivity implements TruckFragment.OnFragmentInteractionListener {
    ViewPager mViewPager;
    TruckPagerAdapter mAdapter;
    ArrayList<String> filelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_truck_selection);
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisc(true).cacheInMemory(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .displayer(new CircleBitmapDisplayer(1000)).build();

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                    getApplicationContext())
                    .defaultDisplayImageOptions(defaultOptions)
                    .memoryCache(new WeakMemoryCache())
                    .discCacheSize(100 * 1024 * 1024).build();

            ImageLoader.getInstance().init(config);
            mViewPager = (ViewPager) findViewById(R.id.viewpager);
            //mViewPager.setPageTransformer(Page);
            mAdapter = new TruckPagerAdapter(getSupportFragmentManager());
            AssetManager assetManager = getAssets();
            filelist = new ArrayList<>();
            String[] files = assetManager.list("truck");
            for (String s : files) {
                Log.d("Path", "file:///android_asset/truck" + s);
                if (s.contains("."))
                    filelist.add(s);
            }
            mAdapter.setFiles(filelist);
            mAdapter.notifyDataSetChanged();
            mViewPager.setAdapter(mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
