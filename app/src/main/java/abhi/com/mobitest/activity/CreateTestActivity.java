package abhi.com.mobitest.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import abhi.com.mobitest.R;
import abhi.com.mobitest.entity.Question;
import abhi.com.mobitest.entity.TestData;
import abhi.com.mobitest.preference.AppManager;
import abhi.com.mobitest.preference.UserPreference;
import abhi.com.mobitest.utils.CustomDialog;
import abhi.com.mobitest.webservices.IWebService;
import abhi.com.mobitest.webservices.WebData;
import abhi.com.mobitest.webservices.async.TestAsync;

public class CreateTestActivity extends AppCompatActivity implements IWebService {

    private EditText titleEt, descriptionEt;
    private EditText questionEt;
    private EditText option1Et, option2Et;
    private EditText option3Et, option4Et;
    private TextView questionNoTv;
    private Button createBtn, addMoreBtn;
    private RadioGroup correctOptionRg;
    private int mCorrectOption = 0;
    private JSONArray mQuesJsonArray;
    private JSONObject mQuesObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_test);
        getSupportActionBar().setTitle(getResources().getString(R.string.create_test));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mQuesJsonArray = new JSONArray();

        initUiWidgets();
        setListeners();

    }

    /**
     * Initialize the UI widgets.
     */
    private void initUiWidgets() {
        titleEt = (EditText) findViewById(R.id.title);
        descriptionEt = (EditText) findViewById(R.id.description);
        questionEt = (EditText) findViewById(R.id.question);
        questionNoTv = (TextView) findViewById(R.id.question_no);
        option1Et = (EditText) findViewById(R.id.option_1);
        option2Et = (EditText) findViewById(R.id.option_2);
        option3Et = (EditText) findViewById(R.id.option_3);
        option4Et = (EditText) findViewById(R.id.option_4);
        correctOptionRg = (RadioGroup) findViewById(R.id.correct_option_radio_group);
        createBtn = (Button) findViewById(R.id.create_test);
        addMoreBtn = (Button) findViewById(R.id.add_more);
        questionNoTv.setText("" + (mQuesJsonArray.length() + 1));

    }

    /**
     * Set click listeners on required widgets.
     */
    private void setListeners() {

        addMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addNewQuestion()) {
                    clearData();
                }
            }
        });
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTest();
            }
        });
    }

    /**
     * Add current question to the list
     */
    private boolean addNewQuestion() {
        String msg = validateData();
        boolean success = false;
        if (msg.length() == 0) {

            mQuesObject = new JSONObject();
            try {
                mQuesObject.put(Question.question, questionEt.getText().toString().trim());
                mQuesObject.put(Question.option1, option1Et.getText().toString().trim());
                mQuesObject.put(Question.option2, option2Et.getText().toString().trim());
                mQuesObject.put(Question.option3, option3Et.getText().toString().trim());
                mQuesObject.put(Question.option4, option4Et.getText().toString().trim());
                mQuesObject.put(Question.correctOption, mCorrectOption);
                mQuesObject.put(Question.serialNo, mQuesJsonArray.length() + 1);
                mQuesJsonArray.put(mQuesObject);
                success = true;
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {
            success = false;
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }

        return success;

    }

    private String validateData() {
        String msg = "";

        if (titleEt.getText().toString().trim().equals("")) {
            msg = "Please enter title";
            return msg;
        }

        if (descriptionEt.getText().toString().trim().equals("")) {
            msg = "Please enter description";
            return msg;
        }

        if (questionEt.getText().toString().trim().equals("")) {
            msg = "Please enter question";
            return msg;
        }
        if (option1Et.getText().toString().trim().equals("")) {
            msg = "Please enter option 1";
            return msg;
        }
        if (option2Et.getText().toString().trim().equals("")) {
            msg = "Please enter option 2";
            return msg;
        }
        if (option3Et.getText().toString().trim().equals("")) {
            msg = "Please enter option 3";
            return msg;
        }
        if (option4Et.getText().toString().trim().equals("")) {
            msg = "Please enter option 4";
            return msg;
        }

        if (correctOptionRg.getCheckedRadioButtonId() == -1) {
            msg = "Please select correct option";
            return msg;
        }
        return msg;
    }

    /**
     * Clear the question ui widgets data
     */
    private void clearData() {
        questionNoTv.setText("" + (mQuesJsonArray.length() + 1));
        questionEt.setText("");
        option1Et.setText("");
        option2Et.setText("");
        option3Et.setText("");
        option4Et.setText("");
        ((RadioButton) findViewById(correctOptionRg.getCheckedRadioButtonId())).setChecked(false);

    }

    /**
     * Create test
     */
    private void createTest() {

        if (addNewQuestion()) {
            TestData testData = new TestData();
            testData.setTitle(titleEt.getText().toString().trim());
            testData.setDescription(descriptionEt.getText().toString().trim());
            testData.setQuestion(mQuesJsonArray.toString());
            testData.setUserId(UserPreference.getUserId());
            testData.setDuration("30");

            TestAsync testAsync = new TestAsync(CreateTestActivity.this, TestAsync.CREATE_TEST);
            testAsync.execute(testData);

        }


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
            if (webData.getStatusCode() == TestAsync.CREATE_TEST) {
                showInviteDialog((TestData) webData.getData());
            }
        } else {
            Toast.makeText(getApplicationContext(), webData.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showInviteDialog(final TestData testData) {


        AlertDialog.Builder builder = new AlertDialog.Builder(CreateTestActivity.this)
                .setMessage(getResources().getString(R.string.test_created_successfully))
                .setPositiveButton(getResources().getString(R.string.invite), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent inviteIntent = new Intent(CreateTestActivity.this, InviteParticipantActivity.class);
                        inviteIntent.putExtra(InviteParticipantActivity.TEST_ID, testData.getTestId());
                        inviteIntent.putExtra(InviteParticipantActivity.TITLE, testData.getTitle());
                        startActivity(inviteIntent);
                        CreateTestActivity.this.finish();
                    }
                }).setNegativeButton(getResources().getString(R.string.later), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CreateTestActivity.this.finish();
                    }
                });


        Dialog dialog = builder.create();
        dialog.show();
    }
}
