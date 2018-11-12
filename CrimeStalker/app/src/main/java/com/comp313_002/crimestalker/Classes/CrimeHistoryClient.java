package com.comp313_002.crimestalker.Classes;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class CrimeHistoryClient{
    private static final CrimeHistoryClient ourInstance = new CrimeHistoryClient();
    private final AsyncHttpClient client = new AsyncHttpClient();
    public static CrimeHistoryClient getInstance() {
        return ourInstance;
    }
    private final String BASE_URL = "https://services.arcgis.com/S9th0jAJ7bqgIRjw/arcgis/rest/services/MCI_2014_2017/FeatureServer/0/query?";
    private CrimeHistoryClient() {

    }

    public void getData(JsonHttpResponseHandler handler)
    {
        RequestParams params = new RequestParams();
        params.add("where","1=1");
        params.add("f","json");
        params.add("outFields","occurrenceyear,MCI,Lat,Long");
        client.get(BASE_URL, params, handler);
    }
}
