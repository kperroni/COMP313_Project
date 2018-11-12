package com.comp313_002.crimestalker.Classes;

import android.app.Activity;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comp313_002.crimestalker.R;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

public class CrimeList extends ArrayAdapter<Crime> {
    private Activity context;
    private List<Crime> crimeList;
    private LinearLayout llCrimeReportItem;

    public CrimeList(Activity context, List<Crime> crimeList) {
        super(context, R.layout.crime_layout, crimeList);
        this.context = context;
        this.crimeList = crimeList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.crime_layout, null, true);

        TextView textTitle = (TextView) listViewItem.findViewById(R.id.textViewTitle);
        TextView textDesc = (TextView) listViewItem.findViewById(R.id.textViewDesc);
        TextView textLatitude = (TextView) listViewItem.findViewById(R.id.textViewLatitude);
        TextView textLongitude = (TextView) listViewItem.findViewById(R.id.textViewLongitude);
        llCrimeReportItem = (LinearLayout) listViewItem.findViewById(R.id.ll_crime_report_item);
        TextView textkey= (TextView)listViewItem.findViewById(R.id.textViewkey);
        TextView address = (TextView)listViewItem.findViewById(R.id.textViewAddress);

        Crime crime = crimeList.get(position);
        final String reportTitle = crime.getTitle();
        final String reportDescription = crime.getDescription();
        final String reportLocLat = Float.toString(crime.getLatitude());
        final String reportLocLong = Float.toString(crime.getLongitude());
        textTitle.setText(reportTitle);
        textDesc.setText(reportDescription);
        textLatitude.setText(reportLocLat);
        textLongitude.setText(reportLocLong);
        textkey.setText(crime.getKey());
        address.setText(crime.getAddress());
        llCrimeReportItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TweetComposer.Builder builder = new TweetComposer.Builder(context).text(
                        context.getString(R.string.report_title) + " " + reportTitle + " " +
                                context.getString(R.string.report_desc) + " " + reportDescription + " " +
                                context.getString(R.string.report_loc_lat) + " " + reportLocLat + " " +
                                context.getString(R.string.report_loc_long) + " " + reportLocLong
                );
                builder.show();
            }
        });

        return listViewItem;
    }
}

