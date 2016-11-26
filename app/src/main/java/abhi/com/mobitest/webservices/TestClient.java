package abhi.com.mobitest.webservices;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import abhi.com.mobitest.entity.TestData;
import abhi.com.mobitest.entity.UserData;
import abhi.com.mobitest.preference.UserPreference;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * TestClient class contains test related webservices.
 * <p/>
 * Created by Abhishek on 26-Nov-16.
 */
public class TestClient {

    private static final String TAG = TestClient.class.getSimpleName();
    public static final MediaType JSON = MediaType.parse(WebConstant.MEDIA_TYPE);
    private static final String ERROR_MSG = "Error in getting data.Please try again.";

    private TestClient() {

    }

    private static class TestClientHelper {
        private static final TestClient INSTANCE = new TestClient();
    }

    public static TestClient getInstance() {
        return TestClientHelper.INSTANCE;
    }

    public WebData createTest(TestData testData) {

        WebData data = new WebData();
        Gson gson = new Gson();
        try {
            String requestMessage = gson.toJson(testData);
            RequestBody body = RequestBody.create(JSON, requestMessage);
            Request request = new Request.Builder()
                    .url(WebConstant.CREATE_TEST_URL)
                    .post(body)
                    .build();
            final OkHttpClient client = OkHttpClientFactory.getOkHttpClientInstance();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                data.setSuccess(true);
                String responseMessage = response.body().string();
                JSONObject obj = new JSONObject(responseMessage);
                parseTestId(obj, testData);
                data.setData(testData);
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

    private void parseTestId(JSONObject object, TestData testData) {

        try {
            JSONObject jsonObject = object.getJSONObject("mExamTest");
            if (jsonObject != null && jsonObject.has("test_id")) {
                testData.setTestId(jsonObject.getString("test_id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
