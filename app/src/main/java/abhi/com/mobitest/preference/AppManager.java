package abhi.com.mobitest.preference;

import android.app.Application;
import android.content.Context;

/**
 * Created by Abhishek on 20-Nov-16.
 */
public class AppManager extends Application {

    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }
}
