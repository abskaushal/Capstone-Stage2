package abhi.com.mobitest.webservices.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import abhi.com.mobitest.R;
import abhi.com.mobitest.entity.TestData;
import abhi.com.mobitest.entity.UserData;
import abhi.com.mobitest.webservices.IWebService;
import abhi.com.mobitest.webservices.TestClient;
import abhi.com.mobitest.webservices.UserClient;
import abhi.com.mobitest.webservices.WebData;

/**
 * AsyncTask to hold Test related functionality.
 * <p/>
 * Created by Abhishek on 20-Nov-16.
 */
public class TestAsync extends AsyncTask<TestData, Void, WebData> {

    public static final int CREATE_TEST = 1;
    public static final int GET_TEST_BY_USER_ID = 2;
    public static final int INVITE_STUDENT = 3;
    private Context mContext;
    private int mCode;
    private IWebService mCallback;
    private ProgressDialog mProgressDialog;

    public TestAsync(Context context, int code) {
        if (context == null) {
            throw new IllegalArgumentException("Null arguments passed");
        }
        mContext = context;
        mCode = code;
        mCallback = (IWebService) context;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(mContext.getResources().getString(R.string.loading));
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    @Override
    protected WebData doInBackground(TestData... params) {
        WebData data = null;
        if (mCode == CREATE_TEST) {
            data = TestClient.getInstance().createTest(params[0]);
        }else if(mCode == GET_TEST_BY_USER_ID){
            data = TestClient.getInstance().getTestByUserId(params[0]);
        }
        return data;
    }

    @Override
    protected void onPostExecute(WebData data) {
        super.onPostExecute(data);
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        data.setApiCode(mCode);
        mCallback.onDataReceived(data);
    }
}
