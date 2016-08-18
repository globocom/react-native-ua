package com.globo.reactnativeua;

import android.util.Log;

import com.urbanairship.Autopilot;
import com.urbanairship.UAirship;
import com.urbanairship.actions.OpenExternalUrlAction;

public class ReactNativeUAAutoPilot extends Autopilot {

    private static final String TAG = "ReactNativeUAAutoPilot";

    @Override
    public void onAirshipReady(UAirship airship) {
		UAirship.shared().getActionRegistry().unregisterAction(OpenExternalUrlAction.DEFAULT_REGISTRY_NAME);

        Log.w(TAG, "Airship ready!");
    }

}
