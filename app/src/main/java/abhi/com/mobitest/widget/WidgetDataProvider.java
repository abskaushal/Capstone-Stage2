package abhi.com.mobitest.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import abhi.com.mobitest.R;
import abhi.com.mobitest.entity.TestData;
import abhi.com.mobitest.provider.TestDataProvider;

/**
 * Created by Abhishek on 19-Mar-16.
 */
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = WidgetDataProvider.class.getSimpleName();

    private List<TestData> mList = new ArrayList<>();
    private Context mContext;

    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        fetchTests();
    }

    @Override
    public void onDataSetChanged() {
        fetchTests();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews views;
        if (mList.size() == 0) {
            views = new RemoteViews(mContext.getPackageName(), R.layout.widget_no_internet);
        } else {
            views  = new RemoteViews(mContext.getPackageName(), R.layout.widget_test_row);
            TestData entity = mList.get(i);
            views.setTextViewText(R.id.title, entity.getTitle());
            views.setTextViewText(R.id.duration, entity.getDuration());
        }
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void fetchTests() {
        mList.clear();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(calendar.getTime());
        String arr[] = new String[]{date};

        Cursor cursor = mContext.getContentResolver().query(TestDataProvider.CONTENT_URI, null, null, null, null);
        TestData entity;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                entity = new TestData();
                entity.setTitle(cursor.getString(cursor.getColumnIndex(TestDataProvider.TITLE)));
                entity.setDuration(cursor.getString(cursor.getColumnIndex(TestDataProvider.DURATION)));
                mList.add(entity);
            } while (cursor.moveToNext());

            cursor.close();
        }

    }
}
