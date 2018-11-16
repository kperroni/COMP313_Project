package com.comp313_002.crimestalker.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.comp313_002.crimestalker.Classes.Crime;
import com.comp313_002.crimestalker.Classes.CrimeHistoryClient;
import com.comp313_002.crimestalker.R;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;
import java.util.List;


public class CrimeHistoryActivity extends AppCompatActivity {
    private ListView listViewCrime ;
    private List<Crime> crimeList;
    private CrimeHistoryClient histClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_crime);

        listViewCrime = (ListView)findViewById(R.id.listViewCrime);
        crimeList = new ArrayList<>();

        histClient = CrimeHistoryClient.getInstance();
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Toast.makeText(getApplicationContext(), response.getJSONArray("features").toString(),Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        histClient.getData(handler);

    }
}
