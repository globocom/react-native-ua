package com.globo.reactnativeua;

import android.util.Log;

import com.urbanairship.Autopilot;
import com.urbanairship.UAirship;

public class ReactNativeUAAutoPilot extends Autopilot {

    private static final String TAG = "ReactNativeUAAutoPilot";

    @Override
    public void onAirshipReady(UAirship airship) {
        Log.w(TAG, "Airship ready!");
    }

}
