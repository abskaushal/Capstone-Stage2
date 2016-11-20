package abhi.com.mobitest.registeration;

import android.app.Activity;

/**
 * Created by Abhishek on 03-Nov-16.
 */
public class GoogleRegFactory extends RegistrationAbsFactory {
    private Activity mActivity;
    private IUserDataCallback mCallback;

    public GoogleRegFactory(Activity mActivity, IUserDataCallback mCallback) {
        this.mActivity = mActivity;
        this.mCallback = mCallback;
    }

    public GoogleRegFactory(Activity activity) {
        this.mActivity = activity;
        this.mCallback = (IUserDataCallback) activity;
    }

    @Override
    IRegistrationManager createRegManager() {
        return new GooglePlusregManager(mActivity, mCallback);
    }
}
