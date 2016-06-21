package logistic.compare.comparelogistic.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import java.util.ArrayList;

import logistic.compare.comparelogistic.R;

/**
 * Created by Sony on 4/3/2016.
 */
public class PlaceAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    public ArrayList<String> resultList = new ArrayList<>();
    Context mContext;
    int mResource;

    public PlaceAPI mPlaceAPI = new PlaceAPI();

    public PlaceAutoCompleteAdapter(Context context, int resource) {
        super(context, resource);
        mContext = context;
        mResource = resource;
    }

    @Override
    public int getCount() {
        // Last item will be the footer
        return resultList.size();
    }

    @Override
    public String getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        try {
            //if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (position != (resultList.size() - 1))
                view = inflater.inflate(R.layout.autocompletetext, null);
            else
                view = inflater.inflate(R.layout.autocompletetext, null);

            if (position != (resultList.size() - 1)) {
                TextView autocompleteTextView = (TextView) view.findViewById(R.id.autocompleteText);
                autocompleteTextView.setText(resultList.get(position));
            } else {
                ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
                // not sure what to do <img draggable="false" class="emoji" alt="ðŸ˜€" src="http://s.w.org/images/core/emoji/72x72/1f600.png">

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    resultList = mPlaceAPI.autocomplete(constraint.toString());

                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };

        return filter;
    }
}
