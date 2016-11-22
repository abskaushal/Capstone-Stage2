package abhi.com.mobitest.registeration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import abhi.com.mobitest.constant.UserConstant;
import abhi.com.mobitest.entity.UserData;


/**
 * Created by Abhishek on 03-Nov-16.
 */
public class GooglePlusregManager implements IRegistrationManager,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    private Context mContext;
    private IUserDataCallback mUserDataCallback;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private AppCompatActivity mActivity;
    private int RC_SIGN_IN = 100;

    public GooglePlusregManager(Context mContext, IUserDataCallback mUserDataCallback) {
        this.mContext = mContext;
        this.mUserDataCallback = mUserDataCallback;
        try {
            mActivity = (AppCompatActivity) mContext;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startRegistrationProcess() throws NullPointerException {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage(mActivity, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        mActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void stopRegistrationProcess() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            mGoogleApiClient.stopAutoManage((AppCompatActivity) mContext);
            mGoogleApiClient.disconnect();
            mGoogleApiClient = null;
            gso = null;
        }
    }

    @Override
    public void processResponse(UserData data) {
        if (mUserDataCallback != null) {
            mUserDataCallback.updateUserData(data);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("co");
    }

    private void handleSignInResult(GoogleSignInResult result) {
        //If the login succeed
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            UserData data = new UserData();
            if (acct.getDisplayName() != null) {
                data.setUserName(acct.getDisplayName());
            }
            if (acct.getEmail() != null) {
                data.setEmail(acct.getEmail());
            }
            if (acct.getPhotoUrl() != null && acct.getPhotoUrl().toString() != null) {
                data.setImageUrl(acct.getPhotoUrl().toString());
            }
            data.setParentAccount(UserConstant.GOOGLE);
            processResponse(data);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
