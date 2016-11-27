package abhi.com.mobitest.preference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import abhi.com.mobitest.entity.UserData;


/**
 * Class holds user data in SharedPreference
 * <p/>
 * Created by Abhishek on 20-Nov-16.
 */
public class UserPreference {

    private static SharedPreferences mPreferences;
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String IMAGEURL = "imageurl";
    public static final String ROLE = "role";
    public static final String GCM_ID = "gcmid";
    public static final String PARENT_ACCOUNT = "parent_account";
    public static final String LOGIN_STATUS = "loginstatus";
    public static final String USER_ID = "userid";


    public static void init(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void saveUserInfo(UserData userData) {

        SharedPreferences.Editor editor = mPreferences.edit();

        editor.putString(USERNAME, userData.getUserName());
        editor.putString(EMAIL, userData.getEmail());
        editor.putString(IMAGEURL, userData.getImageUrl());
        editor.putString(ROLE, "" + userData.getRole());
        editor.putString(GCM_ID, userData.getGcmId());
        editor.putString(PARENT_ACCOUNT, userData.getParentAccount());
        editor.putBoolean(LOGIN_STATUS, true);
        editor.putInt(USER_ID, userData.getUserId());

        editor.apply();
    }

    public static UserData getUserData() {
        UserData data = new UserData();
        data.setUserName(mPreferences.getString(USERNAME, ""));
        data.setEmail(mPreferences.getString(EMAIL, ""));
        data.setImageUrl(mPreferences.getString(IMAGEURL, ""));
        data.setRole(Integer.parseInt(mPreferences.getString(ROLE, "0")));
        data.setGcmId(mPreferences.getString(GCM_ID, ""));
        data.setParentAccount(mPreferences.getString(PARENT_ACCOUNT, ""));
        data.setUserId(mPreferences.getInt(USER_ID, 0));
        return data;
    }

    public static String getUserId() {
        return "" + mPreferences.getInt(USER_ID, 0);
    }

    public static String getUserDataByField(String field) {
        return mPreferences.getString(field, "");
    }


    public static boolean getLoginStatus() {
        return mPreferences.getBoolean(LOGIN_STATUS, false);
    }

    public static void clearData() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
