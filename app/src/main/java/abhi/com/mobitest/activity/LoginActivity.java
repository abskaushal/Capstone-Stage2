package abhi.com.mobitest.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import abhi.com.mobitest.R;
import abhi.com.mobitest.registeration.GoogleRegFactory;
import abhi.com.mobitest.registeration.IRegistrationManager;
import abhi.com.mobitest.registeration.IUserDataCallback;
import abhi.com.mobitest.registeration.RegistrationAbsFactory;
import abhi.com.mobitest.entity.UserData;
import abhi.com.mobitest.webservices.IWebService;
import abhi.com.mobitest.webservices.WebData;
import abhi.com.mobitest.webservices.async.LoginAsync;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, IUserDataCallback,
        View.OnClickListener, IWebService {

    private IRegistrationManager mRegistrationManager;
    private SignInButton googleBtn;
    private EditText emailEt, passwordEt;
    private Button loginBtn;
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
        emailEt = (EditText) findViewById(R.id.email);
        passwordEt = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(this);
        googleBtn.setOnClickListener(this);
    }

    private void doGoogleSignIn() {
        GoogleRegFactory googleRegFactory = new GoogleRegFactory(LoginActivity.this);
        mRegistrationManager = RegistrationAbsFactory.getRegManager(googleRegFactory);
        mRegistrationManager.startRegistrationProcess();
    }

    private void doLogin() {
        String email = emailEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();
        if (email.length() == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
        } else if (password.length() == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
        } else {
            UserData data = new UserData();
            data.setEmail(email);
            data.setPassword(password);
            LoginAsync loginAsync = new LoginAsync(LoginActivity.this, LoginAsync.LOGIN);
            loginAsync.execute(data);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.sign_in_button:
                doGoogleSignIn();
                break;

            case R.id.login_btn:
                doLogin();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void updateUserData(UserData data) {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        Bundle extra = new Bundle();
        extra.putParcelable(DATA, data);
        registerIntent.putExtra(BUNDLE, extra);
        startActivity(registerIntent);
        mRegistrationManager.stopRegistrationProcess();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mRegistrationManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDataReceived(WebData webData) {
        if (webData.isSuccess()) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
            Toast.makeText(getApplicationContext(), webData.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), webData.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
