package abhi.com.mobitest.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import abhi.com.mobitest.R;
import abhi.com.mobitest.constant.UserConstant;
import abhi.com.mobitest.entity.UserData;
import abhi.com.mobitest.webservices.IWebService;
import abhi.com.mobitest.webservices.WebData;
import abhi.com.mobitest.webservices.async.LoginAsync;

public class RegisterActivity extends AppCompatActivity implements IWebService {

    private EditText usernameEt, emailEt;
    private EditText passwordEt, confirmPasswordEt;
    private RadioGroup roleRadioGroup;
    private Button createBtn;
    private UserData mUserData;
    private int role = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle(getResources().getString(R.string.create_account));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().getBundleExtra(LoginActivity.BUNDLE) != null) {
            Bundle bundle = getIntent().getBundleExtra(LoginActivity.BUNDLE);
            mUserData = bundle.getParcelable(LoginActivity.DATA);
        }

        initUiWidgets();
        setListeners();
    }

    /**
     * Initialize the UI widgets.
     */
    private void initUiWidgets() {
        usernameEt = (EditText) findViewById(R.id.username);
        emailEt = (EditText) findViewById(R.id.email);
        passwordEt = (EditText) findViewById(R.id.password);
        confirmPasswordEt = (EditText) findViewById(R.id.confirm_password);
        roleRadioGroup = (RadioGroup) findViewById(R.id.role_radio_group);
        createBtn = (Button) findViewById(R.id.createAccount);
        usernameEt.setText(mUserData.getUserName());
        emailEt.setText(mUserData.getEmail());
    }

    /**
     * Set click listeners on required widgets.
     */
    private void setListeners() {

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = validate();
                if (msg.length() == 0) {

                    LoginAsync async = new LoginAsync(RegisterActivity.this, LoginAsync.REGISTER);
                    async.execute(mUserData);
                } else {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }

            }
        });

        roleRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rbtn_student) {
                    role = UserConstant.STUDENT;
                } else if (checkedId == R.id.rbtn_teacher) {
                    role = UserConstant.TEACHER;
                }
            }
        });
    }

    private String validate() {
        String msg = "";

        if (TextUtils.isEmpty(usernameEt.getText().toString())) {
            msg = "Please enter Username";
            return msg;
        }
        if (TextUtils.isEmpty(emailEt.getText().toString())) {
            msg = "Please enter Email";
            return msg;
        }
        if (TextUtils.isEmpty(passwordEt.getText().toString())) {
            msg = "Please enter Password";
            return msg;
        }
        if (TextUtils.isEmpty(confirmPasswordEt.getText().toString())) {
            msg = "Please enter Confirm Password";
            return msg;
        }
        if (!passwordEt.getText().toString().trim().equals(confirmPasswordEt.getText().toString().trim())) {
            msg = "Password does not match";
            return msg;
        } else {
            mUserData.setPassword(passwordEt.getText().toString().trim());
        }

        if (role == -1) {
            msg = "Please select your role";
            return msg;
        } else {
            mUserData.setRole(role);
        }
        return msg;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataReceived(WebData webData) {

        if (webData.isSuccess()) {
            Intent homeIntent = new Intent(RegisterActivity.this, HomeActivity.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }
    }
}
