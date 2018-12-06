package com.comp313_002.crimestalker.Classes;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
* @author Kenneth Bato
* Singleton class use to make api calls to http://data.torontopolice.on.ca/datasets/mci-2014-to-2017
* to retrieve Crime Report History data in the city of Toronto.
*
* Uses the AsyncHttpClient from https://github.com/loopj/android-async-http
*/
public class CrimeHistoryClient{
    //Create a single instance of CrimeHistoryClient
    private static final CrimeHistoryClient ourInstance = new CrimeHistoryClient();
    //Initialize a client
    private final AsyncHttpClient client = new AsyncHttpClient();
    /*
     * Getter method for the singleton instance
     * @return retrieve the CrimeHistoryClient instance
     * */
    public static CrimeHistoryClient getInstance() {
        return ourInstance;
    }
    //Public field to store RequestParams object that hold the Http requests for the URI
    public static RequestParams params;
    //Base URI for the REST Api
    private final String BASE_URL = "https://services.arcgis.com/S9th0jAJ7bqgIRjw/arcgis/rest/services/MCI_2014_2017/FeatureServer/0/query?";

    //Constructor that sets default request parameters
    private CrimeHistoryClient() {
        params = new RequestParams();
        params.add("where","1=1");
        params.add("outFields","occurrenceyear,occurrencemonth,occurrenceday,MCI,Lat,Long");
        params.add("f","json");
    }

    /*
    * Method to call to send request to the Api
    * @param handler JsonHttpResponseHandler to handle the response
    * */
    public void getData(JsonHttpResponseHandler handler)
    {
        client.get(BASE_URL, params, handler);
    }
}
