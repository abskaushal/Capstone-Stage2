package abhi.com.mobitest.webservices.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.concurrent.FutureTask;

import abhi.com.mobitest.R;
import abhi.com.mobitest.entity.UserData;
import abhi.com.mobitest.webservices.IWebService;
import abhi.com.mobitest.webservices.UserClient;
import abhi.com.mobitest.webservices.WebConstant;
import abhi.com.mobitest.webservices.WebData;

/**
 * AsyncTask to hold Login/Register functionality.
 * <p/>
 * Created by Abhishek on 20-Nov-16.
 */
public class LoginAsync extends AsyncTask<UserData, Void, WebData> {

    public static final int LOGIN = 1;
    public static final int REGISTER = 2;
    private Context mContext;
    private int mCode;
    private IWebService mCallback;
    private ProgressDialog mProgressDialog;

    public LoginAsync(Context context, int code) {
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
    protected WebData doInBackground(UserData... params) {
        WebData data = null;
        if (mCode == LOGIN) {
            data = UserClient.getInstance().loginUser(params[0]);
        } else {
            data =  UserClient.getInstance().registerUser(params[0]);
        }
        return data;
    }

    @Override
    protected void onPostExecute(WebData data) {
        super.onPostExecute(data);
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mCallback.onDataReceived(data);
    }
}
