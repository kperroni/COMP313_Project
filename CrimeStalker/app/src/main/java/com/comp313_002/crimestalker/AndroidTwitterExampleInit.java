package com.comp313_002.crimestalker;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.comp313_002.crimestalker.Activities.MainActivity;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

public class AndroidTwitterExampleInit extends Activity {
    private static final String TWITTER_KEY = "8UlasGb3G5bFxrzbFDsidD31V";
    private static final String TWITTER_SECRET = "RvozGyJ8KBS1vfz0gf7bvyUOgx9gnP5TBKR7cIgnk7wwJaaCsB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        startLoginActivity();
    }

    private void startLoginActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }
}