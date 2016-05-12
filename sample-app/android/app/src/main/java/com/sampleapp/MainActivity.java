package com.sampleapp;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;

import java.util.Arrays;
import java.util.List;

import com.globo.reactnativeua.ReactNativeUAPackage;


public class MainActivity extends ReactActivity {
    private ReactNativeUAPackage mReactNativeUAPackage;

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "SampleApp";
    }

    /**
     * Returns whether dev mode should be enabled.
     * This enables e.g. the dev menu.
     */
    @Override
    protected boolean getUseDeveloperSupport() {
        return BuildConfig.DEBUG;
    }

    /**
     * A list of packages used by the app. If the app uses additional views
     * or modules besides the default ones, add more packages here.
     */
    @Override
    protected List<ReactPackage> getPackages() {
        mReactNativeUAPackage = new ReactNativeUAPackage(this);
        return Arrays.<ReactPackage>asList(
            new MainReactPackage(),
            mReactNativeUAPackage
        );
    }
}
