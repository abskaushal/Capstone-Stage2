package abhi.com.mobitest.webservices;

import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.JsonElement;

import abhi.com.mobitest.constant.UserConstant;
import abhi.com.mobitest.entity.UserData;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Abhishek on 20-Nov-16.
 */
public class UserClient {

    private static final String TAG = UserClient.class.getSimpleName();
    public static final MediaType JSON = MediaType.parse(WebConstant.MEDIA_TYPE);

    private UserClient() {

    }

    private static class UserClientHelper {
        private static final UserClient INSTANCE = new UserClient();
    }

    public static UserClient getInstance() {
        return UserClientHelper.INSTANCE;
    }

    public WebData registerUser(UserData userData) {

        WebData data = new WebData();
        Gson gson = new Gson();
        try {
            String requestMessage = gson.toJson(userData);
            RequestBody body = RequestBody.create(JSON, requestMessage);
            Request request = new Request.Builder()
                    .url(WebConstant.REGISTER_URL)
                    .post(body)
                    .build();
            final OkHttpClient client = OkHttpClientFactory.getOkHttpClientInstance();
            Response response = client.newCall(request).execute();
            String responseMessage = response.body().string();
            Log.v(TAG,responseMessage);

        } catch (Exception e){
            e.printStackTrace();
        }
        return data;


    }
}
