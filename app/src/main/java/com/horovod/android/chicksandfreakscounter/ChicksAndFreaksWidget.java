package com.horovod.android.chicksandfreakscounter;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class ChicksAndFreaksWidget extends AppWidgetProvider {

    private static int counterFreaks = 0;
    private static int counterChicks = 0;

    private static int myWidgetId;

    private static void updateWidget(Context context, AppWidgetManager manager, int widgetId) {

        myWidgetId = widgetId;

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_screen);
        remoteViews.setTextViewText(R.id.widget_counter_freaks, String.valueOf(counterFreaks));
        remoteViews.setTextViewText(R.id.widget_counter_chicks, String.valueOf(counterChicks));

        Intent intentAddFreak = new Intent(context, MainActivity.class);
        intentAddFreak.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentAddFreak.setAction(Data.KEY_CREATE_FREAK);

        Intent intentAddChick = new Intent(context, MainActivity.class);
        intentAddChick.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentAddChick.setAction(Data.KEY_CREATE_CHICK);

        PendingIntent pendingIntentFreak = PendingIntent.getActivity(context, 1, intentAddFreak, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentChick = PendingIntent.getActivity(context, 2, intentAddChick, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.widget_add_freak, pendingIntentFreak);
        remoteViews.setOnClickPendingIntent(R.id.widget_add_chick, pendingIntentChick);

    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int id : appWidgetIds) {
            updateWidget(context, appWidgetManager, id);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (Data.KEY_UPDATE_WIDGET.equalsIgnoreCase(intent.getAction())) {
            counterFreaks = intent.getIntExtra(Data.KEY_NUMBER_FREAK, counterFreaks);
            counterChicks = intent.getIntExtra(Data.KEY_NUMBER_CHICK, counterChicks);
        }

        updateWidget(context, AppWidgetManager.getInstance(context), myWidgetId);

    }


}
