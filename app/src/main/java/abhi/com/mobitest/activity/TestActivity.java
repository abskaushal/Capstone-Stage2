package abhi.com.mobitest.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

import abhi.com.mobitest.R;
import abhi.com.mobitest.entity.Question;
import abhi.com.mobitest.entity.TestData;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String BUNDLE = "bundle";
    public static final String TESTDATA = "testdata";
    private TestData mTestData;
    private TextView titleTv, descriptionTv;
    private TextView questionNoTv, questionTv;
    private TextView timeTv;
    private RadioGroup optionRg;
    private LinearLayout rulesLinear, questionLinear;
    private Button startBtn, finishBtn, nextBtn;
    private RadioButton option1, option2, option3, option4;
    private TestTimer mTimer;
    private boolean timerRunning = false;
    private JSONArray mQuestionArray;
    private int mQuestionNo = 0;
    private JSONObject question;
    private int mScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        getSupportActionBar().setTitle(getResources().getString(R.string.start_test));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getBundleExtra(BUNDLE);
        mTestData = bundle.getParcelable(TESTDATA);

        initUI();
        setListeners();
        setData();
        setQuestion();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            backPressed();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.start_test:
                showTestDialog();
                break;

            case R.id.finish:
                showFinishDialog();
                break;

            case R.id.next:
                nextPressed();
                break;
        }
    }

    private void initUI() {

        titleTv = (TextView) findViewById(R.id.title);
        descriptionTv = (TextView) findViewById(R.id.description);
        questionNoTv = (TextView) findViewById(R.id.question_no);
        questionTv = (TextView) findViewById(R.id.question);
        timeTv = (TextView) findViewById(R.id.time);
        rulesLinear = (LinearLayout) findViewById(R.id.rules_linear);
        questionLinear = (LinearLayout) findViewById(R.id.question_linear);
        startBtn = (Button) findViewById(R.id.start_test);
        finishBtn = (Button) findViewById(R.id.finish);
        nextBtn = (Button) findViewById(R.id.next);
        option1 = (RadioButton) findViewById(R.id.rbtn_1);
        option2 = (RadioButton) findViewById(R.id.rbtn_2);
        option3 = (RadioButton) findViewById(R.id.rbtn_3);
        option4 = (RadioButton) findViewById(R.id.rbtn_4);

        rulesLinear.setVisibility(View.VISIBLE);
        questionLinear.setVisibility(View.GONE);
    }

    private void setListeners() {
        startBtn.setOnClickListener(this);
        finishBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
    }

    private void showTestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this)
                .setMessage(getResources().getString(R.string.start_test_confirmation))
                .setPositiveButton(getResources().getString(R.string.start_test), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startTest();
                    }
                }).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        Dialog dialog = builder.create();
        dialog.show();
    }

    private void startTest() {
        rulesLinear.setVisibility(View.GONE);
        questionLinear.setVisibility(View.VISIBLE);
        mTimer = new TestTimer(1800000, 1000);
        mTimer.start();
    }

    private class TestTimer extends CountDownTimer {

        public TestTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timerRunning = true;
            String time = String.format("%02d min %02d sec",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
            );
            timeTv.setText(time);
        }

        @Override
        public void onFinish() {
            timerRunning = false;
        }
    }

    private void backPressed() {

        if (mTimer != null) {
            if (timerRunning) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this)
                        .setMessage(getResources().getString(R.string.test_in_progress))
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TestActivity.this.finish();
                                dialog.dismiss();
                            }
                        }).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });


                Dialog dialog = builder.create();
                dialog.show();
            }
        } else {
            finish();
        }
    }

    private void setData() {
        titleTv.setText(mTestData.getTitle());
        descriptionTv.setText(mTestData.getDescription());
        try {
            mQuestionArray = new JSONArray(mTestData.getQuestion());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void setQuestion() {
        if (mQuestionArray.length() > mQuestionNo) {
            try {
                ((RadioButton) findViewById(optionRg.getCheckedRadioButtonId())).setChecked(false);
                question = mQuestionArray.getJSONObject(mQuestionNo);
                mQuestionNo++;
                questionTv.setText("" + mQuestionNo);
                questionTv.setText(question.getString(Question.question));
                option1.setText(question.getString(Question.option1));
                option2.setText(question.getString(Question.option2));
                option3.setText(question.getString(Question.option3));
                option4.setText(question.getString(Question.option4));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "No more questions", Toast.LENGTH_SHORT).show();
            showFinishDialog();
        }
    }

    private void nextPressed() {
        if (optionRg.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "Please select correct answer", Toast.LENGTH_SHORT).show();
        } else {
            int radioId = optionRg.getCheckedRadioButtonId();
            int selectedOption = 0;
            if (radioId == R.id.rbtn_1) {
                selectedOption = 1;
            } else if (radioId == R.id.rbtn_2) {
                selectedOption = 2;
            } else if (radioId == R.id.rbtn_3) {
                selectedOption = 3;
            } else if (radioId == R.id.rbtn_4) {
                selectedOption = 4;
            }

            try {
                if (selectedOption == question.getInt(Question.correctOption)) {
                    mScore++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setQuestion();
        }
    }

    private void showFinishDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this)
                .setMessage(getResources().getString(R.string.finish_test))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showResult();
                    }
                }).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        Dialog dialog = builder.create();
        dialog.show();
    }

    private void showResult() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this)
                .setMessage("your total score is " + mScore + " out of " + mQuestionArray.length())
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        TestActivity.this.finish();
                    }
                });


        Dialog dialog = builder.create();
        dialog.show();
    }
}
