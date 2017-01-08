package abhi.com.mobitest.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Abhishek on 08-Jan-17.
 */
public class NetUtils {

    private NetUtils(){

    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }
}
