package abhi.com.mobitest.registeration;

import android.content.Intent;

/**
 * Created by Abhishek on 03-Nov-16.
 */
public interface IRegistrationManager {
    public void startRegistrationProcess() throws NullPointerException;
    public void stopRegistrationProcess();
    public void processResponse(UserData data);
    public void onActivityResult(int requestCode, int resultCode, Intent data);
}
