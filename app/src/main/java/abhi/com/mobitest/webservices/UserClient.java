package abhi.com.mobitest.webservices;

import android.content.Intent;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import abhi.com.mobitest.constant.UserConstant;
import abhi.com.mobitest.entity.UserData;
import abhi.com.mobitest.preference.UserPreference;
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
    private static final String ERROR_MSG = "Error in getting data.Please try again.";

    private UserClient() {

    }

    private static class UserClientHelper {
        private static final UserClient INSTANCE = new UserClient();
    }

    public static UserClient getInstance() {
        return UserClientHelper.INSTANCE;
    }


    /**
     * Register the new user.
     *
     * @param userData
     * @return
     */
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
            if (response.isSuccessful()) {
                data.setSuccess(true);
                UserPreference.saveUserInfo(new UserData());
                String responseMessage = response.body().string();
                Log.v(TAG, responseMessage);
            } else {
                data.setSuccess(false);
                data.setMessage(ERROR_MSG);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Login user api
     *
     * @param userData
     * @return
     */
    public WebData loginUser(UserData userData) {
        WebData data = new WebData();
        final OkHttpClient client = OkHttpClientFactory.getOkHttpClientInstance();
        HttpUrl.Builder builder = HttpUrl.parse(WebConstant.LOGIN_URL).newBuilder();
        builder.addQueryParameter(WebConstant.EMAIL, userData.getEmail());
        builder.addQueryParameter(WebConstant.PASSWORD, userData.getPassword());
        Request request = new Request.Builder().url(builder.build().toString()).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {

                data.setSuccess(true);
                data.setMessage("Login Successful");
                String jsonResponse = response.body().string();
                JSONObject object = new JSONObject(jsonResponse);
                JSONObject statusObject = object.getJSONObject("status");
                if (statusObject.has("statusCode")) {
                    data.setStatusCode(statusObject.getInt("statusCode"));
                }


                if (data.getStatusCode() != 301) {

                    JSONObject userObject = object.getJSONObject("user");
                    parseUserData(userObject, userData);
                    UserPreference.saveUserInfo(userData);
                } else {
                    data.setSuccess(false);
                    if (statusObject.has("statusMessage")) {
                        data.setMessage(statusObject.getString("statusMessage"));
                    }
                }
            } else {
                data.setSuccess(false);
                data.setMessage(ERROR_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
            data.setSuccess(false);
            data.setMessage(ERROR_MSG);
        }

        return data;
    }

    private void parseUserData(JSONObject jsonObject, UserData userData) {
        {
            try {
                userData.setUserId(Integer.parseInt(jsonObject.getString("id")));
                userData.setUserName(jsonObject.getString("displayname"));
                userData.setImageUrl(jsonObject.getString("imageurl"));
                userData.setRole(Integer.parseInt(jsonObject.getString("category")));
                userData.setParentAccount(jsonObject.getString("parentaccount"));
                userData.setGcmId(jsonObject.getString("gsmid"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
