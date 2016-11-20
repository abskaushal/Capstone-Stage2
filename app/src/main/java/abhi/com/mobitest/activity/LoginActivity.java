package abhi.com.mobitest.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import abhi.com.mobitest.R;
import abhi.com.mobitest.registeration.GoogleRegFactory;
import abhi.com.mobitest.registeration.IRegistrationManager;
import abhi.com.mobitest.registeration.IUserDataCallback;
import abhi.com.mobitest.registeration.RegistrationAbsFactory;
import abhi.com.mobitest.registeration.UserData;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,IUserDataCallback,
        View.OnClickListener {

    private IRegistrationManager mRegistrationManager;
    private SignInButton googleBtn;
    public static final String DATA = "data";
    public static final String BUNDLE = "bundle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
    }

    private void initUI() {
        googleBtn = (SignInButton) findViewById(R.id.sign_in_button);
        googleBtn.setOnClickListener(this);
    }

    private void doGoogleSignIn(){
        GoogleRegFactory googleRegFactory = new GoogleRegFactory(LoginActivity.this);
        mRegistrationManager = RegistrationAbsFactory.getRegManager(googleRegFactory);
        mRegistrationManager.startRegistrationProcess();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.sign_in_button:
                doGoogleSignIn();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void updateUserData(UserData data) {
        Intent registerIntent = new Intent(this,RegisterActivity.class);
        Bundle extra = new Bundle();
        extra.putParcelable(DATA,data);
        registerIntent.putExtra(BUNDLE,extra);
        startActivity(registerIntent);
        Toast.makeText(LoginActivity.this,""+data.mDisplayName,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mRegistrationManager.onActivityResult(requestCode, resultCode, data);
    }
}
