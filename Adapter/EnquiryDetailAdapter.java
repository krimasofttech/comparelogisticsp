package logistic.compare.comparelogistic.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONObject;

import java.util.Iterator;

import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.CommonUtility;
import logistic.compare.comparelogistic.R;

/**
 * Created by Sony on 2/29/2016.
 */
public class EnquiryDetailAdapter extends RecyclerView.Adapter<EnquiryDetailAdapter.ViewHolder> implements CommonUtility {
    Context context;
    public JSONObject jobj;
    Iterator keyset;
    String[] quoteArray;
    String[] yesnomap;
    String[] customer;
    CommonInterface commonInterface;
    String url = "http://comparelogistic.in/public/uploads/";
    public DisplayImageOptions options;
    TextView fileValue;
    boolean b = false;

    public CommonInterface getCommonInterface() {
        return commonInterface;
    }

    public void setCommonInterface(CommonInterface commonInterface) {
        this.commonInterface = commonInterface;
    }

    public EnquiryDetailAdapter(Context context, JSONObject obj) {
        this.context = context;
        this.jobj = obj;
        this.commonInterface = commonInterface;
        keyset = this.jobj.keys();
        customer = new String[]{"Mobile", "Email"};
        quoteArray = new String[]{PRICE, REMARK, VALIDITY, DAYS, ID, REASON, SPSTATUS, "U_DATE"};
        yesnomap = new String[]{"Yes", "No"};
    }

    public JSONObject getJobj() {
        return jobj;
    }

    public void setJobj(JSONObject jobj) {
        this.jobj = jobj;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder mHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.enquiry_item, parent, false));
        return mHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String data = getJsonData(position);
        setData(holder, data.split(":")[0], data.split(":")[1]);
    }

    // this is called from getView and getItem in this class
    // for getting key and value in string format like key:value
    public String getJsonData(int pos) {
        String dataToReturn = "false";
        if (jobj != null) {
            int i = 0;
            keyset = jobj.keys();
            while (keyset.hasNext()) {
                //   Log.d("pos==i", "pos" + pos + "i" + i);
                String key = keyset.next().toString();
                if (pos == i) {
                    try {
                        dataToReturn = key + ":" + jobj.getString(key);
                        //  Log.d("dataToReturn", dataToReturn);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                i++;

            }
        }
        // Log.d("data", dataToReturn);
        return dataToReturn;
    }


    @Override
    public int getItemCount() {
        return this.jobj.length();
    }

    public void setData(ViewHolder holder, String key, String value) {
        // Log.d("key/value", "key/value" + key + ":" + value);
        holder.type.setText(key);
        holder.value.setTag(key + ":" + value);
        holder.value.setText(value);
    }


    public void setCall(String enquiry) {
        commonInterface.ReceiveEnquiry(enquiry);
    }

    //parse 1 and 2 value into yes and no
    public String yesnomapper(String value) {
        try {
            value = yesnomap[Integer.parseInt(value) - 1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    //we are not going to show a customer detail and quote detail in live enquiry
    public boolean validateData(String key) {
        boolean b = true;
        for (String s : quoteArray) {
            if (key.equalsIgnoreCase(s)) {
                b = false;
                break;
            }
        }
        return b;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView type, value;

        public ViewHolder(View convertView) {
            super(convertView);
            type = (TextView) convertView.findViewById(R.id.type);
            value = (TextView) convertView.findViewById(R.id.value);
            value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    filterEvent(v.getTag().toString());
                }
            });
        }
    }

    public void filterEvent(String keyValue) {
        String key = keyValue.split(":")[0];
        String value = keyValue.split(":")[1];
        if (key.equals("File")) {
            setCall(value);
        }
    }
}
