package abhi.com.mobitest.preference;

import android.app.Application;
import android.content.Context;

/**
 * Created by Abhishek on 20-Nov-16.
 */
public class AppManager extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        UserPreference.init(getApplicationContext());
    }

}
