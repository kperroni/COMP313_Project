package com.comp313_002.crimestalker.Classes;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.comp313_002.crimestalker.R;

import java.util.List;

public class CrimeList extends ArrayAdapter<Crime> {
    private Activity context;
    private List<Crime> crimeList;

    public CrimeList(Activity context,List<Crime> crimeList){
        super(context, R.layout.crime_layout,crimeList);
        this.context = context;
        this.crimeList = crimeList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.crime_layout,null,true);

        TextView textTitle = (TextView)listViewItem.findViewById(R.id.textViewTitle);
        TextView textDesc = (TextView)listViewItem.findViewById(R.id.textViewDesc);
        TextView textLatitude = (TextView)listViewItem.findViewById(R.id.textViewLatitude);
        TextView textLongitude = (TextView)listViewItem.findViewById(R.id.textViewLongitude);

        Crime crime = crimeList.get(position);

        textTitle.setText(crime.getTitle());
        textDesc.setText(crime.getDescription());
        textLatitude.setText(Float.toString(crime.getLatitude()));
        textLongitude.setText(Float.toString(crime.getLongitude()));


        return listViewItem;
    }
}

