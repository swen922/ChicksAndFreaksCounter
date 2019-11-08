package com.horovod.android.chicksandfreakscounter;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class ChicksAndFreaksWidget extends AppWidgetProvider {

    private static int counterFreaks = 0;
    private static int counterChicks = 0;
    private static int myWidgetId;

    private static void updateWidget(Context context, AppWidgetManager manager, int widgetId) {

        Log.i("BLOGGGG Widget |||| ", "inside updateWidget()");

        myWidgetId = widgetId;

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_screen);

        remoteViews.setTextViewText(R.id.widget_counter_freaks, String.valueOf(counterFreaks));
        remoteViews.setTextViewText(R.id.widget_counter_chicks, String.valueOf(counterChicks));

        Toast.makeText(context, "updateWidget, freaks = " + counterFreaks + ", chcks = " + counterChicks, Toast.LENGTH_SHORT).show();


        Intent intentOpenApp = new Intent(context, MainActivity.class);
        intentOpenApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntentApp = PendingIntent.getActivity(context, 0, intentOpenApp, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_header, pendingIntentApp);

        Intent intentAddFreak = new Intent(context, MainActivity.class);
        intentAddFreak.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentAddFreak.setAction(Data.KEY_CREATE_FREAK);
        PendingIntent pendingIntentFreak = PendingIntent.getActivity(context, 1, intentAddFreak, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_counter_freaks, pendingIntentFreak);

        Intent intentAddChick = new Intent(context, MainActivity.class);
        intentAddChick.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentAddChick.setAction(Data.KEY_CREATE_CHICK);
        PendingIntent pendingIntentChick = PendingIntent.getActivity(context, 2, intentAddChick, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_counter_chicks, pendingIntentChick);

        manager.updateAppWidget(myWidgetId, remoteViews);

    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.i("BLOGGGG Widget |||| ", "inside ChicksAndFreaksWidget onUpdate");

        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int id : appWidgetIds) {
            updateWidget(context, appWidgetManager, id);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("BLOGGGG Widget |||| ", "inside ChicksAndFreaksWidget onReceive");

        super.onReceive(context, intent);

        if (Data.KEY_UPDATE_WIDGET.equalsIgnoreCase(intent.getAction())) {

            //Log.i("BLOGGGG Widget |||| ", "intent.getAction() = " + intent.getAction());

            int tmp = counterFreaks;
            counterFreaks = intent.getIntExtra(Data.KEY_NUMBER_FREAK, tmp);
            tmp = counterChicks;
            counterChicks = intent.getIntExtra(Data.KEY_NUMBER_CHICK, tmp);

            intent.setAction("");
            intent.setFlags(0);
            intent.removeExtra(Data.KEY_NUMBER_FREAK);
            intent.removeExtra(Data.KEY_NUMBER_CHICK);

            //Toast.makeText(context, "widget onReceive, freaks = " + counterFreaks + ", chcks = " + counterChicks, Toast.LENGTH_SHORT).show();

            updateWidget(context, AppWidgetManager.getInstance(context), myWidgetId);

        }


    }



}
