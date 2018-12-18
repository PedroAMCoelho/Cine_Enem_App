package br.infnet.com.cineenem;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class CineEnemApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Ubuntu-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
