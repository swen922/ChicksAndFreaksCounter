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
    private static int myWidgetId = 0;

    public static void updateWidget(Context context, AppWidgetManager manager, int widgetId) {

        Log.i("BLOGGGG Widget |||| ", "inside ChicksAndFreaksWidget updateWidget()");

        Log.i("BLOGGGG Widget |||| ", "updateWidget widgetId = " + widgetId);

        myWidgetId = widgetId;

        Log.i("BLOGGGG Widget |||| ", "updateWidget myWidgetId = " + myWidgetId);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_screen);
        remoteViews.setTextViewText(R.id.widget_counter_freaks, String.valueOf(counterFreaks));
        remoteViews.setTextViewText(R.id.widget_counter_chicks, String.valueOf(counterChicks));

        //Toast.makeText(context, "updateWidget, freaks = " + counterFreaks + ", chcks = " + counterChicks, Toast.LENGTH_SHORT).show();

        Intent intentOpenApp = new Intent(context, MainActivity.class);
        intentOpenApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentApp = PendingIntent.getActivity(context, myWidgetId, intentOpenApp, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_layout, pendingIntentApp);

        manager.updateAppWidget(myWidgetId, remoteViews);

    }



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //super.onUpdate(context, appWidgetManager, appWidgetIds);

        Log.i("BLOGGGG Widget |||| ", "inside ChicksAndFreaksWidget onUpdate");

        Log.i("BLOGGGG Widget |||| ", "onUpdate appWidgetIds = " + Arrays.toString(appWidgetIds));

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

        Log.i("BLOGGGG Widget |||| ", "inside ChicksAndFreaksWidget updateCoiunters, freaks = " + counterFreaks + ", chcks = " + counterChicks);

        super.onReceive(context, intent);

    }


    /*private static void updateCoiunters(RemoteViews remoteViews, AppWidgetManager manager) {

        //Log.i("BLOGGGG Widget |||| ", "inside ChicksAndFreaksWidget updateCoiunters");

        Log.i("BLOGGGG Widget |||| ", "inside ChicksAndFreaksWidget updateCoiunters, freaks = " + counterFreaks + ", chcks = " + counterChicks);

        remoteViews.setTextViewText(R.id.widget_counter_freaks, String.valueOf(counterFreaks));
        remoteViews.setTextViewText(R.id.widget_counter_chicks, String.valueOf(counterChicks));

        Log.i("BLOGGGG Widget |||| ", "updateCoiunters myWidgetId = " + myWidgetId);

    }*/


    /*@Override
    public void onReceive(Context context, Intent intent) {

        Log.i("BLOGGGG Widget |||| ", "inside ChicksAndFreaksWidget onReceive");

        Log.i("BLOGGGG Widget |||| ", "onUpdate ChicksAndFreaksWidget intent.getAction() = " + intent.getAction());


        if (Data.KEY_UPDATE_WIDGET.equalsIgnoreCase(intent.getAction())) {

            Log.i("BLOGGGG Widget |||| ", "inside ChicksAndFreaksWidget IF");

            int tmp = counterFreaks;
            counterFreaks = intent.getIntExtra(Data.KEY_NUMBER_FREAK, tmp);
            tmp = counterChicks;
            counterChicks = intent.getIntExtra(Data.KEY_NUMBER_CHICK, tmp);

            //RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_screen);
            //AppWidgetManager manager = AppWidgetManager.getInstance(context);
            //updateCoiunters(remoteViews, manager);

            updateWidget(context, AppWidgetManager.getInstance(context), myWidgetId);

            intent.setAction("");
            intent.setFlags(0);
            intent.removeExtra(Data.KEY_NUMBER_FREAK);
            intent.removeExtra(Data.KEY_NUMBER_CHICK);

            //Toast.makeText(context, "widget onReceive, freaks = " + counterFreaks + ", chcks = " + counterChicks, Toast.LENGTH_SHORT).show();

            //updateWidget(context, AppWidgetManager.getInstance(context), myWidgetId);

        }

        super.onReceive(context, intent);

    }*/



}
