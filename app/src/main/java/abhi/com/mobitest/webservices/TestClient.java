package abhi.com.mobitest.webservices;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import abhi.com.mobitest.entity.IBaseData;
import abhi.com.mobitest.entity.TestData;
import okhttp3.HttpUrl;
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

    /**
     * Api to create test
     *
     * @param testData
     * @return
     */
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

    /**
     * Api to fetch tests by user id for teachers
     *
     * @param testData
     * @return
     */
    public WebData getTestByUserId(TestData testData) {
        WebData data = new WebData();
        final OkHttpClient client = OkHttpClientFactory.getOkHttpClientInstance();
        HttpUrl.Builder builder = HttpUrl.parse(WebConstant.GET_TEST_BY_USER_ID).newBuilder();
        builder.addQueryParameter(WebConstant.USERID, "8000" /*+ userData.getUserId()*/);
        Request request = new Request.Builder().url(builder.build().toString()).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonResponse = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONArray testArray = jsonObject.getJSONArray("mExamTest");
                if (testArray != null && testArray.length() > 0) {
                    data.setSuccess(true);
                    parseTestData(testArray, data);
                } else {
                    data.setSuccess(false);
                    data.setMessage(ERROR_MSG);
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


    private void parseTestData(JSONArray testArray, WebData data) {
        List<IBaseData> testList = new ArrayList<IBaseData>();
        TestData test;
        for (int i = 0; i < testArray.length(); i++) {
            test = new TestData();
            try {
                JSONObject testJson = testArray.getJSONObject(i);
                test.setDuration(testJson.getString("duration"));
                test.setUserId(testJson.getString("user_id"));
                test.setTestId(testJson.getString("test_id"));
                test.setQuestion(testJson.getString("question"));
                test.setTitle(testJson.getString("title"));
                test.setDescription(testJson.getString("description"));

                testList.add(test);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        data.setTestData(testList);
    }


}
