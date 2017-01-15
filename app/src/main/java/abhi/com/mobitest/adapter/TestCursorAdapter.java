package abhi.com.mobitest.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import abhi.com.mobitest.R;
import abhi.com.mobitest.activity.TestActivity;
import abhi.com.mobitest.constant.UserConstant;
import abhi.com.mobitest.entity.TestData;
import abhi.com.mobitest.preference.UserPreference;
import abhi.com.mobitest.provider.TestDataProvider;

/**
 * Created by Abhishek on 15-Jan-17.
 */
public class TestCursorAdapter extends CursorAdapter {

    public TestCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View mItem = LayoutInflater.from(context).inflate(R.layout.test_list, parent, false);
        ViewHolder mHolder = new ViewHolder(mItem);
        mItem.setTag(mHolder);
        return mItem;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final ViewHolder mHolder = (ViewHolder) view.getTag();
        mHolder.title.setText(cursor.getString(cursor.getColumnIndex(TestDataProvider.TITLE)));
        mHolder.duration.setText(cursor.getString(cursor.getColumnIndex(TestDataProvider.DURATION)));

    }


    public static class ViewHolder {

        TextView title, duration;
        LinearLayout listBg;

        public ViewHolder(View itemView) {
            title = (TextView) itemView.findViewById(R.id.title);
            duration = (TextView) itemView.findViewById(R.id.duration);
            listBg = (LinearLayout) itemView.findViewById(R.id.list_bg);
        }
    }
}
