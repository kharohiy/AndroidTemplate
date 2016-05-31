package com.template.android.application;

import android.app.Application;

import com.template.android.BuildConfig;
import com.template.android.R;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

/**
 * Created by user on 5/31/16.
 */
@ReportsCrashes(
        mailTo = "some@email.com",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.app_toast_error)
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            ACRA.init(this);
        }
    }
}
