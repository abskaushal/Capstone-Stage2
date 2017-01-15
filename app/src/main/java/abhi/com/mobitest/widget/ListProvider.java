package abhi.com.mobitest.widget;

import android.app.LauncherActivity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import abhi.com.mobitest.R;
import abhi.com.mobitest.entity.TestData;
import abhi.com.mobitest.provider.TestDataProvider;

/**
 * Created by Abhishek on 15-Jan-17.
 */
public class ListProvider implements RemoteViewsService.RemoteViewsFactory {

    private int appWidgetId;

    private List<TestData> mList = new ArrayList<>();
    private Context mContext;

    public ListProvider(Context context, Intent intent) {
        this.mContext = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        fetchTests();
    }

    @Override
    public void onCreate() {
        fetchTests();
    }

    @Override
    public void onDataSetChanged() {
        fetchTests();
    }

    private void fetchTests() {
        mList.clear();

        mList.add(new TestData());
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



    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     *Similar to getView of Adapter where instead of View
     *we return RemoteViews
     *
     */
    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView = new RemoteViews(
                mContext.getPackageName(), R.layout.widget_test_list_row);
        if(mList.size()==1){
            remoteView.setTextViewText(R.id.title, "No data available");
        }else{
            TestData listItem = mList.get(position);
            remoteView.setTextViewText(R.id.title, listItem.getTitle());
            remoteView.setTextViewText(R.id.duration, listItem.getDuration());
        }


        return remoteView;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public void onDestroy() {
    }

}