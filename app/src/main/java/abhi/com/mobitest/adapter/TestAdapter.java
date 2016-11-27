package abhi.com.mobitest.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import abhi.com.mobitest.R;
import abhi.com.mobitest.activity.TestActivity;
import abhi.com.mobitest.constant.UserConstant;
import abhi.com.mobitest.entity.IBaseData;
import abhi.com.mobitest.entity.TestData;
import abhi.com.mobitest.preference.UserPreference;

/**
 * Created by Abhishek on 27-Nov-16.
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    private Context mContext;
    private List<IBaseData> mList;

    public TestAdapter(Context context, List<IBaseData> list) {

        if (context == null || list == null) {
            throw new IllegalArgumentException("Null arguments passed");
        }
        mContext = context;
        mList = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.test_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final TestData testData = (TestData) mList.get(position);
        holder.title.setText(testData.getTitle());
        holder.duration.setText("Duration: " + testData.getDuration() + " min");

        if (UserPreference.getUserDataByField(UserPreference.ROLE).equals( "" + UserConstant.TEACHER)) {
            holder.listBg.setTag(position);
            holder.listBg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int) v.getTag();
                    Intent testIntent = new Intent(mContext, TestActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(TestActivity.TESTDATA, (TestData) mList.get(tag));
                    testIntent.putExtra(TestActivity.BUNDLE, bundle);
                    mContext.startActivity(testIntent);

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, duration;
        LinearLayout listBg;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            duration = (TextView) itemView.findViewById(R.id.duration);
            listBg = (LinearLayout) itemView.findViewById(R.id.list_bg);
        }
    }
}
