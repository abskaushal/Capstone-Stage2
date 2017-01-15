package abhi.com.mobitest.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import abhi.com.mobitest.R;
import abhi.com.mobitest.activity.HomeActivity;

/**
 * Created by Abhishek on 15-Jan-17.
 */
public class WidgetProvider  extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        final int N = appWidgetIds.length;
		/*int[] appWidgetIds holds ids of multiple instance of your widget
		 * meaning you are placing more than one widgets on your homescreen*/
        for (int i = 0; i < N; ++i) {
            RemoteViews remoteViews = updateWidgetListView(context,
                    appWidgetIds[i],appWidgetManager);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId, AppWidgetManager appWidgetManager) {

        //which layout to show on widget
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.test_app_widget);

        Intent intent = new Intent(context, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_layout_main, pendingIntent);


        //RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, WidgetService.class);
        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        //setting a unique Uri to the intent
        //don't know its purpose to me right now
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        //setting adapter to listview of the widget
        remoteViews.setRemoteAdapter(appWidgetId, R.id.widget_list,
                svcIntent);

        remoteViews.setRemoteAdapter(appWidgetId, svcIntent);
        //setting an empty view in case of no data
        //remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);

        ComponentName componentName = new ComponentName(context, WidgetProvider.class);
        appWidgetManager.updateAppWidget(componentName, remoteViews);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list);

        return remoteViews;
    }

}
