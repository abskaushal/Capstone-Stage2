package abhi.com.mobitest.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import abhi.com.mobitest.R;

public class InviteParticipantActivity extends AppCompatActivity {

    public static final String TEST_ID = "test_id";
    public static final String TITLE = "title";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_participant);


        getSupportActionBar().setTitle(getResources().getString(R.string.invite_participant));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
