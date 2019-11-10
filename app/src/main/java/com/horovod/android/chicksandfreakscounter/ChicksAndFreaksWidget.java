package com.horovod.android.chicksandfreakscounter;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Arrays;

public class ChicksAndFreaksWidget extends AppWidgetProvider {

    private static int counterFreaks = 0;
    private static int counterChicks = 0;


    public static void updateWidget(Context context, AppWidgetManager manager, int widgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_screen);
        remoteViews.setTextViewText(R.id.widget_counter_freaks, String.valueOf(counterFreaks));
        remoteViews.setTextViewText(R.id.widget_counter_chicks, String.valueOf(counterChicks));

        Intent intentOpenApp = new Intent(context, MainActivity.class);
        intentOpenApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentApp = PendingIntent.getActivity(context, widgetId, intentOpenApp, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_layout, pendingIntentApp);

        manager.updateAppWidget(widgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int id : appWidgetIds) {
            updateWidget(context, appWidgetManager, id);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        int tmp = counterFreaks;
        counterFreaks = intent.getIntExtra(Data.KEY_NUMBER_FREAK, tmp);
        tmp = counterChicks;
        counterChicks = intent.getIntExtra(Data.KEY_NUMBER_CHICK, tmp);

        super.onReceive(context, intent);

    }

}
