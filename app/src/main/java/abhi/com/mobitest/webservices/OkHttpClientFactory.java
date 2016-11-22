package abhi.com.mobitest.webservices;

import okhttp3.OkHttpClient;

/**
 * Created by Abhishek on 20-Nov-16.
 */
public class OkHttpClientFactory {
    private static  OkHttpClient sOkHttpClient;
    public static OkHttpClient getOkHttpClientInstance() {
        if (sOkHttpClient == null) {
            synchronized (OkHttpClientFactory.class) {
                if (sOkHttpClient == null) {
                    sOkHttpClient = new OkHttpClient();
                }
            }
        }
        return sOkHttpClient;
    }
}
