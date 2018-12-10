package com.comp313_002.crimestalker.Classes;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comp313_002.crimestalker.R;

import java.util.List;

/*
 * @author Manoel B. Burgos
 * Creating a Customize adapter to be used in the list view
 */

public class CrimeList extends ArrayAdapter<Crime> {
    private Activity context;
    private List<Crime> crimeList;
    private LinearLayout llCrimeReportItem;

    public CrimeList(Activity context, List<Crime> crimeList) {
        super(context, R.layout.crime_layout, crimeList);
        this.context = context;
        this.crimeList = crimeList;
    }
    // taking the view objects from xml to send them to list view
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

        return listViewItem;
    }
}

