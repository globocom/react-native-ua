package com.globo.reactnativeua;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import com.urbanairship.util.HelperActivity;

class RequestPermissionsTask extends AsyncTask<String, Void, Boolean> {

    private final Context context;
    private Callback callback;

    interface Callback {
        void onResult(boolean enabled);
    }

    RequestPermissionsTask(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(String... permissions) {
        int[] result = HelperActivity.requestPermissions(context, permissions);
        for (int element : result) {
            if (element == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (callback != null) {
            callback.onResult(result);
        }
    }
}

