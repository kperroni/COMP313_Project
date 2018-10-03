package com.comp313_002.crimestalker.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.comp313_002.crimestalker.R;

public class ReadCrime extends AppCompatActivity {

    var database = firebase.database();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_crime);
    }
}
