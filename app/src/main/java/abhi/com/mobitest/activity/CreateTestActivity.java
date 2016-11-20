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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import abhi.com.mobitest.R;
import abhi.com.mobitest.entity.Question;
import abhi.com.mobitest.utils.CustomDialog;

public class CreateTestActivity extends AppCompatActivity {

    private EditText titleEt, descriptionEt;
    private EditText questionEt;
    private EditText option1Et, option2Et;
    private EditText option3Et, option4Et;
    private TextView questionNoTv;
    private Button createBtn, addMoreBtn;
    private RadioGroup correctOptionRg;
    private List<Question> mList;
    private Question mQuestion;
    private int mCorrectOption = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_test);
        getSupportActionBar().setTitle(getResources().getString(R.string.create_test));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mList = new ArrayList<Question>();


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
        questionNoTv.setText("" + (mList.size() + 1));

    }

    /**
     * Set click listeners on required widgets.
     */
    private void setListeners() {

        addMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewQuestion();
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
    private void addNewQuestion() {
        // TODO validate the data

        mQuestion = new Question();
        mQuestion.setQuestion(questionEt.getText().toString().trim());
        mQuestion.setOption1(option1Et.getText().toString().trim());
        mQuestion.setOption2(option2Et.getText().toString().trim());
        mQuestion.setOption3(option3Et.getText().toString().trim());
        mQuestion.setOption4(option4Et.getText().toString().trim());
        mQuestion.setCorrectOption(mCorrectOption);
        mQuestion.setSerialNo(mList.size() + 1);
        mList.add(mQuestion);

        clearData();

    }

    /**
     * Clear the question ui widgets data
     */
    private void clearData() {
        questionNoTv.setText("" + (mList.size() + 1));
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

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateTestActivity.this)
                .setMessage(getResources().getString(R.string.test_created_successfully))
                .setPositiveButton(getResources().getString(R.string.invite), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent inviteIntent = new Intent(CreateTestActivity.this, InviteParticipantActivity.class);
                        inviteIntent.putExtra(InviteParticipantActivity.TEST_ID,1);
                        inviteIntent.putExtra(InviteParticipantActivity.TITLE,"Test");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
