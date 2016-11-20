package abhi.com.mobitest.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import abhi.com.mobitest.R;
import abhi.com.mobitest.registeration.UserData;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEt, emailEt;
    private EditText passwordEt, confirmPasswordEt;
    private RadioGroup roleRadioGroup;
    private Button createBtn;
    private UserData mUserData;


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
    }

    /**
     * Set click listeners on required widgets.
     */
    private void setListeners() {

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent homeIntent = new Intent(RegisterActivity.this,HomeActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(homeIntent);
            }
        });

        roleRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
