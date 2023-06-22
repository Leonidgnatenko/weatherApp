package com.weather.sunnymonkey;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustAttribution;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.LogLevel;
import com.adjust.sdk.OnAttributionChangedListener;
import com.adjust.sdk.OnDeviceIdsRead;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Otribucia extends Application {
    String gaid;

    @Override
    public void onCreate() {
        super.onCreate();

        // Configure adjust SDK.
        String appToken = "1ncr6lrzjio0";//Todo вставить ключ adjust
        String environment = AdjustConfig.ENVIRONMENT_PRODUCTION;

        AdjustConfig config = new AdjustConfig(this, appToken, environment);

        // Change the log level.
        config.setLogLevel(LogLevel.VERBOSE);

        Adjust.setEnabled(true);

        Adjust.onCreate(config);
        config.setOnAttributionChangedListener(new OnAttributionChangedListener() {
            @Override
            public void onAttributionChanged(AdjustAttribution attribution) {
                JSONObject json = new JSONObject();
                try {
                    json.put("trackerToken", attribution.trackerToken);
                    json.put("trackerName", attribution.trackerName);
                    json.put("adgroup", attribution.adgroup);
                    json.put("network", attribution.network);
                    json.put("creative", attribution.creative);
                    json.put("campaign", attribution.campaign);
                    json.put("clickLabel", attribution.clickLabel);
                    json.put("adid", attribution.adid);
                    json.put("costCurrency", attribution.costCurrency);
                    json.put("costType", attribution.costType);
                    json.put("costAmount", attribution.costAmount);
                    json.put("gaid", gaid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("info",attribution.toString());
                write_key1(json.toString());
            }
        });

        Adjust.getGoogleAdId(this, new OnDeviceIdsRead() {
            @Override
            public void onGoogleAdIdRead(String googleAdId) {
                gaid = googleAdId;
            }
        });

        config.setSendInBackground(true);

        registerActivityLifecycleCallbacks(new AdjustLifecycleCallbacks());

    }

    // You can use this class if your app is for Android 4.0 or higher
    private static final class AdjustLifecycleCallbacks implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityResumed(Activity activity) {
            Adjust.onResume();
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Adjust.onPause();
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }
    }
    public void write_key1(String s) {
        SharedPreferences myPrefs = getSharedPreferences("file", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("key1", s);
        editor.apply();
    }
}