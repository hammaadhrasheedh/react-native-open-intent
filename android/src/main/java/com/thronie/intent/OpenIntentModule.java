package com.thronie.intent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class OpenIntentModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private Promise intentPromise;

    public OpenIntentModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        reactContext.addActivityEventListener(mActivityEventListener);
    }

    @Override
    public String getName() {
        return "OpenIntent";
    }

    @ReactMethod
    public void openLink(String url, final Promise promise) {
        // TODO: Implement some actually useful functionality
        intentPromise = promise;
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        try {
            this.reactContext.startActivityForResult(intent, 5864, null);
        } catch (Exception e){
            intentPromise.reject("Failed", "NO APP FOUND");
        }
    }

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
        //listener for activity
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            if (intentPromise != null) {
                if (data != null) {
                    String res = data.getStringExtra("response");
                    intentPromise.resolve(res);
                } else {
                    intentPromise.reject("Failed", "");
                }
            }
        }
    };
}
