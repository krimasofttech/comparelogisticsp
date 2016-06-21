package logistic.compare.comparelogistic.Profile;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.lang.reflect.Field;

import logistic.compare.comparelogistic.CityActivity;
import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.CommonUtility;
import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.Database.User;
import logistic.compare.comparelogistic.R;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, CommonUtility, CommonInterface {
    EditText ownername, companyname, email, street, companyarea, companylandmark;
    TextView state;
    EditText pincode;
    EditText phoneno;
    EditText mobile;
    ImageView ownernameediticon;
    TextView city, mytruckpostvalue, myloadpostvalue;
    ImageView companynameediticon;
    TextView walletText;
    View background;
    LinearLayout truckpostparent, loadpostparent;
    ProgressBar loader;
    ImageView streetediticon, emailediticon;
    ImageView companyareaediticon;
    ImageView companylandmarkediticon;
    ImageView cityediticon;
    TextView companyTitle;
    ImageView stateediticon;
    ImageView pincodeediticon;
    ImageView phonenoediticon;
    ImageView mobileediticon, companyLogo;
    CoordinatorLayout root;
    DB db;
    String existText = "";
    ImageView current;
    ProfileIntentService myService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ProfileIntentService.MyBinder binder = (ProfileIntentService.MyBinder) service;
            myService = binder.getService();
            getProfile();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void updateLogo(String logo) {
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
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String city = "", state = "";
        Log.d("requestCode", "" + requestCode);
        if (resultCode == 2) {
            if (data.hasExtra("city")) {
                city = data.getStringExtra("city");
            }
            if (data.hasExtra("state")) {
                state = data.getStringExtra("state");
            }
            setCityState(city, state);
        }
    }

    public void setCityState(String cit, String stat) {
        existText = city.getText().toString();
        if (!TextUtils.isEmpty(cit) && !TextUtils.isEmpty(stat)) {
            stat = stat.trim();
            cit = cit.trim();
            city.setText(cit);
            state.setText(stat);
            Log.d("DataOnSet", cit + stat);
            updateProfile("CITY", cit);
            updateProfile("STATE", stat);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.cross);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.up_from_bottom, R.anim.down_from_top);
            }
        });

        initComponent();
        ServiceBind();
        if (db == null) {
            db = new DB(this);
        }
    }

    public void ServiceBind() {
        Intent intent = new Intent(this, ProfileIntentService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }


    public void initComponent() {
        ownername = (EditText) findViewById(R.id.ownername);
        companyname = (EditText) findViewById(R.id.companyname);
        street = (EditText) findViewById(R.id.street);
        mytruckpostvalue = (TextView) findViewById(R.id.mytruckpostvalue);
        myloadpostvalue = (TextView) findViewById(R.id.myloadpostvalue);
        mytruckpostvalue.setOnClickListener(this);
        myloadpostvalue.setOnClickListener(this);
        companyarea = (EditText) findViewById(R.id.companyarea);
        companylandmark = (EditText) findViewById(R.id.companylandmark);
        city = (TextView) findViewById(R.id.city);
        state = (TextView) findViewById(R.id.state);
        truckpostparent = (LinearLayout) findViewById(R.id.mytruckpostparent);
        loadpostparent = (LinearLayout) findViewById(R.id.myloadpostparent);
        pincode = (EditText) findViewById(R.id.pincode);
        companyLogo = (ImageView) findViewById(R.id.companyLogo);
        companyTitle = (TextView) findViewById(R.id.companyTitle);
        phoneno = (EditText) findViewById(R.id.phoneno);
        mobile = (EditText) findViewById(R.id.mobile);
        root = (CoordinatorLayout) findViewById(R.id.root);
        walletText = (TextView) findViewById(R.id.wallettextvalue);
        ownernameediticon = (ImageView) findViewById(R.id.ownerediticon);
        ownernameediticon.setOnClickListener(this);
        ownername.setOnFocusChangeListener(this);
        background = findViewById(R.id.backgroundview);
        loader = (ProgressBar) findViewById(R.id.loader);
        companynameediticon = (ImageView) findViewById(R.id.companynameicon);
        companynameediticon.setOnClickListener(this);
        companyname.setOnFocusChangeListener(this);
        truckpostparent.setOnClickListener(this);
        loadpostparent.setOnClickListener(this);
        streetediticon = (ImageView) findViewById(R.id.streetediticon);
        streetediticon.setOnClickListener(this);
        street.setOnFocusChangeListener(this);
        email = (EditText) findViewById(R.id.email);
        emailediticon = (ImageView) findViewById(R.id.emailediticon);
        companyareaediticon = (ImageView) findViewById(R.id.companyareaediticon);
        companyareaediticon.setOnClickListener(this);
        companyarea.setOnFocusChangeListener(this);
        companylandmarkediticon = (ImageView) findViewById(R.id.companylandmarkediticon);
        companylandmarkediticon.setOnClickListener(this);
        companylandmark.setOnFocusChangeListener(this);
        cityediticon = (ImageView) findViewById(R.id.cityediticon);
        cityediticon.setOnClickListener(this);
        city.setOnFocusChangeListener(this);
        // stateediticon = (ImageView) findViewById(R.id.stateediticon);
//        stateediticon.setOnClickListener(this);
        state.setOnFocusChangeListener(this);
        pincodeediticon = (ImageView) findViewById(R.id.pincodeediticon);
        pincodeediticon.setOnClickListener(this);
        pincode.setOnFocusChangeListener(this);
        phonenoediticon = (ImageView) findViewById(R.id.phonenoediticon);
        phonenoediticon.setOnClickListener(this);
        phoneno.setOnFocusChangeListener(this);
        mobileediticon = (ImageView) findViewById(R.id.mobileediticon);
        mobileediticon.setOnClickListener(this);
        mobile.setOnFocusChangeListener(this);
    }

    public void setDataOnView(User user) {
        ownername.setText(user.getOwnername());
        companyname.setText(user.getCompanyname());
        street.setText(user.getStreet());
        companyarea.setText(user.getArea());
        companylandmark.setText(user.getLandamark());
        state.setText(user.getState());
        city.setText(user.getCity());
        pincode.setText(user.getPincode());
        phoneno.setText(user.getPhone());
        mobile.setText(user.getMobileno());
        companyTitle.setText(user.getCompanyname());
        email.setText(user.getEmail());
        walletText.setText(user.getCREDIT());
        myloadpostvalue.setText(user.getLoadPostCount());
        mytruckpostvalue.setText(user.getTruckPostCount());
        updateLogo(user.getLOGOIMG());
    }

    public void getProfile() {
        myService.setCommonInterface(this);
        if (myService != null) {
            myService.setUrl(getprofile);
            changeLoader(View.VISIBLE);
            myService.getProfile();
        }
    }

    public void updateProfile(String column, String value) {
        if (validate(existText, value)) {
            myService.setCommonInterface(this);
            if (myService != null) {
                myService.setUrl(updateProfile);
                changeLoader(View.VISIBLE);
                myService.updateProfile(column, value);
            }
        }
    }

    public void changeLoader(int visibility) {
        background.setVisibility(visibility);
        loader.setVisibility(visibility);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        // changeImageIcon((ImageView) v, android.R.drawable.ic_menu_save);
        switch (id) {
            case R.id.ownerediticon:
                enableEditBox(ownername, "OWNERNAME", (ImageView) v);
                User.getInstance().setOwnername(ownername.getText().toString());
                break;
            case R.id.companyareaediticon:
                enableEditBox(companyarea, "COMPANYAREA", (ImageView) v);
                User.getInstance().setArea(companyarea.getText().toString());
                break;
            case R.id.streetediticon:
                enableEditBox(street, "STREET", (ImageView) v);
                User.getInstance().setStreet(street.getText().toString());
                break;
            case R.id.companynameicon:
                enableEditBox(companyname, "COMPANYNAME", (ImageView) v);
                User.getInstance().setCompanyname(companyname.getText().toString());
                break;
            case R.id.companylandmarkediticon:
                enableEditBox(companylandmark, "COMPANYLANDMARK", (ImageView) v);
                User.getInstance().setLandamark(companylandmark.getText().toString());
                break;
            case R.id.cityediticon:
                startCityActivity(); // start city activity
                User.getInstance().setCity(city.getText().toString());
                break;
            case R.id.pincodeediticon:
                enableEditBox(pincode, "PINCODE", (ImageView) v);
                User.getInstance().setPincode(pincode.getText().toString());
                break;
            case R.id.phonenoediticon:
                enableEditBox(phoneno, "PHONENO", (ImageView) v);
                User.getInstance().setPhone(phoneno.getText().toString());
                break;
            case R.id.mobileediticon:
                enableEditBox(mobile, "MOBILE", (ImageView) v);
                User.getInstance().setMobileno(mobile.getText().toString());
                break;
            case R.id.emailediticon:
                enableEditBox(email, "HOEMAIL", (ImageView) v);
                User.getInstance().setEmail(email.getText().toString());
                break;
            case R.id.myloadpostparent:
                startMyLoadFragment();
                break;
            case R.id.mytruckpostparent:
                startMyTruckFragment();
                break;
        }
    }

    public void startMyTruckFragment() {
        Intent intent = new Intent(this, MyPostActivity.class);
        intent.putExtra("truck", "truck");
        startActivity(intent);
        overridePendingTransition(R.anim.up_from_bottom, R.anim.down_from_top);
    }

    //this will start MyPostActivity
    public void startMyLoadFragment() {
        Intent intent = new Intent(this, MyPostActivity.class);
        intent.putExtra("load", "load");
        startActivity(intent);
        overridePendingTransition(R.anim.up_from_bottom, R.anim.down_from_top);
    }

    public void startCityActivity() {
        Intent i = new Intent(this, CityActivity.class);
        startActivityForResult(i, 2);
    }

    public void enableEditBox(EditText editText, String column, ImageView iv) {
        if (current != null) {
            current.setImageResource(android.R.drawable.ic_menu_edit);
        }
        if (!editText.isEnabled()) {
            editText.setEnabled(true);
            editText.requestFocus();
            iv.setImageResource(android.R.drawable.ic_menu_save);
            existText = editText.getText().toString();
            current = iv;
        } else {
            updateProfile(column, editText.getText().toString());
            disableEditBox(editText);
            iv.setImageResource(android.R.drawable.ic_menu_edit);

        }
    }

    public boolean validate(String text, String updateText) {
        if (text.equals(updateText)) {
            Snackbar.make(root, "oops you have not changed anything", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void disableEditBox(EditText editText) {
        editText.setEnabled(false);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus)
            disableEditBox((EditText) v);
        else if (hasFocus) {

        }
    }

    @Override
    public void RefreshEnquiry(String status, int maxid) {

    }

    @Override
    public void ReceiveEnquiry(Object enquiry) {
        changeLoader(View.INVISIBLE);//hide loader
        if (enquiry.toString().contains("success")) {
            Snackbar.make(root, "Your profile Updated Successfully", Snackbar.LENGTH_SHORT).show();
        } else if (enquiry.toString().contains("failure")) {
            Snackbar.make(root, "Something went wrong ,try after some time", Snackbar.LENGTH_SHORT).show();
        } else if (enquiry.toString().contains("OWNERNAME")) {
            Log.d("ProfileReceived", enquiry.toString());
            createUser(enquiry.toString());
        }
    }

    // update to local database
    public void createUser(String data) {
        if (myService != null) {
            User user = myService.getUser(data);
            user.isOtpVerified = "true";
            user.isOtpSend = "true";
            db.open();
            db.UpdateUser(user);
            db.close();
            setDataOnView(user);
        }
    }

}
