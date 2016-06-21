package logistic.compare.comparelogistic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedVignetteBitmapDisplayer;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionStateChange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.Database.User;
import logistic.compare.comparelogistic.Declined.DeclinedFragment;
import logistic.compare.comparelogistic.Live.LiveFragment;
import logistic.compare.comparelogistic.Load.FindLoad;
import logistic.compare.comparelogistic.Load.PostLoad;
import logistic.compare.comparelogistic.Load.PostLoadActivity;
import logistic.compare.comparelogistic.Lost.LostFragment;
import logistic.compare.comparelogistic.Profile.ProfileActivity;
import logistic.compare.comparelogistic.Quoted.QuotedFragment;
import logistic.compare.comparelogistic.Service.CreditService;
import logistic.compare.comparelogistic.Truck.*;
import logistic.compare.comparelogistic.Won.WonFragment;
import logistic.compare.comparelogistic.verification.OtpScreen;


public class MainActivity extends AppCompatActivity implements CommonInterface, CommonUtility, NavigationView.OnNavigationItemSelectedListener, FindLoad.FindLoadListener, PostLoad.OnPostFragmentInteractionListener, logistic.compare.comparelogistic.Truck.TruckFragment.OnTruckFragmentListener, PostTruck.OnFragmentInteractionListener, LiveFragment.OnLiveFragmentListener, QuotedFragment.OnQuotedFragmentListener, WonFragment.OnWonFragmentListener, LostFragment.OnLostFragmentListener, DeclinedFragment.OnDeclinedFragment {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    String[] mNavItems;
    TextView companyTitle, walletText;
    ImageView companyLogo;
    Bundle SaveIntstance;
    DB db;
    MyPageAdapter adapter;
    Toolbar toolbar;
    CreditService mService;
    public TabLayout tablayout;
    public static final String[] TITLES = {"Live Enquiry", "Quoted Enquiry", "Won Enquiry", "Lost Enquiry", "Declined Enquiry", "Find Truck", "Post Truck", "Post Load", "Find Load"};
    ViewPager viewPager;
    public static Pusher pusher;
    public static Channel channel;
    NavigationView navigationView;
    LinearLayout headerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Here This will  check that user is verified or not
        tablayout = (TabLayout) findViewById(R.id.tabs);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setSupportActionBar(toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        ImageLoader();
        db = new DB(this);
        addTabs();
        initHeader(navigationView.getHeaderView(0));
        this.SaveIntstance = savedInstanceState;
        getUser();//Load User
        initNavigationDrawer();// start Navigation Drawer.
        serviceBinder();
    }


    public void initHeader(View view) {
        walletText = (TextView) view.findViewById(R.id.wallettextvalue);
        headerLayout = (LinearLayout) view.findViewById(R.id.headerlayout);
        companyLogo = (ImageView) view.findViewById(R.id.companyLogo);
        companyTitle = (TextView) view.findViewById(R.id.companyTitle);
        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });
    }

    public void addTabs() {
        ArrayList<TabLayout.Tab> tablist = new ArrayList<>();
        for (int i = 0; i < TITLES.length; i++) {
            tablist.add(tablayout.newTab().setText(TITLES[i]));
        }
        adapter = new MyPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    public void setCurrentTab(int pos) {
        mDrawerLayout.closeDrawers();
        if (viewPager != null) {
            viewPager.setCurrentItem(pos);
        }
    }

    public int getPosition(String text) {
        int c_pos = 0;
        for (int i = 0; i < TITLES.length; i++) {
            if (TITLES[i].contains(text)) {
                c_pos = i;
            }
        }
        return c_pos;
    }

    public void serviceBinder() {
        Intent intent = new Intent(this, CreditService.class);
        boolean b = bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("ServiceConnectedHome", "true");
            CreditService.MyBinder binder = (CreditService.MyBinder) service;
            mService = binder.getService();
            getWallet();
            getLogo();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("ServiceDisconnected", "true");
        }
    };

    public void getWallet() {
        if (new ConnectionUtility(this).isConnectingToInternet()) {

            if (mService != null) {
                mService.setCommonInterface(this);
                mService.getCredit();
            }
        } else {
            if (walletText != null)
                walletText.setText("-");
        }
    }


    public void ImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
    }

    public void getLogo() {
        if (new ConnectionUtility(this).isConnectingToInternet()) {
            if (mService != null) {
                mService.setCommonInterface(this);
                mService.getLogo();
            }
        }
    }

    public void getUser() {
        try {
            String user; // JsonArray containing user data
            db.open();
            user = db.getUser();
            db.close();
            Log.d("user", user.toString());
            JSONArray array = new JSONArray(user);
            //Now Parse the user data into User Instance
            JSONObject object = array.getJSONObject(0);
            Log.d("objectTag", object.toString());
            //set data to User Object
            if (User.getInstance() != null) {
                User.getInstance().setID(object.getString(User.u.ID));
                User.getInstance().setGcmKey(object.getString(User.u.gcmKey));
                User.getInstance().setMobileno(object.getString(User.u.mobileno));
                User.getInstance().setEmail(object.getString(User.u.email));
                User.getInstance().setCity(object.getString(User.u.city));
                User.getInstance().setCREDIT(object.getString(User.u.getCREDIT()));
                User.getInstance().setIsOtpVerified(object.getString(User.u.isOtpVerified));
                User.getInstance().setArea(object.getString(User.u.area));
                User.getInstance().setCompanyname(object.getString(User.u.companyname));
                User.getInstance().setOwnername(object.getString(User.u.ownername));
                User.getInstance().setLandamark(object.getString(User.u.landamark));
                User.getInstance().setAMOUNT(object.getString(User.u.getAMOUNT()));
                User.getInstance().setALTERCONTACT(object.getString(User.u.getALTERCONTACT()));
                User.getInstance().setCountry(object.getString(User.u.country));
                User.getInstance().setState(object.getString(User.u.state));
                User.getInstance().setIsOtpSend(object.getString(User.u.isOtpSend));
                User.getInstance().setLOGOIMG(object.getString(User.u.getLOGOIMG()));
                User.getInstance().setPassword(object.getString(User.u.password));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void initNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavItems = getResources().getStringArray(R.array.mNavItems);
        // leftdrawer = find(R.id.left_drawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                getLogo();
                getWallet();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //setActionBar Property
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setIcon(R.drawable.launcher);
        // getSupportActionBar().setHomeAsUpIndicator(R.drawable.launcher);
        mDrawerToggle.syncState();
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.launcher);
        //load home screen of App
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void OnQuotedInteraction(String uri) {

    }

    @Override
    public void OnDeclinedFragmentInteraction(String uri) {

    }

    @Override
    public void onLostFragmentInteration(String uri) {

    }

    @Override
    public void onWonFragmentInteration(Uri uri) {

    }

    @Override
    public void onTruckInteraction(Object uri) {

    }

    @Override
    public void onPostInteraction(String uri) {

    }

    @Override
    public void OnFindInteraction(String uri) {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.liveEnquiry:
                setCurrentTab(getPosition("Live"));
                break;
            case R.id.quotedenquiry:
                setCurrentTab(getPosition("Quoted"));
                break;
            case R.id.wonenquiry:
                setCurrentTab(getPosition("Won"));
                break;
            case R.id.lostenquiry:
                setCurrentTab(getPosition("Lost"));
                break;
            case R.id.declinedEnquiry:
                setCurrentTab(getPosition("Declined"));
                break;
            case R.id.postload:
                startActivity(new Intent(this, PostLoadActivity.class));
                overridePendingTransition(0, R.anim.down_from_top);
                break;
            case R.id.posttruck:
                startActivity(new Intent(this, PostTruckActivity.class));
                overridePendingTransition(0, R.anim.down_from_top);
                break;
            case R.id.findload:
                setCurrentTab(getPosition("Find Load"));
                break;
            case R.id.findtruck:
                setCurrentTab(getPosition("Find Truck"));
                break;
            case R.id.share:
                share();
                break;
            case R.id.feedback:
                sendFeedBack();
                break;
        }
        return false;
    }

    public void sendFeedBack() {
        Intent Email = new Intent(Intent.ACTION_SEND);
        Email.setType("text/email");
        Email.putExtra(Intent.EXTRA_EMAIL, new String[]{"krimasofttech@gmail.com"});
        Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
        Email.putExtra(Intent.EXTRA_TEXT, "Dear ...," + "");
        startActivity(Intent.createChooser(Email, "Send Feedback:"));
    }

    public void share() {
        String textToShare = "Search Truck and Load with comparelogistic.in.Download this app and enjoy service" + " https://play.google.com/store/apps/details?id=logistic.compare.comparelogistic";
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public void RefreshEnquiry(String status, int maxid) {

    }

    @Override
    public void ReceiveEnquiry(Object enquiry) {
        Log.d("ReceivedInMain", enquiry.toString());

        String data = enquiry.toString();
        if (data.contains("[") && data.contains("]")) {
            if (data.contains("{") && data.contains("}")) {
                try {
                    JSONObject obj = (JSONObject) (new JSONArray(data)).get(0);
                    Iterator<String> iterator = obj.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next();

                        Log.d("KeyReceived", key);
                        if (key.contains("CREDIT")) {
                            String text = obj.getString(key);
                            updateCredit(text);
                        } else if (key.contains("LOGOIMG")) {
                            Log.d("LOGOIMG", "true");
                            String companyname = obj.getString("COMPANYNAME");
                            String text = obj.getString("LOGOIMG");
                            updateLogo(text, companyname);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateCredit(String credit) {
        if (walletText != null) {
            walletText.setText(credit);
        }
    }

    public void updateLogo(String logo, String companyname) {
        Log.d("LogoReceived", logo);
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(120))
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        Log.d("URLinif", "http://comparelogistic.in/public/uploads/" + logo);
        imageLoader.displayImage("http://comparelogistic.in/public/uploads/" + logo, companyLogo, options);
        companyTitle.setText(companyname);
    }
}