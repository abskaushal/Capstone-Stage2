package abhi.com.mobitest.activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

import java.util.List;

import abhi.com.mobitest.R;
import abhi.com.mobitest.adapter.TestAdapter;
import abhi.com.mobitest.adapter.TestCursorAdapter;
import abhi.com.mobitest.entity.IBaseData;
import abhi.com.mobitest.entity.TestData;
import abhi.com.mobitest.preference.UserPreference;
import abhi.com.mobitest.provider.TestDataProvider;
import abhi.com.mobitest.utils.RoundedImageView;
import abhi.com.mobitest.webservices.IWebService;
import abhi.com.mobitest.webservices.WebData;
import abhi.com.mobitest.webservices.async.LoginAsync;
import abhi.com.mobitest.webservices.async.TestAsync;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IWebService, LoaderManager.LoaderCallbacks<Cursor> {


    private CardView mNoTestView;
    private ListView mTestRecyclerView;
    private TestCursorAdapter cursorAdapter;
    private static final int TEST_LOADER = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (!UserPreference.getLoginStatus()) {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            HomeActivity.this.finish();
            return;
        }
        initToolbar();
        initUI();

        getSupportLoaderManager().initLoader(TEST_LOADER, null, HomeActivity.this);
        fetchTests();

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3305246529016543~8383730915");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }

    private void initUI() {
        mNoTestView = (CardView) findViewById(R.id.cardview);
        mTestRecyclerView = (ListView) findViewById(R.id.recycler_view);
        cursorAdapter = new TestCursorAdapter(HomeActivity.this, null, 0);
        mTestRecyclerView.setAdapter(cursorAdapter);

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.create_test);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createIntent = new Intent(HomeActivity.this, CreateTestActivity.class);
                startActivity(createIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        RoundedImageView userImageView = (RoundedImageView) header.findViewById(R.id.user_image);
        TextView userNameTv = (TextView) header.findViewById(R.id.username);
        TextView emailTv = (TextView) header.findViewById(R.id.email);

        userNameTv.setText(UserPreference.getUserDataByField(UserPreference.USERNAME));
        emailTv.setText(UserPreference.getUserDataByField(UserPreference.EMAIL));
        if (UserPreference.getUserDataByField(UserPreference.IMAGEURL).length() > 0) {
            Picasso.with(getApplicationContext())
                    .load(UserPreference.getUserDataByField(UserPreference.IMAGEURL))
                    .error(R.mipmap.ic_launcher)
                    .into(userImageView);
        } else {
            userImageView.setImageResource(R.mipmap.ic_launcher);
        }
    }

    private void fetchTests() {
        TestAsync fetchTest = new TestAsync(HomeActivity.this, TestAsync.GET_TEST_BY_USER_ID);
        TestData data = new TestData();
        data.setUserId(UserPreference.getUserId());
        fetchTest.execute(data);
    }

/*    private void setTestList(List<IBaseData> list) {
        if (list.size() > 0) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
            mTestRecyclerView.setHasFixedSize(true);
            mTestRecyclerView.setLayoutManager(gridLayoutManager);
            TestAdapter testAdapter = new TestAdapter(HomeActivity.this, list);
            mTestRecyclerView.setAdapter(testAdapter);

            mNoTestView.setVisibility(View.GONE);
            mTestRecyclerView.setVisibility(View.VISIBLE);

        } else {
            mNoTestView.setVisibility(View.VISIBLE);
            mTestRecyclerView.setVisibility(View.GONE);
        }
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {

        } else if (id == R.id.nav_share) {
            share();
        } else if (id == R.id.nav_logout) {
            doLogout();
        } else if (id == R.id.nav_about_us) {
            showAboutUs();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void doLogout() {

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this)
                .setMessage(getResources().getString(R.string.logout_msg))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        UserPreference.clearData();
                        getContentResolver().delete(TestDataProvider.CONTENT_URI, null, null);
                        startActivity(intent);
                        dialog.dismiss();
                        HomeActivity.this.finish();

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

    @Override
    public void onDataReceived(WebData webData) {

        if (webData.isSuccess()) {
            if (webData.getApiCode() == TestAsync.GET_TEST_BY_USER_ID) {
                saveTestDataLocally(webData.getTestData());
            }
        } else {
            mNoTestView.setVisibility(View.VISIBLE);
            mTestRecyclerView.setVisibility(View.GONE);
        }
    }

    private void saveTestDataLocally(List<IBaseData> dataList) {

        for (IBaseData iData : dataList) {
            TestData data = (TestData) iData;
            ContentValues contentValues = new ContentValues();
            contentValues.put(TestDataProvider.DESCRIPTION, data.getDescription());
            contentValues.put(TestDataProvider.DURATION, data.getDuration());
            contentValues.put(TestDataProvider.USER_ID, data.getUserId());
            contentValues.put(TestDataProvider.TEST_ID, data.getTestId());
            contentValues.put(TestDataProvider.QUESTION, data.getQuestion());
            contentValues.put(TestDataProvider.TITLE, data.getTitle());

            getContentResolver().insert(TestDataProvider.CONTENT_URI, contentValues);
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(HomeActivity.this, TestDataProvider.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int i = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            i++;
            cursor.moveToNext();
        }
        //Log.v(FetchScoreTask.LOG_TAG,"Loader query: " + String.valueOf(i));
        cursorAdapter.swapCursor(cursor);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }


    private void showAboutUs() {

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this)
                .setMessage(getResources().getString(R.string.about_us_msg))
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });


        Dialog dialog = builder.create();
        dialog.show();
    }


    private void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text));
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


}
