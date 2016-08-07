package info.nkzn.biblia.sample;

import android.app.Application;

import info.nkzn.biblia.Biblia;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Biblia.setRakutenApplicationId(BuildConfig.RAKUTEN_APPLICATION_ID);
    }
}
